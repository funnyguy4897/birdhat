package com.birdhat;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

/**
 * A small original songbird, about 5 pixels tall.
 * Model space is Y-down, feet sit at y = 0, beak points toward -Z (same way the player faces).
 */
public class BirdModel extends Model<BirdRenderState> {
    private final ModelPart bird;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;

    public BirdModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.bird = root.getChild("bird");
        this.body = bird.getChild("body");
        this.head = bird.getChild("head");
        this.leftWing = bird.getChild("left_wing");
        this.rightWing = bird.getChild("right_wing");
        this.tail = bird.getChild("tail");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition bird = root.addOrReplaceChild("bird", CubeListBuilder.create(), PartPose.ZERO);

        bird.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-1.5F, -3.0F, -2.0F, 3, 3, 4), PartPose.offset(0F, -2F, 0F));

        PartDefinition head = bird.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-1.5F, -3.0F, -1.5F, 3, 3, 3), PartPose.offset(0F, -4.5F, -1.5F));
        head.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 8)
                .addBox(-0.5F, -1.5F, -3.5F, 1, 1, 2), PartPose.ZERO);
        head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(20, 8)
                .addBox(-0.5F, -5.0F, -1.0F, 1, 2, 2), PartPose.rotation(-0.35F, 0F, 0F));

        bird.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 14)
                .addBox(0F, 0F, -2F, 1, 2, 4), PartPose.offset(1.5F, -4.8F, 0.2F));
        bird.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(10, 14)
                .addBox(-1F, 0F, -2F, 1, 2, 4), PartPose.offset(-1.5F, -4.8F, 0.2F));

        bird.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 0)
                .addBox(-1F, -0.5F, 0F, 2, 1, 3), PartPose.offsetAndRotation(0F, -3.2F, 1.8F, 0.45F, 0F, 0F));

        bird.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 20)
                .addBox(-0.5F, 0F, -0.5F, 1, 2, 1), PartPose.offset(0.8F, -2F, 0.3F));
        bird.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(4, 20)
                .addBox(-0.5F, 0F, -0.5F, 1, 2, 1), PartPose.offset(-0.8F, -2F, 0.3F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(BirdRenderState s) {
        this.resetPose();
        float t = s.ageInTicks;

        // Gentle breathing bob
        bird.y += Mth.sin(t * 0.12F) * 0.25F;

        // Looking around: two slow waves added together feel less robotic than one
        head.yRot = Mth.sin(t * 0.031F) * 0.55F + Mth.sin(t * 0.083F) * 0.25F;
        head.zRot = Mth.sin(t * 0.047F) * 0.18F;
        head.xRot = Mth.sin(t * 0.06F) * 0.08F;

        // Idle tail flick
        tail.xRot += Mth.sin(t * 0.25F) * 0.08F;

        // Every ~5 seconds: a little hop with a wing flutter
        float cycle = t % 100F;
        float flap = 0F;
        if (cycle < 14F) {
            float p = cycle / 14F;
            flap = Mth.sin(cycle * 1.7F) * 0.9F * (1F - p);
            bird.y -= Mth.sin(p * Mth.PI) * 1.4F;
            tail.xRot -= 0.25F * (1F - p);
        }

        // While the player runs, the bird leans forward and keeps fluttering for balance
        float run = Math.min(1F, s.walkSpeed);
        if (run > 0.2F) {
            body.xRot += 0.25F * run;
            flap = Math.max(flap, (0.4F + 0.5F * Math.abs(Mth.sin(t * 1.3F))) * run);
            tail.xRot -= 0.2F * run;
        }

        leftWing.zRot = -flap;
        rightWing.zRot = flap;

        // Hunker down when sneaking
        if (s.crouching) {
            bird.y += 0.6F;
            head.y += 0.4F;
        }
    }
}
