package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.TooltipState;
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
        final var drawDelimiter = !components.isEmpty() && !isZeroHeight(components);
        components.add(0, new HeaderComponent(drawDelimiter, state, title, label));
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
}
