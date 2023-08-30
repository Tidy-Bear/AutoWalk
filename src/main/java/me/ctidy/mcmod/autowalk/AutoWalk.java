package me.ctidy.mcmod.autowalk;

import me.ctidy.mcmod.autowalk.client.ClientEventHandler;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AutoWalk.MODID)
public class AutoWalk {

    public static final String MODID = "autowalk";

    public AutoWalk() {
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
        event.register(ClientEventHandler.AUTO_WALK_KEY);
    }

}
