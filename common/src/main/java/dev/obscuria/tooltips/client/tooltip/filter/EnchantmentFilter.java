package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;
import java.util.Optional;

public record EnchantmentFilter(
        Optional<Boolean> anyEnchantment,
        Optional<Boolean> anyCurse,
        Optional<List<ResourceLocation>> enchantments
) implements ItemFilter {

    public static final MapCodec<EnchantmentFilter> CODEC;

    @Override
    public MapCodec<EnchantmentFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (anyEnchantment.isPresent() && !anyEnchantment.get().equals(stack.isEnchanted())) return false;
        if (anyCurse.isPresent() && !anyCurse.get().equals(isCursed(stack))) return false;
        if (enchantments.isPresent() && !containsAll(stack, enchantments.get())) return false;
        return true;
    }

    private boolean isCursed(ItemStack stack) {
        return stack.getComponents()
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .keySet().stream().anyMatch(it -> it.is(EnchantmentTags.CURSE));
    }

    private boolean containsAll(ItemStack stack, List<ResourceLocation> enchantmentIds) {
        final var enchantments = stack.getComponents().getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        return enchantmentIds.stream().allMatch(id -> enchantments.keySet().stream().anyMatch(it -> it.is(id)));
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.BOOL.optionalFieldOf("any_enchantment").forGetter(EnchantmentFilter::anyEnchantment),
                Codec.BOOL.optionalFieldOf("any_curse").forGetter(EnchantmentFilter::anyCurse),
                ResourceLocation.CODEC.listOf().optionalFieldOf("enchantments").forGetter(EnchantmentFilter::enchantments)
        ).apply(codec, EnchantmentFilter::new));
    }
}
