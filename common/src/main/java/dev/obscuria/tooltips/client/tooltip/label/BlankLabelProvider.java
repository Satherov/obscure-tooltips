package dev.obscuria.tooltips.client.tooltip.label;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.component.BlankComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public record BlankLabelProvider() implements LabelProvider {

    public static final Codec<BlankLabelProvider> CODEC = Codec.unit(new BlankLabelProvider());

    @Override
    public Codec<BlankLabelProvider> codec() {
        return CODEC;
    }

    @Override
    public ClientTooltipComponent create(ItemStack stack) {
        return BlankComponent.INSTANCE;
    }
}
