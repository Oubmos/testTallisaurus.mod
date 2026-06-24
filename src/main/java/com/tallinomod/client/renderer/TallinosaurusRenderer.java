package com.tallinomod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tallinomod.TallinoMod;
import com.tallinomod.client.model.TallinosaurusModel;
import com.tallinomod.entity.TallinosaurusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TallinosaurusRenderer extends MobRenderer<TallinosaurusEntity, TallinosaurusModel> {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation(TallinoMod.MOD_ID, "textures/entity/tallinosaurus.png");

    public TallinosaurusRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TallinosaurusModel(
            TallinosaurusModel.createBodyLayer().bakeRoot()), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(TallinosaurusEntity entity) { return TEXTURE; }

    @Override
    protected void scale(TallinosaurusEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }
}
