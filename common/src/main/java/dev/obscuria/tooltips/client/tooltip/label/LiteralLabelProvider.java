package dev.obscuria.tooltips.client.tooltip.label;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record LiteralLabelProvider(String text) implements LabelProvider {

    public static final MapCodec<LiteralLabelProvider> CODEC;

    @Override
    public MapCodec<LiteralLabelProvider> codec() {
        return CODEC;
    }

    @Override
    public ClientTooltipComponent create(ItemStack stack) {
        final var component = Component.literal(text).withStyle(ChatFormatting.GRAY);
        return ClientTooltipComponent.create(component.getVisualOrderText());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.fieldOf("text").forGetter(LiteralLabelProvider::text)
        ).apply(codec, LiteralLabelProvider::new));
    }
}
