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
 * A chubby, googly-eyed songbird, about one block tall.
 * Model space is Y-down, feet at y = 0, beak points toward -Z (the way the player faces).
 */
public class BirdModel extends Model<BirdRenderState> {
    private final ModelPart bird, body, head, leftEye, rightEye, leftPupil, rightPupil;
    private final ModelPart lowerBeak, crest, crestTip, leftWing, rightWing, tail, leftLeg, rightLeg;

    public BirdModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutNoCull);
        bird = root.getChild("bird");
        body = bird.getChild("body");
        head = bird.getChild("head");
        leftEye = head.getChild("left_eye");
        rightEye = head.getChild("right_eye");
        leftPupil = leftEye.getChild("pupil");
        rightPupil = rightEye.getChild("pupil");
        lowerBeak = head.getChild("lower_beak");
        crest = head.getChild("crest");
        crestTip = crest.getChild("tip");
        leftWing = bird.getChild("left_wing");
        rightWing = bird.getChild("right_wing");
        tail = bird.getChild("tail");
        leftLeg = bird.getChild("left_leg");
        rightLeg = bird.getChild("right_leg");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition bird = mesh.getRoot().addOrReplaceChild("bird", CubeListBuilder.create(), PartPose.ZERO);

        bird.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3F, -5F, -3F, 6, 5, 6), PartPose.offset(0F, -2F, 0F));

        PartDefinition head = bird.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11)
                .addBox(-2.5F, -5F, -2.5F, 5, 5, 5), PartPose.offset(0F, -6.5F, -1F));

        // Big googly eyes that bulge out past the sides of the head
        PartDefinition le = head.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(24, 0)
                .addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1), PartPose.offset(1.5F, -3F, -2.7F));
        le.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(40, 0)
                .addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(0F, 0F, -0.6F));
        PartDefinition re = head.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1), PartPose.offset(-1.5F, -3F, -2.7F));
        re.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(44, 0)
                .addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(0F, 0F, -0.6F));

        // Chunky beak; the lower half hinges open
        head.addOrReplaceChild("upper_beak", CubeListBuilder.create().texOffs(24, 4)
                .addBox(-1.5F, -1.5F, -5.5F, 3, 1, 3), PartPose.ZERO);
        head.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(36, 4)
                .addBox(-1F, 0F, -3F, 2, 1, 3), PartPose.offset(0F, -0.5F, -2.5F));

        // Floppy two-piece crest
        PartDefinition crest = head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(48, 0)
                .addBox(-0.5F, -4F, -1F, 1, 4, 3), PartPose.offsetAndRotation(0F, -5F, 0F, -0.3F, 0F, 0F));
        crest.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(56, 0)
                .addBox(-0.5F, -3F, 0F, 1, 3, 2), PartPose.offsetAndRotation(0F, -4F, 0F, -0.4F, 0F, 0F));

        bird.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 22)
                .addBox(0F, 0F, -2.5F, 1, 4, 5), PartPose.offset(3F, -6.5F, 0F));
        bird.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(12, 22)
                .addBox(-1F, 0F, -2.5F, 1, 4, 5), PartPose.offset(-3F, -6.5F, 0F));

        bird.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 11)
                .addBox(-2F, -0.5F, 0F, 4, 1, 4), PartPose.offsetAndRotation(0F, -4F, 2.5F, 0.5F, 0F, 0F));

        bird.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(24, 16).addBox(-0.5F, 0F, -0.5F, 1, 1, 1)
                .texOffs(32, 16).addBox(-1F, 1F, -2F, 2, 1, 2), PartPose.offset(1.5F, -2F, 0.5F));
        bird.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(28, 16).addBox(-0.5F, 0F, -0.5F, 1, 1, 1)
                .texOffs(40, 16).addBox(-1F, 1F, -2F, 2, 1, 2), PartPose.offset(-1.5F, -2F, 0.5F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    /** 0 = 0 at the start/end of a window, peaks at 1 in the middle. */
    private static float window(float t, float period, float length) {
        float c = t % period;
        return c < length ? Mth.sin(c / length * Mth.PI) : 0F;
    }

    @Override
    public void setupAnim(BirdRenderState s) {
        this.resetPose();
        float t = s.ageInTicks;
        float g = BirdConfig.goofiness;          // 0 chill, 1 goofy, 2 chaos
        float goof = 0.35F + 0.65F * g;          // overall silliness multiplier
        float run = Math.min(1F, s.walkSpeed);

        // Squash and stretch bounce (scales around the feet)
        float squash = Mth.sin(t * 0.25F * (1F + 0.5F * g)) * 0.07F * goof;
        bird.yScale = 1F + squash;
        bird.xScale = bird.zScale = 1F - squash * 0.6F;

        // Looking around + pigeon head bob (headbanging while you walk)
        head.yRot = Mth.sin(t * 0.031F) * 0.5F + Mth.sin(t * 0.083F) * 0.25F;
        head.z += Mth.sin(t * 0.3F) * 0.5F * goof + Mth.sin(t * 0.9F) * 1.4F * run;
        head.xRot = Mth.sin(t * 0.9F) * 0.35F * run;

        // Every ~8 seconds: a big confused head tilt
        head.zRot = Mth.sin(t * 0.047F) * 0.15F + window(t + 40F, 160F, 40F) * 0.75F * goof;

        // Googly pupils rattle around inside the eyes, and go cross-eyed now and then
        float jig = 0.45F + 0.25F * g + 0.4F * run;
        leftPupil.x = Mth.sin(t * 0.37F) * jig;
        leftPupil.y = Mth.cos(t * 0.29F) * jig;
        rightPupil.x = Mth.sin(t * 0.41F + 2F) * jig;
        rightPupil.y = Mth.cos(t * 0.33F + 1F) * jig;
        float crossEyed = window(t, 130F, 30F) * Math.min(1F, g);
        leftPupil.x = Mth.lerp(crossEyed, leftPupil.x, -0.9F);
        rightPupil.x = Mth.lerp(crossEyed, rightPupil.x, 0.9F);
        leftPupil.y = Mth.lerp(crossEyed, leftPupil.y, 0F);
        rightPupil.y = Mth.lerp(crossEyed, rightPupil.y, 0F);
        float pop = 1F + window(t, 130F, 30F) * 0.25F * goof;
        leftEye.xScale = leftEye.yScale = rightEye.xScale = rightEye.yScale = pop;

        // Beak chatters every few seconds, screams while running
        float chatter = window(t, 70F, 22F) > 0F ? Math.max(0F, Mth.sin(t * 1.8F)) * 0.6F * goof : 0F;
        lowerBeak.xRot = Math.max(chatter, run * (0.6F + 0.2F * Mth.sin(t * 1.5F)));

        // Floppy crest
        crest.zRot = Mth.sin(t * 0.2F) * 0.25F * goof + Mth.sin(t * 0.9F) * 0.3F * run;
        crestTip.zRot = Mth.sin(t * 0.2F - 0.9F) * 0.5F * goof + Mth.sin(t * 0.9F - 0.9F) * 0.5F * run;
        crest.xRot += -0.4F * run;

        // Tail wag
        tail.yRot = Mth.sin(t * 0.4F) * 0.35F * goof;
        tail.xRot += Mth.sin(t * 0.25F) * 0.1F;

        // Hop + wing flutter every 5 seconds
        float flap = 0F;
        float hopCycle = t % 100F;
        if (hopCycle < 14F) {
            float p = hopCycle / 14F;
            flap = Mth.sin(hopCycle * 1.7F) * 1.1F * (1F - p);
            bird.y -= Mth.sin(p * Mth.PI) * (2F + 2F * g);
        }
        // Running: flail!
        if (run > 0.1F) flap = Math.max(flap, (0.7F + 0.9F * Math.abs(Mth.sin(t * 2F))) * run);
        leftWing.zRot = -flap;
        rightWing.zRot = flap;

        // Full 360 spin: every 12 s when goofy, every 6 s on chaos
        if (g >= 1F) {
            float period = g >= 2F ? 120F : 240F;
            float c = (t + 60F) % period;
            if (c < 20F) {
                float p = c / 20F;
                bird.yRot = (p * p * (3F - 2F * p)) * (Mth.PI * 2F);
                bird.y -= Mth.sin(p * Mth.PI) * 1.5F;
            }
        }

        // Chaos: little tap dance
        if (g >= 2F) {
            leftLeg.y -= Math.max(0F, Mth.sin(t * 0.6F)) * 0.8F;
            rightLeg.y -= Math.max(0F, -Mth.sin(t * 0.6F)) * 0.8F;
            body.zRot = Mth.sin(t * 0.6F) * 0.12F;
        }

        // Sneak = pancake
        if (s.crouching) {
            bird.yScale *= 0.65F;
            bird.xScale *= 1.25F;
            bird.zScale *= 1.25F;
        }
    }
}
