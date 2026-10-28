package com.enderium.physicsswapping;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysicsSwappingCommon {

    public static final String MODID = "physicsswapping";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = /*$ mod_version*/ "1.4.0";
    public static final String MINECRAFT = /*$ minecraft*/ "26.3";

    /**
     * Adapts to the {@link Identifier} changes introduced in 1.21.
     */
    public static Identifier id(String namespace, String path) {
        //? if <1.21 {
        /*return new Identifier(namespace, path);
         *///?} else
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier idDefault(String path) {
        //? if <1.21 {
        /*return Identifier.tryParse(path);
        *///?} else
        return Identifier.withDefaultNamespace(path);
    }

}
