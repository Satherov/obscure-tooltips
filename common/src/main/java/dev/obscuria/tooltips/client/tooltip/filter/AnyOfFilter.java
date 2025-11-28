package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AnyOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static final MapCodec<AnyOfFilter> CODEC;

    @Override
    public MapCodec<AnyOfFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().anyMatch(it -> it.test(stack));
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                ItemFilter.CODEC.listOf().fieldOf("terms").forGetter(AnyOfFilter::terms)
        ).apply(codec, AnyOfFilter::new));
    }
}
