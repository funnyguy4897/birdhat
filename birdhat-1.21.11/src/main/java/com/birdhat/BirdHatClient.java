package com.birdhat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

/**
 * Client entrypoint. Adds the bird layer to every player renderer
 * (default and slim skins).
 */
public class BirdHatClient implements ClientModInitializer {
    public static final String MOD_ID = "birdhat";

    @Override
    public void onInitializeClient() {
        BirdConfig.load();

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, helper, context) -> {
            if (entityRenderer instanceof AvatarRenderer<?> avatarRenderer) {
                helper.register(new BirdLayer<>(avatarRenderer));
            }
        });
    }
}
