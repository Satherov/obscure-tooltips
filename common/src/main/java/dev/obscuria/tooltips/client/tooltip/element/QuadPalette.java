package dev.obscuria.tooltips.client.tooltip.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.config.ARGBProvider;

public record QuadPalette(
        ARGBProvider topLeft,
        ARGBProvider topRight,
        ARGBProvider bottomLeft,
        ARGBProvider bottomRight) {

    public static final Codec<QuadPalette> CODEC = RecordCodecBuilder.create(codec -> codec.group(
            ARGBProvider.CODEC.fieldOf("top_left").forGetter(QuadPalette::topLeft),
            ARGBProvider.CODEC.fieldOf("top_right").forGetter(QuadPalette::topRight),
            ARGBProvider.CODEC.fieldOf("bottom_left").forGetter(QuadPalette::bottomLeft),
            ARGBProvider.CODEC.fieldOf("bottom_right").forGetter(QuadPalette::bottomRight)
    ).apply(codec, QuadPalette::new));
}
