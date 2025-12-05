package dev.obscuria.tooltips.client.component;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

public record HeaderComponent(
        TooltipState state,
        ClientTooltipComponent title,
        ClientTooltipComponent label,
        boolean drawSeparator,
        ARGB separatorColor
) implements ClientTooltipComponent {

    @Override
    public int getHeight() {
        return drawSeparator ? 25 : 22;
    }

    @Override
    public int getWidth(Font font) {
        return 22 + Math.max(title.getWidth(font), label.getWidth(font));
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource source) {
        if (!ClientConfig.LABELS_ENABLED.get() || label instanceof BlankComponent) {
            title.renderText(font, 22 + x, 1 + y + 5, matrix, source);
        } else {
            title.renderText(font, 22 + x, 1 + y, matrix, source);
            label.renderText(font, 22 + x, 1 + y + title.getHeight(), matrix, source);
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        state.style.slot().ifPresent(it -> it.render(graphics, x, y, 20, 20));
        state.style.effects().forEach(it -> it.renderIcon(state, graphics, x + 10, y + 10));
        state.style.icon().ifPresent(it -> it.render(state, graphics, x + 10, y + 10));

        if (!drawSeparator) return;
        final var length = getWidth(font) / 2;
        final var edgeColor = separatorColor.withAlpha(0f);
        GraphicUtils.drawHLine(graphics, x, y + 22, length, edgeColor, separatorColor);
        GraphicUtils.drawHLine(graphics, x + length, y + 22, 1 + length, separatorColor, edgeColor);
    }
}
