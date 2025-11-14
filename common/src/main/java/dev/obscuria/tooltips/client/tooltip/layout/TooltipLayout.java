package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.client.renderer.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public interface TooltipLayout<T extends TooltipState> {

    Codec<TooltipPanel> DIRECT_CODEC = TooltipRegistries.TOOLTIP_PANEL_TYPE.byNameCodec().dispatch(TooltipPanel::codec, Function.identity());

    Codec<? extends TooltipLayout<?>> codec();

    T makeTooltipState(ItemStack stack);

    void render(T state, GuiGraphics graphics, List<ClientTooltipComponent> components,
                int mouseX, int mouseY, ClientTooltipPositioner positioner, Font font);

    default void renderRaw(
            TooltipState state, GuiGraphics graphics, List<ClientTooltipComponent> components,
            int mouseX, int mouseY, ClientTooltipPositioner positioner, Font font) {
        this.render(this.adapt(state), graphics, components, mouseX, mouseY, positioner, font);
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
