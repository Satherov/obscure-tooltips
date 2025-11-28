package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public record AlwaysFilter() implements ItemFilter {

    public static final AlwaysFilter INSTANCE = new AlwaysFilter();
    public static final MapCodec<AlwaysFilter> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<AlwaysFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return true;
    }
}
