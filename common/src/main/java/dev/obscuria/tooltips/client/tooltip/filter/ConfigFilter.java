package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.config.BooleanDelegate;
import net.minecraft.world.item.ItemStack;

public record ConfigFilter(BooleanDelegate configValue) implements ItemFilter {

    public static final Codec<ConfigFilter> CODEC;

    @Override
    public Codec<ConfigFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return configValue.value();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BooleanDelegate.CODEC.fieldOf("config_value").forGetter(ConfigFilter::configValue)
        ).apply(codec, ConfigFilter::new));
    }
}
