package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.TooltipState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ToolPreviewLayout extends AbstractHeaderLayout<ToolPreviewLayout.State> {

    public static final ToolPreviewLayout INSTANCE = new ToolPreviewLayout();
    public static final Codec<ToolPreviewLayout> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<ToolPreviewLayout> codec() {
        return CODEC;
    }

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<ClientTooltipComponent> processPostWrap(State state, List<ClientTooltipComponent> components, Font font) {
        return components;
    }

    public static class State extends TooltipState {

        protected State(ItemStack stack) {
            super(stack);
        }
    }
}
