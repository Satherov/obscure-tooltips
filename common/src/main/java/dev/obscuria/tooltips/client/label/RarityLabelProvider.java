package dev.obscuria.tooltips.client.label;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public record RarityLabelProvider() implements LabelProvider {

    public static final RarityLabelProvider INSTANCE = new RarityLabelProvider();
    public static final Codec<RarityLabelProvider> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<RarityLabelProvider> codec() {
        return CODEC;
    }

    @Override
    public ClientTooltipComponent create(ItemStack stack) {
        final var key = makeRarityKey(stack.getRarity());
        final var component = Component.translatable(key).withStyle(ChatFormatting.GRAY);
        return ClientTooltipComponent.create(component.getVisualOrderText());
    }

    private String makeRarityKey(Rarity rarity) {
        final var name = rarity.name().toLowerCase().replaceAll(":", ".");
        final var shortKey = "rarity." + name;
        if (I18n.exists(shortKey)) return shortKey;
        return "rarity." + name + ".name";
    }
}
