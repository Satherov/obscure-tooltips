package dev.obscuria.tooltips.client.tooltip.element.slot;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankSlot() implements TooltipSlot {

    public static final BlankSlot INSTANCE = new BlankSlot();
    public static final MapCodec<BlankSlot> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<BlankSlot> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
