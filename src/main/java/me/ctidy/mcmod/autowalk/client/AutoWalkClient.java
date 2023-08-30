package me.ctidy.mcmod.autowalk.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import me.ctidy.mcmod.autowalk.api.IAutoWalkable;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class AutoWalkClient {

    private static AutoWalkClient instance;

    public static final KeyMapping AUTO_WALK_KEY =
            new KeyMapping("key.autowalk.autowalk", InputConstants.UNKNOWN.getValue(), KeyMapping.CATEGORY_MOVEMENT);

    public static AutoWalkClient instance() {
        if (instance == null) {
            instance = new AutoWalkClient();
        }
        return instance;
    }

    private int cyclicTickInGame;

    private final Minecraft mc = Minecraft.getInstance();

    private AutoWalkClient() { }

    /**
     * Calc tick in a cycle of 20.
     */
    public void tick() {
        Screen gui = Minecraft.getInstance().screen;
        if (gui == null || !gui.isPauseScreen()) {
            cyclicTickInGame += 1;
            cyclicTickInGame %= 20;
        }
    }

    /**
     * Draw message horizontally centered and vertically up on the screen when auto walking.
     */
    public void drawHud(Window window, PoseStack poseStack) {
        if (mc.player == null ||
                !((IAutoWalkable) mc.player).isAutoWalkEnabled()) return;

        String message = I18n.get("gui.autowalk.title");
        String displayMessage = cyclicTickInGame < 10 ? "OoO " + message + " oOo" : "oOo " + message + " OoO";

        int w = mc.font.width(displayMessage);

        int x = (window.getGuiScaledWidth() - w) / 2;
        int y = AutoWalkClientConfig.INSTANCE.hudOffsetY.get();

        mc.font.drawShadow(poseStack, displayMessage, x, y, 0xFFFFFFFF);
    }

    /**
     * Try to toggle auto walking when has input.
     */
    public boolean acceptInput() {
        if (mc.player == null) return false;
        IAutoWalkable player = (IAutoWalkable) mc.player;
        if (mc.options.keyUp.consumeClick()) {
            // abort auto walking when 'W' pressed
            player.stopAutoWalk();
            return true;
        } else if (AUTO_WALK_KEY.consumeClick()) {
            // toggle auto walking when the key pressed
            player.toggleAutoWalk();
            return true;
        }
        return false;
    }

    /**
     * Keep player forward when auto walking.
     */
    public void autoForward(LocalPlayer player) {
        if (mc.player == null || player != mc.player) return;
        ((IAutoWalkable) player).autoForward();
    }


}
