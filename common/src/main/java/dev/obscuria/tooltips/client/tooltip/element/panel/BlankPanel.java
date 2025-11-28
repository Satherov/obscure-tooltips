package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankPanel() implements TooltipPanel {

    public static final BlankPanel INSTANCE = new BlankPanel();
    public static final MapCodec<BlankPanel> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<BlankPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
