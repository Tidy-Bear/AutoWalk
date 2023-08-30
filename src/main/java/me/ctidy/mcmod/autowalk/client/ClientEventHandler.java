package me.ctidy.mcmod.autowalk.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import me.ctidy.mcmod.autowalk.AutoWalk;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import me.ctidy.mcmod.autowalk.api.IAutoWalkable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
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
 * ClientEventHandler
 *
 * @author ctidy
 * @since 2023/8/7
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AutoWalk.MODID)
public final class ClientEventHandler {

    public static final KeyMapping AUTO_WALK_KEY =
            new KeyMapping("autowalk.keybind.autowalk", InputConstants.UNKNOWN.getValue(), KeyMapping.CATEGORY_MOVEMENT);

    private static int cyclicTickInGame;

    /**
     * Calc tick in a cycle of 20.
     */
    @SubscribeEvent
    public static void clientTickEnd(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Screen gui = Minecraft.getInstance().screen;
            if (gui == null || !gui.isPauseScreen()) {
                cyclicTickInGame += 1;
                cyclicTickInGame %= 20;
            }
        }
    }

    /**
     * Draw message horizontally centered and vertically up on the screen when auto walking.
     */
    @SubscribeEvent
    public static void drawHUD(final RenderGuiOverlayEvent.Post event) {
        if (!AutoWalkClientConfig.INSTANCE.showHud.get() ||
                event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null ||
                !((IAutoWalkable) mc.player).isAutoWalkEnabled()) return;

        String message = I18n.get("autowalk.misc.autowalking.msg");
        String displayMessage = cyclicTickInGame < 10 ? "OoO " + message + " oOo" : "oOo " + message + " OoO";

        int w = mc.font.width(displayMessage);

        Window window = event.getWindow();
        int x = (window.getGuiScaledWidth() - w) / 2;
        int y = AutoWalkClientConfig.INSTANCE.hudOffsetY.get();

        mc.font.drawShadow(event.getPoseStack(), displayMessage, x, y, 0xFFFFFFFF);
    }

    /**
     * Try to toggle auto walking when certain key is pressed.
     */
    @SubscribeEvent
    public static void onMouseInput(final InputEvent.MouseButton event) {
        acceptInput();
    }

    /**
     * Try to toggle auto walking when certain key is pressed.
     */
    @SubscribeEvent
    public static void onKeyInput(final InputEvent.Key event) {
        acceptInput();
    }

    /**
     * Try to toggle auto walking when certain key is pressed.
     */
    private static void acceptInput() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        IAutoWalkable player = (IAutoWalkable) mc.player;
        if (mc.options.keyUp.consumeClick()) {
            // abort auto walking when 'W' pressed
            player.stopAutoWalk();
        } else if (AUTO_WALK_KEY.consumeClick()) {
            // toggle auto walking when the key pressed
            player.toggleAutoWalk();
        }
    }

    /**
     * Keep player forward when auto walking.
     */
    @SubscribeEvent
    public static void onMovementInput(final MovementInputUpdateEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && ((IAutoWalkable) mc.player).isAutoWalkEnabled()) {
            Input input = event.getInput();
            input.up = true;
            input.forwardImpulse = ((LocalPlayer) event.getEntity()).isMovingSlowly() ? 0.3F : 1F;
        }
    }

    /**
     * Reset auto walk state when level unloaded
     */
    @SubscribeEvent
    public static void onLevelUnloaded(final LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            ((IAutoWalkable) mc.player).stopAutoWalk();
        }
    }

}
