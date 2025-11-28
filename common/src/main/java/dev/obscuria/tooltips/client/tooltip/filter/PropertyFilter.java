package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record PropertyFilter(Optional<Boolean> hasFoil) implements ItemFilter {

    public static final MapCodec<PropertyFilter> CODEC;

    @Override
    public MapCodec<PropertyFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (hasFoil.isPresent() && !hasFoil.get().equals(stack.hasFoil())) return false;
        return true;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.BOOL.optionalFieldOf("has_foil").forGetter(PropertyFilter::hasFoil)
        ).apply(codec, PropertyFilter::new));
    }
}
