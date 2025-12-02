package dev.obscuria.tooltips.client;

import dev.obscuria.fragmentum.client.ClientGroupTooltip;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.client.tooltip.TooltipScroll;
import dev.obscuria.tooltips.client.tooltip.layout.ArmorPreviewLayout;
import dev.obscuria.tooltips.client.tooltip.layout.DefaultLayout;
import dev.obscuria.tooltips.client.tooltip.layout.ToolPreviewLayout;
import dev.obscuria.tooltips.client.tooltip.layout.TooltipLayout;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TooltipRenderer {

    private static ItemStack lastStack = ItemStack.EMPTY;
    private static ItemStack actualStack = ItemStack.EMPTY;
    private static TooltipLayout<?> layout = new DefaultLayout();
    private static TooltipState state = new EmptyState();

    public static boolean render(
            GuiGraphics graphics, Font font, List<ClientTooltipComponent> components,
            int mouseX, int mouseY, ClientTooltipPositioner positioner) {

        if (!ClientConfig.ENABLED.get()) return false;
        if (components.isEmpty()) return false;
        if (!perform(components)) return false;

        components = new ArrayList<>(components);
        components = layout.rawProcessPreWrap(state, components, font);
        components = TooltipHelper.wrapLines(graphics, components, font);
        components = layout.rawProcessPostWrap(state, components, font);

        final var margin = ClientConfig.CONTENT_MARGIN.get();
        final var width = margin * 2 + TooltipHelper.widthOf(components, font);
        final var height = margin * 2 + TooltipHelper.heightOf(components) - 2;
        final var pos = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, width, height);

        TooltipScroll.update(state, 6 + height, graphics.guiHeight());

        graphics.pose().pushPose();
        graphics.pose().translate(0f, TooltipScroll.getScroll(), 400f);

        graphics.flush();
        state.renderPanel(graphics, pos, width, height);
        state.renderEffects(graphics, pos, width, height);
        graphics.flush();
        graphics.pose().pushPose();
        graphics.pose().translate(0f, 0f, 2f);
        state.renderFrame(graphics, pos, width, height);
        graphics.pose().popPose();
        graphics.flush();

        var componentX = margin + pos.x();
        var componentY = margin + pos.y();
        for (var component : components) {
            component.renderText(font, componentX, componentY, graphics.pose().last().pose(), graphics.bufferSource());
            component.renderImage(font, componentX, componentY, graphics);
            componentY += component.getHeight();
        }

        graphics.pose().popPose();

        lastStack = actualStack;
        actualStack = ItemStack.EMPTY;
        state.removeExpiredParticles();
        return true;
    }

    private static boolean perform(List<ClientTooltipComponent> components) {
        final @Nullable var buffer = ClientGroupTooltip.findFirst(components, StackBuffer.class);
        if (buffer == null) return false;
        actualStack = buffer.stack();
        if (ItemStack.isSameItemSameTags(lastStack, actualStack)) return true;
        layout = shouldShowArmorPreview(actualStack) ? ArmorPreviewLayout.INSTANCE
                : shouldShowToolPreview(actualStack) ? ToolPreviewLayout.INSTANCE
                : DefaultLayout.INSTANCE;
        state = layout.extractState(actualStack);
        return true;
    }

    private static boolean shouldShowArmorPreview(ItemStack stack) {
        if (!ClientConfig.ARMOR_PREVIEW_ENABLED.get()) return false;
        if (ClientConfig.isInArmorPreviewBlacklist(stack.getItem())) return false;
        return stack.getItem() instanceof ArmorItem || ClientConfig.isInArmorPreviewWhitelist(stack.getItem());
    }

    private static boolean shouldShowToolPreview(ItemStack stack) {
        if (!ClientConfig.TOOL_PREVIEW_ENABLED.get()) return false;
        if (ClientConfig.isInToolPreviewBlacklist(stack.getItem())) return false;
        return stack.getItem() instanceof TieredItem || ClientConfig.isInToolPreviewWhitelist(stack.getItem());
    }

    private static final class EmptyState extends TooltipState {

        private EmptyState() {
            super(ItemStack.EMPTY);
        }
    }
}
