package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public interface TooltipLayout<T extends TooltipState> {

    Codec<TooltipPanel> DIRECT_CODEC = TooltipRegistries.TOOLTIP_PANEL_TYPE.byNameCodec().dispatch(TooltipPanel::codec, Function.identity());

    Codec<? extends TooltipLayout<?>> codec();

    T extractState(ItemStack stack);

    List<ClientTooltipComponent> processPreWrap(T state, List<ClientTooltipComponent> components, Font font);

    List<ClientTooltipComponent> processPostWrap(T state, List<ClientTooltipComponent> components, Font font);

    default List<ClientTooltipComponent> rawProcessPreWrap(TooltipState state, List<ClientTooltipComponent> components, Font font) {
        return processPreWrap(this.adapt(state), components, font);
    }

    default List<ClientTooltipComponent> rawProcessPostWrap(TooltipState state, List<ClientTooltipComponent> components, Font font) {
        return processPostWrap(this.adapt(state), components, font);
    }

    @SuppressWarnings("unchecked")
    default T adapt(TooltipState state) {
        return (T) state;
    }

    static void bootstrap(BootstrapContext<Codec<? extends TooltipLayout<?>>> context) {

        context.register("default", () -> DefaultLayout.CODEC);
        context.register("armor_preview", () -> ArmorPreviewLayout.CODEC);
        context.register("tool_preview", () -> ToolPreviewLayout.CODEC);
    }
}
