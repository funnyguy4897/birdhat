package com.birdhat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Tiny config stored at .minecraft/config/birdhat.properties
 * (in Prism: Instance folder > minecraft > config).
 */
public final class BirdConfig {
    public static final String[] VARIANTS = {"bluebird", "cardinal", "canary", "robin"};

    public static boolean enabled = true;
    public static String variant = "bluebird";
    public static boolean showOnEveryone = false;
    public static float scale = 1.0f;
    public static float heightOffset = 0.0f;

    private static Identifier texture;

    private BirdConfig() {}

    public static Identifier texture() {
        if (texture == null) {
            texture = Identifier.fromNamespaceAndPath(BirdHatClient.MOD_ID, "textures/entity/bird_" + variant + ".png");
        }
        return texture;
    }

    public static void load() {
        Path file = FabricLoader.getInstance().getConfigDir().resolve("birdhat.properties");
        Properties p = new Properties();
        try {
            if (Files.exists(file)) {
                try (Reader r = Files.newBufferedReader(file)) { p.load(r); }
            }
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
            showOnEveryone = Boolean.parseBoolean(p.getProperty("show_on_everyone", "false"));
            scale = clamp(Float.parseFloat(p.getProperty("scale", "1.0")), 0.25f, 3f);
            heightOffset = clamp(Float.parseFloat(p.getProperty("height_offset_pixels", "0")), -8f, 16f);
            String v = p.getProperty("variant", "bluebird").trim().toLowerCase();
            variant = "bluebird";
            for (String known : VARIANTS) if (known.equals(v)) variant = v;
        } catch (Exception e) {
            System.err.println("[birdhat] Could not read config, using defaults: " + e);
        }
        save(file);
    }

    private static void save(Path file) {
        Properties p = new Properties();
        p.setProperty("enabled", String.valueOf(enabled));
        p.setProperty("variant", variant);
        p.setProperty("show_on_everyone", String.valueOf(showOnEveryone));
        p.setProperty("scale", String.valueOf(scale));
        p.setProperty("height_offset_pixels", String.valueOf(heightOffset));
        try {
            Files.createDirectories(file.getParent());
            try (Writer w = Files.newBufferedWriter(file)) {
                p.store(w, "Bird Hat config. variant = bluebird | cardinal | canary | robin. "
                        + "height_offset_pixels raises the bird (e.g. 1 if it clips into helmets). Restart the game after editing.");
            }
        } catch (Exception e) {
            System.err.println("[birdhat] Could not write config: " + e);
        }
    }

    private static float clamp(float v, float lo, float hi) { return Math.max(lo, Math.min(hi, v)); }
}
