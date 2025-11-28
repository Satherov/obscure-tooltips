package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.MapCodec;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.SplitComponent;
import dev.obscuria.tooltips.client.component.ToolPreviewComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ToolPreviewLayout extends AbstractHeaderLayout<ToolPreviewLayout.State> {

    public static final ToolPreviewLayout INSTANCE = new ToolPreviewLayout();
    public static final MapCodec<ToolPreviewLayout> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ToolPreviewLayout> codec() {
        return CODEC;
    }

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<ClientTooltipComponent> processPostWrap(State state, List<ClientTooltipComponent> components, Font font) {
        return List.of(new SplitComponent(new ToolPreviewComponent(state.stack), components));
    }

    public static class State extends TooltipState {

        protected State(ItemStack stack) {
            super(stack);
        }
    }
}
