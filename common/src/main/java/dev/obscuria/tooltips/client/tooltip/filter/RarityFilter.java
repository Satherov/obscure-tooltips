package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record RarityFilter(String rarity) implements ItemFilter {

    public static final MapCodec<RarityFilter> CODEC;

    @Override
    public MapCodec<RarityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        final var rarity = stack.getRarity().name().toLowerCase();
        return this.rarity.equals(rarity);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.fieldOf("rarity").forGetter(RarityFilter::rarity)
        ).apply(codec, RarityFilter::new));
    }
}
