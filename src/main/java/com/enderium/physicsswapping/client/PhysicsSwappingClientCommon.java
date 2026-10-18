package com.enderium.physicsswapping.client;

import com.enderium.physicsswapping.client.config.PhysicsSwappingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.enderium.physicsswapping.PhysicsSwappingCommon.MODID;

public class PhysicsSwappingClientCommon {

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID+"-client");

    public static void initialize() {

        PhysicsSwappingConfig.load();


    }


}
