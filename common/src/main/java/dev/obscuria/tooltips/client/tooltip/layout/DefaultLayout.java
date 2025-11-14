package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.renderer.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class DefaultLayout implements TooltipLayout<DefaultLayout.State> {

    public static final DefaultLayout INSTANCE = new DefaultLayout();
    public static final Codec<DefaultLayout> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<DefaultLayout> codec() {
        return CODEC;
    }

    @Override
    public State makeTooltipState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void render(
            State state, GuiGraphics graphics, List<ClientTooltipComponent> components,
            int mouseX, int mouseY, ClientTooltipPositioner positioner, Font font) {

        this.makeHeader(state, components, font);

        final var width = state.widthOf(components, font);
        final var height = state.heightOf(components) - 2;
        final var pos = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, width, height);

        graphics.drawManaged(() -> {

            graphics.pose().pushPose();
            graphics.pose().translate(0f, 0f, 400f);
            state.renderPanel(graphics, pos, width, height);
            state.renderEffects(graphics, pos, width, height);

            graphics.pose().pushPose();
            graphics.pose().translate(0f, 0f, 2f);
            state.renderFrame(graphics, pos, width, height);
            graphics.pose().popPose();

            var componentY = pos.y();
            for (var component : components) {
                component.renderText(font, pos.x(), componentY, graphics.pose().last().pose(), graphics.bufferSource());
                component.renderImage(font, pos.x(), componentY, graphics);
                componentY += component.getHeight();
            }

            graphics.pose().popPose();
        });
    }

    private void makeHeader(State state, List<ClientTooltipComponent> components, Font font) {
        final var title = components.remove(0);
        final var label = state.createLabel();
        final var width = state.widthOf(components, font);
        components.add(0, new HeaderComponent(width, !components.isEmpty(), state, title, label));
    }

    public static class State extends TooltipState {

        protected State(ItemStack stack) {
            super(stack);
        }
    }
}
