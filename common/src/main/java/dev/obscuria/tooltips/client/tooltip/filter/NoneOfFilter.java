package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record NoneOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static final MapCodec<NoneOfFilter> CODEC;

    @Override
    public MapCodec<NoneOfFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().noneMatch(it -> it.test(stack));
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                ItemFilter.CODEC.listOf().fieldOf("terms").forGetter(NoneOfFilter::terms)
        ).apply(codec, NoneOfFilter::new));
    }
}
