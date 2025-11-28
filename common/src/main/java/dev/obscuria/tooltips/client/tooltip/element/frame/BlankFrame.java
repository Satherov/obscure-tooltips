package dev.obscuria.tooltips.client.tooltip.element.frame;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankFrame() implements TooltipFrame {

    public static final BlankFrame INSTANCE = new BlankFrame();
    public static final MapCodec<BlankFrame> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<BlankFrame> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
