package dev.obscuria.tooltips.mixin;

import dev.obscuria.tooltips.client.toast.VibrantTooltipsHint;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Overlay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ToastComponent getToasts();

    @Inject(method = "setOverlay", at = @At("RETURN"))
    private void injectVibrantTooltipsHint(@Nullable Overlay overlay, CallbackInfo info) {
        if (overlay != null) return;
        try {
            if (!ClientConfig.SHOW_VIBRANT_TOOLTIPS_HINT.get()) return;
            this.getToasts().addToast(new VibrantTooltipsHint());
            ClientConfig.SHOW_VIBRANT_TOOLTIPS_HINT.set(false);
            ClientConfig.SHOW_VIBRANT_TOOLTIPS_HINT.save();
        } catch (Exception ignored) {}
    }
}
