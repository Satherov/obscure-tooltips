package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ModFilter(List<String> mods) implements ItemFilter {

    public static final MapCodec<ModFilter> CODEC;

    @Override
    public MapCodec<ModFilter> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean test(ItemStack stack) {
        return mods.contains(stack.getItem().builtInRegistryHolder().key().location().getNamespace());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.listOf().fieldOf("mods").forGetter(ModFilter::mods)
        ).apply(codec, ModFilter::new));
    }
}
