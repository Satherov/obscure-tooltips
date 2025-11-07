package dev.obscuria.tooltips.content;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record StackBuffer(ItemStack stack) implements TooltipComponent {}