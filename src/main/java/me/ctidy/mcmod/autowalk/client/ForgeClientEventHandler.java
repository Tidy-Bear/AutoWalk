package me.ctidy.mcmod.autowalk.client;

import me.ctidy.mcmod.autowalk.ModEnvConstants;
import me.ctidy.mcmod.autowalk.api.IAutoWalkable;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * ForgeClientEventHandler
 *
 * @author ctidy
 * @since 2023/8/7
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModEnvConstants.MOD_ID)
public final class ForgeClientEventHandler {

    /**
     * Calc tick in a cycle of 20.
     */
    @SubscribeEvent
    public static void onClientTickEnd(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        AutoWalkClient.instance().tick();
    }

    /**
     * Draw message horizontally centered and vertically up on the screen when auto walking.
     */
    @SubscribeEvent
    public static void drawHUD(final RenderGuiOverlayEvent.Post event) {
        if (!AutoWalkClientConfig.INSTANCE.showHud.get() ||
                event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
        AutoWalkClient.instance().drawHud(event.getWindow(), event.getPoseStack());
    }

    /**
     * Try to toggle auto walking when certain mouse button is pressed.
     */
    @SubscribeEvent
    public static void onMouseInput(final InputEvent.MouseButton.Pre event) {
        event.setCanceled(AutoWalkClient.instance().acceptInput());
    }

    /**
     * Try to toggle auto walking when certain key is pressed.
     */
    @SubscribeEvent
    public static void onKeyInput(final InputEvent.Key event) {
        AutoWalkClient.instance().acceptInput();
    }

    /**
     * Keep player forward when auto walking.
     */
    @SubscribeEvent
    public static void onMovementInput(final MovementInputUpdateEvent event) {
        AutoWalkClient.instance().autoForward(event.getEntity());
    }

    /**
     * Reset auto walk state when level unloaded
     */
    @SubscribeEvent
    public static void onLevelUnloaded(final LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        ((IAutoWalkable) mc.player).stopAutoWalk();
    }

    private ForgeClientEventHandler() {
        throw new IllegalStateException("Instantiation disallowed.");
    }

}
