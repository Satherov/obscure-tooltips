package dev.obscuria.tooltips.client;

import dev.obscuria.tooltips.content.StackBuffer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ClientStackBuffer(ItemStack stack) implements ClientTooltipComponent {

    public static ClientStackBuffer create(StackBuffer tooltip) {
        return new ClientStackBuffer(tooltip.stack());
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(Font font) {
        return 0;
    }
}
