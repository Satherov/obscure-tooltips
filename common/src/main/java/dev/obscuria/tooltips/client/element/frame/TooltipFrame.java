package dev.obscuria.tooltips.client.element.frame;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.content.registry.TooltipRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipFrame {

    Codec<TooltipFrame> DIRECT_CODEC = TooltipRegistries.TOOLTIP_FRAME_TYPE.byNameCodec().dispatch(TooltipFrame::codec, Function.identity());
    Codec<TooltipFrame> CODEC = TooltipRegistries.Resource.TOOLTIP_FRAME.byNameCodec();

    Codec<? extends TooltipFrame> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void bootstrap(BootstrapContext<Codec<? extends TooltipFrame>> context) {
        context.register("blank", () -> BlankFrame.CODEC);
        context.register("nine_sliced", () -> NineSlicedFrame.CODEC);
    }
}
