package com.tallinomod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tallinomod.entity.TallinosaurusEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class TallinosaurusModel extends EntityModel<TallinosaurusEntity> {

    private final ModelPart root;
    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart legRight;
    private final ModelPart legLeft;
    private final ModelPart armRight;
    private final ModelPart armLeft;

    public TallinosaurusModel(ModelPart root) {
        this.root     = root;
        this.torso    = root.getChild("torso");
        this.head     = torso.getChild("head");
        this.tail     = torso.getChild("tail");
        this.legRight = root.getChild("leg_right");
        this.legLeft  = root.getChild("leg_left");
        this.armRight = torso.getChild("arm_right");
        this.armLeft  = torso.getChild("arm_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();

        // ── Torso ────────────────────────────────────────────────
        PartDefinition torso = parts.addOrReplaceChild("torso",
            CubeListBuilder.create()
                .texOffs(38, 22).addBox(-4, -13,  -3,  8, 13, 9)   // lower torso
                .texOffs(46,  0).addBox(-3, -26,  -1,  6, 13, 6)   // mid torso
                .texOffs( 0, 44).addBox(-4, -34,  -4,  8,  8, 8),  // upper torso/chest
            PartPose.offset(0, 21, 0));

        // ── Head (child of torso) ─────────────────────────────────
        torso.addOrReplaceChild("head",
            CubeListBuilder.create()
                .texOffs(64, 44).addBox(-2, -6, -6, 4, 6, 6)       // skull
                .texOffs(22, 60).addBox(-1,  0, -3, 2, 8, 2)       // neck lower
                .texOffs(22, 70).addBox(-1,  4, -7, 2, 4, 2)       // snout
                .texOffs(70,  0).addBox(-1,  5,-13, 2, 8, 2)       // snout tip
                .texOffs(70, 10).addBox(-1,  4,-11, 2, 8, 1),      // jaw
            PartPose.offsetAndRotation(0, -34, -4, -1.5708F, 0, 0));

        // ── Tail (child of torso) ─────────────────────────────────
        torso.addOrReplaceChild("tail",
            CubeListBuilder.create()
                .texOffs( 0, 22).addBox(-3, -9, 2, 6, 9,13)        // tail base
                .texOffs(32, 44).addBox(-3,-13,13, 6, 5,10)        // tail mid
                .texOffs( 0, 60).addBox(-1,-15,40, 2, 2, 9),       // tail tip
            PartPose.offsetAndRotation(0, -13, 0, 0.4363F, 0, 0));

        // ── Arm Right (child of torso) ────────────────────────────
        torso.addOrReplaceChild("arm_right",
            CubeListBuilder.create()
                .texOffs( 8, 71).addBox(-1,-7,-5, 1, 7, 1)         // upper arm
                .texOffs( 0, 71).addBox(-2,-2,-5, 2, 2, 2)         // hand
                .texOffs(72, 27).addBox(-1,-4,-6, 1, 4, 1),        // forearm
            PartPose.offset(4, -34, -4));

        // ── Arm Left (child of torso) ─────────────────────────────
        torso.addOrReplaceChild("arm_left",
            CubeListBuilder.create()
                .texOffs(72, 19).addBox( 0,-7,-5, 1, 7, 1)         // upper arm
                .texOffs(12, 71).addBox( 0,-2,-5, 2, 2, 2)         // hand
                .texOffs(72, 32).addBox( 0,-4,-6, 1, 4, 1),        // forearm
            PartPose.offset(-4, -34, -4));

        // ── Legs (direct children of root) ────────────────────────
        parts.addOrReplaceChild("leg_right",
            CubeListBuilder.create()
                .texOffs(32, 59).addBox(-1, -32, -1, 2, 32, 2)     // thigh+shin
                .texOffs(64, 56).addBox(-1,  -8, -2, 3,  8, 4),    // foot
            PartPose.offset(-3, 21, 0));

        parts.addOrReplaceChild("leg_left",
            CubeListBuilder.create()
                .texOffs(48, 59).addBox(-1, -32, -1, 2, 32, 2)
                .texOffs(64, 68).addBox(-2,  -8, -2, 3,  8, 4),
            PartPose.offset(3, 21, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(TallinosaurusEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = entity.isPanicking() ? 1.4F : 1.0F;

        // Leg swing
        legRight.xRot =  Mth.cos(limbSwing * 0.6662F)           * 1.4F * limbSwingAmount * speed;
        legLeft.xRot  =  Mth.cos(limbSwing * 0.6662F + Mth.PI)  * 1.4F * limbSwingAmount * speed;

        // Arm counter-swing
        armRight.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.5F * limbSwingAmount;
        armLeft.xRot  = Mth.cos(limbSwing * 0.6662F)           * 0.5F * limbSwingAmount;

        // Tail wag
        tail.xRot = 0.4363F + Mth.cos(ageInTicks * 0.1F) * 0.15F;

        // Head tracking
        head.yRot   = netHeadYaw  * (Mth.PI / 180F) * 0.5F;
        head.xRot   = headPitch   * (Mth.PI / 180F) * 0.5F - 1.5708F;

        // Idle body bob
        torso.y = 21 + Mth.sin(ageInTicks * 0.05F) * 0.3F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
