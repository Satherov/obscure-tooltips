package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ArmorPreviewLayout implements TooltipLayout<ArmorPreviewLayout.State> {

    public static final ArmorPreviewLayout INSTANCE = new ArmorPreviewLayout();
    public static final Codec<ArmorPreviewLayout> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<ArmorPreviewLayout> codec() {
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

        final var width = 40 + state.widthOf(components, font);
        final var height = Math.max(64, state.heightOf(components) - 2);
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
                component.renderText(font, 40 + pos.x(), componentY, graphics.pose().last().pose(), graphics.bufferSource());
                component.renderImage(font, 40 + pos.x(), componentY, graphics);
                componentY += component.getHeight();
            }

            graphics.pose().popPose();
        });

        if (state.armorStand == null) return;

        graphics.drawManaged(() -> {
            graphics.pose().translate(pos.x() + 18, pos.y() + 57, 500f);
            graphics.pose().scale(-30, -30, 30);
            graphics.pose().mulPose(Axis.XP.rotationDegrees(25));
            graphics.pose().mulPose(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * 20f));
            Lighting.setupForEntityInInventory();
            final var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            dispatcher.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> dispatcher.render(state.armorStand, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, graphics.pose(), graphics.bufferSource(), 15728880));
            dispatcher.setRenderShadow(true);
            Lighting.setupFor3DItems();
        });
    }

    private void makeHeader(List<ClientTooltipComponent> components, State state, Font font) {
        final var title = components.remove(0);
        final var label = state.createLabel();
        final var width = state.widthOf(components, font);
        components.add(0, new HeaderComponent(width, !components.isEmpty(), state, title, label));
    }

    public static class State extends TooltipState {

        public final @Nullable ArmorStand armorStand;

        protected State(ItemStack stack) {
            super(stack);
            armorStand = makeArmorStand(stack);
        }

        private @Nullable ArmorStand makeArmorStand(ItemStack stack) {
            if (Minecraft.getInstance().level == null) return null;
            final var armorStand = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
            if (!(stack.getItem() instanceof ArmorItem armorItem)) return armorStand;
            armorStand.setItemSlot(armorItem.getEquipmentSlot(), stack);
            return armorStand;
        }
    }
}
