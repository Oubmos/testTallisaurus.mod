package com.tallinomod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

public class TallinosaurusEntity extends Animal {

    private static final double FOLLOW_RANGE = 10.0D, FOLLOW_STOP_RANGE = 14.0D, BASE_SPEED = 0.08D;
    private static final int PANIC_TICKS = 100;
    private int panicCooldown = 0;

    public TallinosaurusEntity(EntityType<? extends TallinosaurusEntity> type, Level level) { super(type, level); }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
            .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE * 2)
            .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, BASE_SPEED * 1.5D));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CuriosityFollowGoal(this, BASE_SPEED, (float) FOLLOW_RANGE, (float) FOLLOW_STOP_RANGE));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, BASE_SPEED));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override public boolean isPanicking() { return this.panicCooldown > 0; }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.panicCooldown > 0) this.panicCooldown--;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean damaged = super.hurt(source, amount);
        if (damaged && !this.level().isClientSide()) this.panicCooldown = PANIC_TICKS;
        return damaged;
    }

    @Nullable @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) { return null; }

    @Override public boolean isFood(ItemStack stack) { return false; }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.GENERIC_NEUTRAL; }
    @Override protected SoundEvent getHurtSound(DamageSource src) { return SoundEvents.GENERIC_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.GENERIC_DEATH; }

    // ── Inner Goal ───────────────────────────────────────────────────────────

    private static class CuriosityFollowGoal extends Goal {
        private final TallinosaurusEntity dino;
        private final double speed;
        private final float startRange, stopRange;
        @Nullable private Player target;
        private int recalc = 0;

        CuriosityFollowGoal(TallinosaurusEntity dino, double speed, float startRange, float stopRange) {
            this.dino = dino; this.speed = speed;
            this.startRange = startRange; this.stopRange = stopRange;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.dino.isPanicking()) return false;
            Player p = this.dino.level().getNearestPlayer(this.dino, this.startRange);
            if (p == null) return false;
            this.target = p;
            return true;
        }

        @Override
        public boolean canContinue() {
            return this.target != null && !this.dino.isPanicking()
                && this.dino.distanceToSqr(this.target) <= (double)(this.stopRange * this.stopRange);
        }

        @Override public void start() { this.recalc = 0; }
        @Override public void stop() { this.target = null; this.dino.getNavigation().stop(); }

        @Override
        public void tick() {
            if (this.target == null) return;
            this.dino.getLookControl().setLookAt(this.target, 10.0F, (float) this.dino.getMaxHeadXRot());
            if (--this.recalc <= 0) { this.recalc = 10; this.dino.getNavigation().moveTo(this.target, this.speed); }
        }
    }
}
