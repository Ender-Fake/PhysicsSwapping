package com.enderium.physicsswapping.client.config;

import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.config.data.Property;
import com.enderium.physicsswapping.client.render.AnimationData;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.enderium.physicsswapping.PhysicsSwapping.MODID;

public class PhysicsSwappingConfig {

    private static final Logger log = LoggerFactory.getLogger(PhysicsSwappingConfig.class);

    public static class Keys {

        public static final String ENABLED_KEY = "enabled";
        public static final String DURATION_KEY = "duration";
        public static final String BOUNCE_KEY = "bounce";
        public static final String Y_OFFSET_KEY = "y_offset";
        public static final String ANGLE_KEY = "angle";
        public static final String ITEM_SCALE_KEY = "item_scale";
    }

    public static class Limits {
        public static final DualFloatRange DURATION_LIMIT = new DualFloatRange(0.2f, 5);
        public static final DualIntRange BOUNCE_LIMIT = new DualIntRange(2, 5);
        public static final DualFloatRange Y_OFFSET_LIMIT = new DualFloatRange(0f, 32f);
        public static final DualFloatRange ANGLE_LIMIT = new DualFloatRange(0f, 45f);
        public static final DualFloatRange ITEM_SCALE_LIMIT = new DualFloatRange(0.5f, 2f);
    }

    public static class Defaults {

        public static final boolean ENABLED_VALUE = true;
        public static final DualFloatRange DURATION_VALUE = new DualFloatRange(0.75f, 1.5f);
        public static final DualIntRange BOUNCE_VALUE = new DualIntRange(3, 4);
        public static final DualFloatRange Y_OFFSET_VALUE = new DualFloatRange(2, 4);
        public static final DualFloatRange ANGLE_VALUE = new DualFloatRange(10, 15);
        public static final DualFloatRange ITEM_SCALE_VALUE = new DualFloatRange(1.08f, 1.15f);

        public static final AnimationData SWAP_DEFAULT = new AnimationData(
                DURATION_VALUE,
                BOUNCE_VALUE,
                Y_OFFSET_VALUE,
                ANGLE_VALUE,
                ITEM_SCALE_VALUE
        );

    }


    public static class Values {

        public static boolean enabled = Defaults.ENABLED_VALUE;

        public static final DualFloatRange duration = new DualFloatRange(Defaults.DURATION_VALUE);
        public static final DualIntRange bounce = new DualIntRange(Defaults.BOUNCE_VALUE);
        public static final DualFloatRange yOffset = new DualFloatRange(Defaults.Y_OFFSET_VALUE);
        public static final DualFloatRange angle = new DualFloatRange(Defaults.ANGLE_VALUE);
        public static final DualFloatRange itemScale = new DualFloatRange(Defaults.ITEM_SCALE_VALUE);

        public static final AnimationData SWAP_DATA = new AnimationData(
                duration,
                bounce,
                yOffset,
                angle,
                itemScale
        );


    }


    public static class ConfigFields {

        public static final Property<DualFloatRange> DURATION = Values.duration.createProperty(Keys.DURATION_KEY, Defaults.DURATION_VALUE);
        public static final Property<DualIntRange> BOUNCE = Values.bounce.createProperty(Keys.BOUNCE_KEY, Defaults.BOUNCE_VALUE);
        public static final Property<DualFloatRange> Y_OFFSET = Values.yOffset.createProperty(Keys.Y_OFFSET_KEY, Defaults.Y_OFFSET_VALUE);
        public static final Property<DualFloatRange> ANGLE = Values.angle.createProperty(Keys.ANGLE_KEY, Defaults.ANGLE_VALUE);
        public static final Property<DualFloatRange> ITEM_SCALE = Values.itemScale.createProperty(Keys.ITEM_SCALE_KEY, Defaults.ITEM_SCALE_VALUE);


    }


    public static void save() {
        Path path = getConfigPath();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            Properties properties = new Properties();

            properties.setProperty(Keys.ENABLED_KEY, String.valueOf(Values.enabled));


            ConfigFields.DURATION.save(properties);
            ConfigFields.BOUNCE.save(properties);
            ConfigFields.Y_OFFSET.save(properties);
            ConfigFields.ANGLE.save(properties);
            ConfigFields.ITEM_SCALE.save(properties);


            properties.store(writer, null);
        } catch (IOException e) {
            log.error("Error saving configuration", e);
        }


    }

    public static void load() {
        Path path = getConfigPath();
        if (Files.notExists(path)) save();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Properties properties = new Properties();
            properties.load(reader);


            Values.enabled = Boolean.parseBoolean(properties.getProperty(Keys.ENABLED_KEY, String.valueOf(Defaults.ENABLED_VALUE)));

            ConfigFields.DURATION.load(properties);
            ConfigFields.BOUNCE.load(properties);
            ConfigFields.Y_OFFSET.load(properties);
            ConfigFields.ANGLE.load(properties);
            ConfigFields.ITEM_SCALE.load(properties);

        } catch (IOException e) {
            log.error("Error loading configuration", e);
        }


    }

    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(MODID + ".properties");
    }

}
