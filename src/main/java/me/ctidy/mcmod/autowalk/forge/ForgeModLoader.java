package me.ctidy.mcmod.autowalk.forge;

import me.ctidy.mcmod.autowalk.ModEnvConstants;
import me.ctidy.mcmod.autowalk.client.AutoWalkClient;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * ForgeModLoader
 *
 * @author ctidy
 * @since 2023/8/31
 */
@Mod(ModEnvConstants.MOD_ID)
public class ForgeModLoader {

    public ForgeModLoader() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(
                () -> "ANY", (remote, isServer) -> true
        ));
        if (Dist.CLIENT == FMLEnvironment.dist) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeyMappings);
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AutoWalkClientConfig.SPEC);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        AutoWalkClient.AUTO_WALK_KEY.setKeyConflictContext(KeyConflictContext.IN_GAME);
        event.register(AutoWalkClient.AUTO_WALK_KEY);
    }

}
