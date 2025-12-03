package dev.obscuria.tooltips.client.tooltip.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record SoundTemplate(
        ResourceLocation value,
        float volume,
        float pitch
) {

    public static final Codec<SoundTemplate> CODEC;

    public void play() {
        final @Nullable var sound = BuiltInRegistries.SOUND_EVENT.get(value);
        if (sound != null) {
            final var volume = this.volume * ClientConfig.SOUND_VOLUME.get().floatValue();
            final var instance = SimpleSoundInstance.forUI(sound, pitch, volume);
            Minecraft.getInstance().getSoundManager().play(instance);
        } else {
            ObscureTooltips.LOGGER.warn("Failed to play unknown sound event {}", value);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("value").forGetter(SoundTemplate::value),
                Codec.FLOAT.optionalFieldOf("volume", 1f).forGetter(SoundTemplate::volume),
                Codec.FLOAT.optionalFieldOf("pitch", 1f).forGetter(SoundTemplate::pitch)
        ).apply(codec, SoundTemplate::new));
    }
}
