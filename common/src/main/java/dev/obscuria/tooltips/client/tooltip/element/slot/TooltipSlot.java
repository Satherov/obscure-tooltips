package dev.obscuria.tooltips.client.tooltip.element.slot;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipSlot {

    Codec<TooltipSlot> DIRECT_CODEC = TooltipRegistries.TOOLTIP_SLOT_TYPE.byNameCodec().dispatch(TooltipSlot::codec, Function.identity());
    Codec<TooltipSlot> CODEC = TooltipRegistries.Resource.TOOLTIP_SLOT.byNameCodec();

    Codec<? extends TooltipSlot> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void bootstrap(BootstrapContext<Codec<? extends TooltipSlot>> context) {

        context.register("blank", () -> BlankSlot.CODEC);
        context.register("color_rect", () -> ColorRectSlot.CODEC);
    }
}
