package me.ctidy.mcmod.autowalk.config;

import com.electronwill.nightconfig.core.Config;
import me.ctidy.mcmod.autowalk.ModEnvConstants;
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

    private static String nameTKey(final String key) {
        return ModEnvConstants.MOD_ID + ".config." + key;
    }

    private static String commentTKey(final String key) {
        return nameTKey(key) + ".tooltip";
    }

    public AutoWalkClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("hud").translation(nameTKey("section.hud"));

        showHud = builder.comment("Whether show message horizontally centered on the screen when auto walking.",
                        "[Default: true]")
                .translation(nameTKey("show_hud"))
                .define("enable", true);
        hudOffsetY = builder.comment("The vertical offset from the top of the screen.",
                        "[Default: 10]")
                .translation(nameTKey("hud_offset_y"))
                .define("offset_y", 10);

        builder.pop().push("action").translation(nameTKey("section.action"));

        autoJump = builder.comment("When set to true, player can auto jump when auto walking.",
                        "When set to false, it will depend on the vanilla settings.",
                        "[Default: true]")
                .translation(nameTKey("auto_jump"))
                .define("auto_jump", true);

        builder.pop();
    }

}
