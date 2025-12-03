package dev.obscuria.tooltips.client.registry;

import dev.obscuria.fragmentum.registry.Deferred;
import dev.obscuria.tooltips.ObscureTooltips;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public interface TooltipSounds {

    Deferred<SoundEvent, SoundEvent> EFFECT_EPIC_POOF = register("effect.epic_poof");

    private static Deferred<SoundEvent, SoundEvent> register(String name) {
        final var key = ObscureTooltips.key(name);
        return TooltipRegistries.REGISTRAR.register(Registries.SOUND_EVENT, key,
                () -> SoundEvent.createVariableRangeEvent(key));
    }

    static void init() {}
}
