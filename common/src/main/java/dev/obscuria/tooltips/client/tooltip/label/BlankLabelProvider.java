package dev.obscuria.tooltips.client.tooltip.label;

import com.mojang.serialization.MapCodec;
import dev.obscuria.tooltips.client.component.BlankComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public record BlankLabelProvider() implements LabelProvider {

    public static final MapCodec<BlankLabelProvider> CODEC = MapCodec.unit(new BlankLabelProvider());

    @Override
    public MapCodec<BlankLabelProvider> codec() {
        return CODEC;
    }

    @Override
    public ClientTooltipComponent create(ItemStack stack) {
        return BlankComponent.INSTANCE;
    }
}
