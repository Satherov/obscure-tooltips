package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public record IdentifierFilter(
        TargetType target,
        Optional<String> contains,
        Optional<String> startsWith,
        Optional<String> endsWith
) implements ItemFilter {

    public static final Codec<IdentifierFilter> CODEC;

    @Override
    public Codec<IdentifierFilter> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean test(ItemStack stack) {
        var value = target.resolve(stack.getItem().builtInRegistryHolder().key().location());
        if (contains.isPresent() && !value.contains(contains.get())) return false;
        if (startsWith.isPresent() && !value.startsWith(startsWith.get())) return false;
        if (endsWith.isPresent() && !value.endsWith(endsWith.get())) return false;
        return true;
    }

    public enum TargetType implements StringRepresentable {
        PATH(ResourceLocation::getPath),
        NAMESPACE(ResourceLocation::getNamespace),
        FULL(ResourceLocation::toString);

        public static final Codec<TargetType> CODEC = StringRepresentable.fromEnum(TargetType::values);

        private final Function<ResourceLocation, String> extractor;

        TargetType(Function<ResourceLocation, String> extractor) {
            this.extractor = extractor;
        }

        public String resolve(ResourceLocation identifier) {
            return this.extractor.apply(identifier);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TargetType.CODEC.optionalFieldOf("target", TargetType.PATH).forGetter(IdentifierFilter::target),
                Codec.STRING.optionalFieldOf("contains").forGetter(IdentifierFilter::contains),
                Codec.STRING.optionalFieldOf("starts_with").forGetter(IdentifierFilter::startsWith),
                Codec.STRING.optionalFieldOf("ends_with").forGetter(IdentifierFilter::endsWith)
        ).apply(codec, IdentifierFilter::new));
    }
}
