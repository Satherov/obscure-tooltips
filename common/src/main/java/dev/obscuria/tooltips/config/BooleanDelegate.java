package dev.obscuria.tooltips.config;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public enum BooleanDelegate implements StringRepresentable {
    UNCOMMON_STYLE_ENABLED(ClientConfig.UNCOMMON_STYLE_ENABLED::get),
    RARE_STYLE_ENABLED(ClientConfig.RARE_STYLE_ENABLED::get),
    EPIC_STYLE_ENABLED(ClientConfig.EPIC_STYLE_ENABLED::get),
    ENCHANTED_STYLE_ENABLED(ClientConfig.ENCHANTED_STYLE_ENABLED::get),
    CURSED_STYLE_ENABLED(ClientConfig.CURSED_STYLE_ENABLED::get);

    public static final Codec<BooleanDelegate> CODEC = StringRepresentable.fromEnum(BooleanDelegate::values);

    private final Supplier<Boolean> valueProvider;

    BooleanDelegate(Supplier<Boolean> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public boolean value() {
        return valueProvider.get();
    }

    @Override
    public String getSerializedName() {
        return "@" + this;
    }
}
