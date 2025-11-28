package dev.obscuria.tooltips.mixin;

import dev.obscuria.tooltips.client.tooltip.TooltipScroll;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MouseHandler.class, priority = 1)
public abstract class MixinMouseHandler {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void customScroll(long windowPointer, double xOffset, double yOffset, CallbackInfo info) {
        if (!TooltipScroll.shouldCaptureInput()) return;
        TooltipScroll.onInput((float) yOffset);
        info.cancel();
    }
}
