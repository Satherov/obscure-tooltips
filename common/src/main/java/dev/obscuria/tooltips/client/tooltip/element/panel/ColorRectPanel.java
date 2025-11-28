package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import net.minecraft.client.gui.GuiGraphics;

public record ColorRectPanel(
        QuadPalette background,
        QuadPalette border
) implements TooltipPanel {

    public static final MapCodec<ColorRectPanel> CODEC;

    @Override
    public MapCodec<ColorRectPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        GraphicUtils.drawRect(graphics, x - 3, y - 3, width + 6, height + 6, background);
        GraphicUtils.drawFrame(graphics, x - 3, y - 3, width + 6, height + 6, border);
        GraphicUtils.drawHLine(graphics, x - 3, y - 4, width + 6, background.topLeft(), background.topRight());
        GraphicUtils.drawHLine(graphics, x - 3, y + height + 3, width + 6, background.bottomLeft(), background.bottomRight());
        GraphicUtils.drawVLine(graphics, x - 4, y - 3, height + 6, background.topLeft(), background.bottomLeft());
        GraphicUtils.drawVLine(graphics, x + width + 3, y - 3, height + 6, background.topRight(), background.bottomRight());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                QuadPalette.CODEC.fieldOf("background_palette").forGetter(ColorRectPanel::background),
                QuadPalette.CODEC.fieldOf("border_palette").forGetter(ColorRectPanel::border)
        ).apply(codec, ColorRectPanel::new));
    }
}
