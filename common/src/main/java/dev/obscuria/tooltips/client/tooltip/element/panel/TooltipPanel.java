package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipPanel {

    Codec<TooltipPanel> DIRECT_CODEC = TooltipRegistries.TOOLTIP_PANEL_TYPE.byNameCodec().dispatch(TooltipPanel::codec, Function.identity());
    Codec<TooltipPanel> CODEC = TooltipRegistries.Resource.TOOLTIP_PANEL.byNameCodec();
    ARGB DEFAULT_SEPARATOR_COLOR = Colors.argbOf(0x60ffffff);

    Codec<? extends TooltipPanel> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    default ARGB separatorColor() {
        return DEFAULT_SEPARATOR_COLOR;
    }

    static void bootstrap(BootstrapContext<Codec<? extends TooltipPanel>> context) {
        context.register("blank", () -> BlankPanel.CODEC);
        context.register("color_rect", () -> ColorRectPanel.CODEC);
    }
}
