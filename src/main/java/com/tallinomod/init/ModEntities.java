package com.tallinomod.init;

import com.tallinomod.TallinoMod;
import com.tallinomod.entity.TallinosaurusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TallinoMod.MOD_ID);

    public static final RegistryObject<EntityType<TallinosaurusEntity>> TALLINOSAURUS =
        ENTITY_TYPES.register("tallinosaurus", () ->
            EntityType.Builder.<TallinosaurusEntity>of(TallinosaurusEntity::new, MobCategory.CREATURE)
                .sized(0.8F, 2.0F)
                .clientTrackingRange(10)
                .build("tallinosaurus"));
}
