package com.birdhat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;

/**
 * Client entrypoint. Adds the bird layer to every player renderer
 * and a key (B by default) that opens the Bird Wardrobe.
 */
public class BirdHatClient implements ClientModInitializer {
    public static final String MOD_ID = "birdhat";

    private static KeyMapping openWardrobe;

    @Override
    public void onInitializeClient() {
        BirdConfig.load();

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, helper, context) -> {
            if (entityRenderer instanceof AvatarRenderer<?> avatarRenderer) {
                helper.register(new BirdLayer<>(avatarRenderer));
            }
        });

        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "birdhat"));
        openWardrobe = KeyBindingHelper.registerKeyBinding(
                new KeyMapping("key.birdhat.wardrobe", InputConstants.KEY_B, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openWardrobe.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    client.setScreen(new BirdScreen());
                }
            }
        });
    }
}
