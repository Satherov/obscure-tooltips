package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public record NeverFilter() implements ItemFilter {

    public static final NeverFilter INSTANCE = new NeverFilter();
    public static final MapCodec<NeverFilter> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<NeverFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }
}
