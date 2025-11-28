package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.TooltipState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public abstract class AbstractHeaderLayout<T extends TooltipState> implements TooltipLayout<T> {

    @Override
    public List<ClientTooltipComponent> processPreWrap(T state, List<ClientTooltipComponent> components, Font font) {
        this.makeHeader(state, components, font);
        return components;
    }

    protected void makeHeader(T state, List<ClientTooltipComponent> components, Font font) {
        final var title = components.removeFirst();
        final var label = state.createLabel();
        final var drawDelimiter = !components.isEmpty() && !isZeroHeight(components);
        components.addFirst(new HeaderComponent(drawDelimiter, state, title, label));
    }

    private boolean isZeroHeight(List<ClientTooltipComponent> components) {
        return components.stream().mapToInt(ClientTooltipComponent::getHeight).sum() <= 0;
    }
}
