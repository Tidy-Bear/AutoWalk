package me.ctidy.mcmod.autowalk.mixin;

import me.ctidy.mcmod.autowalk.api.IAutoWalkable;
import me.ctidy.mcmod.autowalk.config.AutoWalkClientConfig;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * LocalPlayerMixin
 *
 * @author ctidy
 * @since 2023/8/7
 */
@OnlyIn(Dist.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements IAutoWalkable, IForgeEntity {

    private boolean autoWalkEnabled;

    /**
     * Enable auto jump when auto walking without modifying {@link Options#autoJump()}.
     */
    @Inject(at = @At(value = "RETURN"), method = "isAutoJumpEnabled", cancellable = true)
    public void isAutoJumpEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() &&
                AutoWalkClientConfig.INSTANCE.autoJump.get() &&
                isAutoWalkEnabled() &&
                getStepHeight() < 1) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean isAutoWalkEnabled() {
        return autoWalkEnabled;
    }

    @Override
    public void startAutoWalk() {
        if (!autoWalkEnabled) {
            autoWalkEnabled = true;
        }
    }

    @Override
    public void stopAutoWalk() {
        if (autoWalkEnabled) {
            autoWalkEnabled = false;
        }
    }

    @Override
    public void toggleAutoWalk() {
        autoWalkEnabled = !autoWalkEnabled;
    }

}
