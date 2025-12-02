package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;

public record ColorRectPanel(
        QuadPalette background,
        QuadPalette border
) implements TooltipPanel {

    public static final Codec<ColorRectPanel> CODEC;

    @Override
    public Codec<ColorRectPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        dropShadow(graphics, x - 3, y - 3, width + 6, height + 6);
        GraphicUtils.drawRect(graphics, x - 3, y - 3, width + 6, height + 6, background);
        GraphicUtils.drawFrame(graphics, x - 3, y - 3, width + 6, height + 6, border);
        GraphicUtils.drawHLine(graphics, x - 3, y - 4, width + 6, background.topLeft(), background.topRight());
        GraphicUtils.drawHLine(graphics, x - 3, y + height + 3, width + 6, background.bottomLeft(), background.bottomRight());
        GraphicUtils.drawVLine(graphics, x - 4, y - 3, height + 6, background.topLeft(), background.bottomLeft());
        GraphicUtils.drawVLine(graphics, x + width + 3, y - 3, height + 6, background.topRight(), background.bottomRight());
    }

    private void dropShadow(GuiGraphics graphics, int x, int y, int width, int height) {
        if (!ClientConfig.SHADOWS_ENABLED.get()) return;
        final var color = (int) (Math.round(ClientConfig.SHADOW_OPACITY.get() * 255) << 24);
        graphics.hLine(x + 2, x + width, y + height + 1, color);
        graphics.hLine(x + 3, x + width + 1, y + height + 2, color);
        graphics.vLine(x + width + 1, y + 1, y + height + 1, color);
        graphics.vLine(x + width + 2, y + 2, y + height + 2, color);
        graphics.hLine(x + width, x + width, y + height, color);
        graphics.hLine(x + width + 1, x + width + 1, y + height + 1, color);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                QuadPalette.CODEC.fieldOf("background_palette").forGetter(ColorRectPanel::background),
                QuadPalette.CODEC.fieldOf("border_palette").forGetter(ColorRectPanel::border)
        ).apply(codec, ColorRectPanel::new));
    }
}
