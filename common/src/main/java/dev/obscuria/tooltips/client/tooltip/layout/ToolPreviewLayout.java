package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.renderer.HeaderComponent;
import dev.obscuria.tooltips.client.renderer.TooltipState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ToolPreviewLayout implements TooltipLayout<ToolPreviewLayout.State> {

    public static final ToolPreviewLayout INSTANCE = new ToolPreviewLayout();
    public static final Codec<ToolPreviewLayout> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<ToolPreviewLayout> codec() {
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

        this.makeHeader(components, state, font);

        final var width = 36 + state.widthOf(components, font);
        final var height = Math.max(60, state.heightOf(components) - 2);
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
                component.renderText(font, 36 + pos.x(), componentY, graphics.pose().last().pose(), graphics.bufferSource());
                component.renderImage(font, 36 + pos.x(), componentY, graphics);
                componentY += component.getHeight();
            }

            graphics.pose().popPose();
        });

        graphics.drawManaged(() -> {
            graphics.pose().pushPose();
            graphics.pose().translate(pos.x() + 18, pos.y() + 30, 500);
            graphics.pose().scale(2.75f, 2.75f, 2.75f);
            graphics.pose().mulPose(Axis.XP.rotationDegrees(-30));
            graphics.pose().mulPose(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * -20f));
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(-45));
            graphics.pose().pushPose();
            graphics.pose().translate(-8, -8, -150);
            graphics.renderItem(state.stack, 0, 0);
            graphics.pose().popPose();
            graphics.pose().popPose();
        });
    }

    private void makeHeader(List<ClientTooltipComponent> components, State state, Font font) {
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
