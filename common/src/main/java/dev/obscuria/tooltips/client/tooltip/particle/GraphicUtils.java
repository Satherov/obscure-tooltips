package dev.obscuria.tooltips.client.tooltip.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.color.Colors;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public interface GraphicUtils {

    ARGB WHITE = Colors.argbOf(0xffffffff);

    static void setShaderColor(ARGB color) {
        RenderSystem.setShaderColor(color.red(), color.green(), color.blue(), color.alpha());
    }

    static void resetShaderColor() {
        setShaderColor(WHITE);
    }

    static void drawFrame(GuiGraphics graphics, int x, int y, int width, int height, QuadPalette palette) {
        drawFrame(graphics, x, y, width, height, palette.topLeft(), palette.topRight(), palette.bottomLeft(), palette.bottomRight());
    }

    static void drawFrame(GuiGraphics graphics, int x, int y, int width, int height, ARGB topLeft, ARGB topRight, ARGB bottomLeft, ARGB bottomRight) {
        drawHLine(graphics, x, y, width, topLeft, topRight);
        drawHLine(graphics, x, y + height - 1, width, bottomLeft, bottomRight);
        drawVLine(graphics, x, y + 1, height - 2, topLeft, bottomLeft);
        drawVLine(graphics, x + width - 1, y + 1, height - 2, topRight, bottomRight);
    }

    static void drawHLine(GuiGraphics graphics, int x, int y, int length, ARGB start, ARGB end) {
        drawRect(graphics, RenderType.gui(), x, y, length, 1, start, end, start, end);
    }

    static void drawVLine(GuiGraphics graphics, int x, int y, int length, ARGB start, ARGB end) {
        drawRect(graphics, RenderType.gui(), x, y, 1, length, start, start, end, end);
    }

    static void drawHLineOverlay(GuiGraphics graphics, int x, int y, int length, ARGB start, ARGB end) {
        drawRect(graphics, RenderType.guiOverlay(), x, y, length, 1, start, end, start, end);
    }

    static void drawVLineOverlay(GuiGraphics graphics, int x, int y, int length, ARGB start, ARGB end) {
        drawRect(graphics, RenderType.guiOverlay(), x, y, 1, length, start, start, end, end);
    }

    static void drawRect(GuiGraphics graphics, int x, int y, int width, int height, QuadPalette palette) {
        drawRect(graphics, RenderType.gui(), x, y, width, height, palette.topLeft(), palette.topRight(), palette.bottomLeft(), palette.bottomRight());
    }

    static void drawRect(GuiGraphics graphics, RenderType type, int x, int y, int width, int height, ARGB topLeft, ARGB topRight, ARGB bottomLeft, ARGB bottomRight) {

        final var consumer = graphics.bufferSource().getBuffer(type);
        final var matrix = graphics.pose().last().pose();
        final var minX = (float) x;
        final var minY = (float) y;
        final var maxX = (float) (x + width);
        final var maxY = (float) (y + height);

        color(consumer.addVertex(matrix, minX, minY, 0f), topLeft);
        color(consumer.addVertex(matrix, minX, maxY, 0f), bottomLeft);
        color(consumer.addVertex(matrix, maxX, maxY, 0f), bottomRight);
        color(consumer.addVertex(matrix, maxX, minY, 0f), topRight);
    }

    static VertexConsumer color(VertexConsumer consumer, ARGB color) {
        return consumer.setColor(color.red(), color.green(), color.blue(), color.alpha());
    }
}
