package com.clonz.blastfromthepast.util;

import net.neoforged.fml.loading.FMLEnvironment;

public class DebugFlags {
    public static final boolean DEBUG_ANIMATED_ATTACK = !FMLEnvironment.production;
    public static final boolean DEBUG_ENTITY_PACK = !FMLEnvironment.production;
    public static final boolean DEBUG_CHARGE_FORWARD = false;
    public static final boolean DEBUG_BEAR_EAT = !FMLEnvironment.production;
    public static final boolean DEBUG_RAID_FOOD_CONTAINER = !FMLEnvironment.production && false;
    public static final boolean DEBUG_SLEEP = !FMLEnvironment.production;
    public static final boolean DEBUG_ROAR = !FMLEnvironment.production;
    public static final boolean DEBUG_BACK_SCRATCH = !FMLEnvironment.production;
    public static final boolean DEBUG_HOLLOW = !FMLEnvironment.production;
}
