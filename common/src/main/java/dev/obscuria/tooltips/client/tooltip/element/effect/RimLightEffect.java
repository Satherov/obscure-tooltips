package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

public record RimLightEffect(
        QuadPalette outerPalette,
        QuadPalette innerPalette
) implements TooltipEffect {

    public static final MapCodec<RimLightEffect> CODEC;

    @Override
    public MapCodec<RimLightEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof RimLightEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {

        final var pX = x - 3f;
        final var pY = y - 3f;
        final var pWidth = width + 6f;
        final var pHeight = height + 6f;

        final var matrix = graphics.pose().last().pose();
        final var buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay());
        final var scale = 0.8f + 0.4f * (float) Math.cos(state.timeInSeconds());
        final var offset = Math.min(pWidth, pHeight) * 0.25f * scale;

        GraphicUtils.color(buffer.addVertex(matrix, pX, pY, 0f), outerPalette.topLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX + offset, pY + offset, 0f), innerPalette.topLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth - offset, pY + offset, 0f), innerPalette.topRight());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth, pY, 0f), outerPalette.topRight());

        GraphicUtils.color(buffer.addVertex(matrix, pX + offset, pY + pHeight - offset, 0f), innerPalette.bottomLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX, pY + pHeight, 0f), outerPalette.bottomLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth, pY + pHeight, 0f), outerPalette.bottomRight());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth - offset, pY + pHeight - offset, 0f), innerPalette.bottomRight());

        GraphicUtils.color(buffer.addVertex(matrix, pX, pY, 0f), outerPalette.topLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX, pY + pHeight, 0f), outerPalette.bottomLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX + offset, pY + pHeight - offset, 0f), innerPalette.bottomLeft());
        GraphicUtils.color(buffer.addVertex(matrix, pX + offset, pY + offset, 0f), innerPalette.topLeft());

        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth - offset, pY + offset, 0f), innerPalette.topRight());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth - offset, pY + pHeight - offset, 0f), innerPalette.bottomRight());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth, pY + pHeight, 0f), outerPalette.bottomRight());
        GraphicUtils.color(buffer.addVertex(matrix, pX + pWidth, pY, 0f), outerPalette.topRight());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                QuadPalette.CODEC.fieldOf("outer_palette").forGetter(RimLightEffect::outerPalette),
                QuadPalette.CODEC.fieldOf("inner_palette").forGetter(RimLightEffect::innerPalette)
        ).apply(codec, RimLightEffect::new));
    }
}
