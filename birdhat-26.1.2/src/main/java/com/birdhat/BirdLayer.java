package com.birdhat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Puts the bird on top of the player's head and moves with it. */
public class BirdLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    private BirdModel model;

    public BirdLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    private BirdModel model() {
        if (model == null) model = new BirdModel(BirdModel.createLayer().bakeRoot());
        return model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, S state, float yRot, float xRot) {
        if (!BirdConfig.enabled || state.isInvisible) return;
        if (!BirdConfig.showOnEveryone && !isLocalPlayer(state)) return;

        ModelPart root = this.getParentModel().root();
        if (!root.hasChild("head")) return;

        poseStack.pushPose();
        // Follow the head's position and rotation, then move to the top of it (8 px = 0.5 blocks; Y is down here).
        root.getChild("head").translateAndRotate(poseStack);
        poseStack.translate(0.0F, -0.5F - BirdConfig.heightOffset / 16.0F, 0.0F);
        poseStack.scale(BirdConfig.scale, BirdConfig.scale, BirdConfig.scale);

        BirdModel bird = model();
        BirdRenderState birdState = new BirdRenderState(state.ageInTicks, state.walkAnimationSpeed, state.isDiscrete);
        collector.submitModel(bird, birdState, poseStack, bird.renderType(BirdConfig.texture()),
                light, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
    }

    /**
     * The client only knows about its own cosmetics, so by default we only show the bird on you.
     * Render states don't carry the entity, so match on the interpolated position instead.
     */
    private static boolean isLocalPlayer(EntityRenderState s) {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p == null) return false;
        return between(s.x, p.xOld, p.getX()) && between(s.y, p.yOld, p.getY()) && between(s.z, p.zOld, p.getZ());
    }

    private static boolean between(double v, double a, double b) {
        return v >= Math.min(a, b) - 0.01 && v <= Math.max(a, b) + 0.01;
    }
}
