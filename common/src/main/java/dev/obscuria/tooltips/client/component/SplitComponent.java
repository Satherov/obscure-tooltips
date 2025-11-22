package dev.obscuria.tooltips.client.component;

import dev.obscuria.tooltips.client.TooltipHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

import java.util.List;

public record SplitComponent(
        ClientTooltipComponent left,
        List<ClientTooltipComponent> right
) implements ClientTooltipComponent {

    @Override
    public int getHeight() {
        return Math.max(left.getHeight(), TooltipHelper.heightOf(right));
    }

    @Override
    public int getWidth(Font font) {
        return left.getWidth(font) + 3 + TooltipHelper.widthOf(right, font);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {

        var componentX = x + left.getWidth(font) + 3;
        var componentY = y;
        for (var component : right) {
            component.renderImage(font, componentX, componentY, graphics);
            componentY += component.getHeight();
        }

        left.renderImage(font, x, y, graphics);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {

        var componentX = x + left.getWidth(font) + 3;
        var componentY = y;
        for (var component : right) {
            component.renderText(font, componentX, componentY, matrix, bufferSource);
            componentY += component.getHeight();
        }

        left.renderText(font, x, y, matrix, bufferSource);
    }
}
