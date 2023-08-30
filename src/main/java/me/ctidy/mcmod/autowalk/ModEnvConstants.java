package me.ctidy.mcmod.autowalk;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ModEnvConstants
 *
 * @author ctidy
 * @since 2023/8/31
 */
public class ModEnvConstants {

    public static final String MOD_ID = "autowalk";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation id(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

}
