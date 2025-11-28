package dev.obscuria.tooltips.client.tooltip.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.tooltips.client.ParticleData;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.client.TooltipState;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipParticle {

    Codec<TooltipParticle> CODEC = TooltipRegistries.TOOLTIP_PARTICLE_TYPE.byNameCodec().dispatch(TooltipParticle::codec, Function.identity());

    MapCodec<? extends TooltipParticle> codec();

    void render(GuiGraphics graphics, TooltipState state, ParticleData data);

    static void bootstrap(BootstrapContext<MapCodec<? extends TooltipParticle>> context) {
        context.register("texture", () -> TextureParticle.CODEC);
        context.register("line", () -> LineParticle.CODEC);
    }
}
