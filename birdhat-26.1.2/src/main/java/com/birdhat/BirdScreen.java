package com.birdhat;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * In-game "Bird Wardrobe". Turns the camera around so you can see yourself,
 * and every change shows up on your bird right away and is saved.
 */
public class BirdScreen extends Screen {
    private static final float[] SIZES = {0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f};
    private static final int MIN_HEIGHT = -3, MAX_HEIGHT = 6;

    private CameraType previousCamera;

    public BirdScreen() {
        super(Component.literal("Bird Wardrobe"));
    }

    @Override
    protected void init() {
        Minecraft mc = Minecraft.getInstance();
        if (previousCamera == null) {
            previousCamera = mc.options.getCameraType();
            mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
        }

        int w = 150, h = 20, gap = 24;
        int x = 20;
        int y = this.height / 2 - 95;

        addRenderableWidget(new StringWidget(x, y, w, h, Component.literal("§lBird Wardrobe"), this.font));
        y += gap;

        addRenderableWidget(Button.builder(enabledText(), b -> {
            BirdConfig.enabled = !BirdConfig.enabled;
            BirdConfig.save();
            b.setMessage(enabledText());
        }).bounds(x, y, w, h).build());
        y += gap;

        addRenderableWidget(Button.builder(Component.literal("<"), b -> cycleVariant(-1))
                .bounds(x, y, 20, h).build());
        addRenderableWidget(Button.builder(variantText(), b -> cycleVariant(1))
                .bounds(x + 22, y, w - 44, h).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> cycleVariant(1))
                .bounds(x + w - 20, y, 20, h).build());
        y += gap;

        addRenderableWidget(Button.builder(sizeText(), b -> {
            int i = 0;
            for (int k = 0; k < SIZES.length; k++) if (Math.abs(SIZES[k] - BirdConfig.scale) < 0.01f) i = k;
            BirdConfig.scale = SIZES[(i + 1) % SIZES.length];
            BirdConfig.save();
            b.setMessage(sizeText());
        }).bounds(x, y, w, h).build());
        y += gap;

        addRenderableWidget(Button.builder(heightText(), b -> {
            int hgt = Math.round(BirdConfig.heightOffset) + 1;
            if (hgt > MAX_HEIGHT) hgt = MIN_HEIGHT;
            BirdConfig.heightOffset = hgt;
            BirdConfig.save();
            b.setMessage(heightText());
        }).bounds(x, y, w, h).build());
        y += gap;

        addRenderableWidget(Button.builder(everyoneText(), b -> {
            BirdConfig.showOnEveryone = !BirdConfig.showOnEveryone;
            BirdConfig.save();
            b.setMessage(everyoneText());
        }).bounds(x, y, w, h).build());
        y += gap;

        addRenderableWidget(Button.builder(goofText(), b -> {
            BirdConfig.goofiness = (BirdConfig.goofiness + 1) % 3;
            BirdConfig.save();
            b.setMessage(goofText());
        }).bounds(x, y, w, h).build());
        y += gap + 8;

        addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
                .bounds(x, y, w, h).build());
    }

    private void cycleVariant(int dir) {
        String[] v = BirdConfig.VARIANTS;
        int i = 0;
        for (int k = 0; k < v.length; k++) if (v[k].equals(BirdConfig.variant)) i = k;
        BirdConfig.setVariant(v[(i + dir + v.length) % v.length]);
        this.rebuildWidgets();
    }

    private static Component enabledText() {
        return Component.literal("Bird: " + (BirdConfig.enabled ? "§aON" : "§cOFF"));
    }

    private static Component variantText() {
        String v = BirdConfig.variant;
        return Component.literal(Character.toUpperCase(v.charAt(0)) + v.substring(1));
    }

    private static Component sizeText() {
        return Component.literal("Size: " + BirdConfig.scale + "x");
    }

    private static Component heightText() {
        int hgt = Math.round(BirdConfig.heightOffset);
        return Component.literal("Height: " + (hgt >= 0 ? "+" : "") + hgt + " px");
    }

    private static Component goofText() {
        String[] names = {"§bChill", "§eGoofy", "§c§lCHAOS"};
        return Component.literal("Goofiness: " + names[BirdConfig.goofiness]);
    }

    private static Component everyoneText() {
        return Component.literal("Birds on everyone: " + (BirdConfig.showOnEveryone ? "§aON" : "§cOFF"));
    }

    // No blur or dark overlay, so you can see your character behind the menu.
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    }

    // Keep the world (and the bird's animation) running in singleplayer.
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        if (previousCamera != null) {
            Minecraft.getInstance().options.setCameraType(previousCamera);
            previousCamera = null;
        }
        super.removed();
    }
}
