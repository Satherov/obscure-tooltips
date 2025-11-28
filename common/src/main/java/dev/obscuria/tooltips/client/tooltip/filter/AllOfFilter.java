package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AllOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static final MapCodec<AllOfFilter> CODEC;

    @Override
    public MapCodec<AllOfFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().allMatch(it -> it.test(stack));
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                ItemFilter.CODEC.listOf().fieldOf("terms").forGetter(AllOfFilter::terms)
        ).apply(codec, AllOfFilter::new));
    }
}
