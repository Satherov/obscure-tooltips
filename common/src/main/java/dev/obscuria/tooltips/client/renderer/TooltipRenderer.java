package dev.obscuria.tooltips.client.renderer;

import dev.obscuria.fragmentum.client.ClientGroupTooltip;
import dev.obscuria.tooltips.client.StackBuffer;
import dev.obscuria.tooltips.client.tooltip.layout.ArmorPreviewLayout;
import dev.obscuria.tooltips.client.tooltip.layout.DefaultLayout;
import dev.obscuria.tooltips.client.tooltip.layout.ToolPreviewLayout;
import dev.obscuria.tooltips.client.tooltip.layout.TooltipLayout;
import dev.obscuria.tooltips.config.TooltipConfig;
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

        if (!TooltipConfig.client.enabled) return false;
        if (components.isEmpty()) return false;
        if (!perform(components)) return false;

        components = new ArrayList<>(components);
        layout.renderRaw(state, graphics, components, mouseX, mouseY, positioner, font);

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
        state = layout.makeTooltipState(actualStack);
        return true;
    }

    private static boolean shouldShowArmorPreview(ItemStack stack) {
        if (!TooltipConfig.client.armorPreviewEnabled) return false;
        if (TooltipConfig.armorPreviewBlacklist.contains(stack.getItem())) return false;
        return stack.getItem() instanceof ArmorItem || TooltipConfig.armorPreviewWhitelist.contains(stack.getItem());
    }

    private static boolean shouldShowToolPreview(ItemStack stack) {
        if (!TooltipConfig.client.toolPreviewEnabled) return false;
        if (TooltipConfig.toolPreviewBlacklist.contains(stack.getItem())) return false;
        return stack.getItem() instanceof TieredItem || TooltipConfig.toolPreviewWhitelist.contains(stack.getItem());
    }

    private static final class EmptyState extends TooltipState {

        private EmptyState() {
            super(ItemStack.EMPTY);
        }
    }
}
