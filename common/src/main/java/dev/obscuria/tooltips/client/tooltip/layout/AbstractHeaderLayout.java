package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractHeaderLayout<T extends TooltipState> implements TooltipLayout<T> {

    @Override
    public List<ClientTooltipComponent> processPreWrap(T state, List<ClientTooltipComponent> components, Font font) {
        return this.makeHeader(state, components, font);
    }

    protected List<ClientTooltipComponent> makeHeader(T state, List<ClientTooltipComponent> components, Font font) {
        final @Nullable var title = extractFirstText(components);
        if (title == null) return components;
        final var label = state.createLabel();
        components.add(0, new HeaderComponent(state, title, label,
                shouldDrawSeparator(components),
                pickSeparatorColor(state)));
        return components;
    }

    private @Nullable ClientTooltipComponent extractFirstText(List<ClientTooltipComponent> components) {
        for (var i = 0; i < components.size(); i++) {
            if (!(components.get(i) instanceof ClientTextTooltip)) continue;
            return components.remove(i);
        }
        return null;
    }

    private boolean isZeroHeight(List<ClientTooltipComponent> components) {
        return components.stream().mapToInt(ClientTooltipComponent::getHeight).sum() <= 0;
    }

    private boolean shouldDrawSeparator(List<ClientTooltipComponent> components) {
        return !components.isEmpty() && !isZeroHeight(components);
    }

    private ARGB pickSeparatorColor(T state) {
        return state.style.panel()
                .map(TooltipPanel::separatorColor)
                .orElse(TooltipPanel.DEFAULT_SEPARATOR_COLOR);
    }
}
