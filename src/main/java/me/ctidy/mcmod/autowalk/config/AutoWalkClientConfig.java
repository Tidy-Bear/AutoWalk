package me.ctidy.mcmod.autowalk.config;

import com.electronwill.nightconfig.core.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * AutoWalkConfig
 *
 * @author ctidy
 * @since 2023/8/7
 */
@OnlyIn(Dist.CLIENT)
public class AutoWalkClientConfig {

    public static final ForgeConfigSpec SPEC;
    public static final AutoWalkClientConfig INSTANCE;

    public final ForgeConfigSpec.BooleanValue showHud;
    public final ForgeConfigSpec.ConfigValue<Integer> hudOffsetY;
    public final ForgeConfigSpec.BooleanValue autoJump;

    static {
        Config.setInsertionOrderPreserved(true);
        Pair<AutoWalkClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AutoWalkClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    public AutoWalkClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("hud");

        showHud = builder.comment("Whether show message horizontally centered on the screen when auto walking.")
                .define("enable", true);
        hudOffsetY = builder.comment("The vertical offset from the top of the screen.")
                .define("offset_y", 10);

        builder.pop().push("action");

        autoJump = builder.comment("When set to true, player can auto jump when auto walking.\nWhen set to false, it will depend on the vanilla settings.")
                .define("auto_jump", true);

        builder.pop();
    }

}
