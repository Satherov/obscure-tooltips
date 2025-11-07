package dev.obscuria.tooltips.client.particle;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.renderer.ParticleData;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import dev.obscuria.tooltips.content.registry.TooltipRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipParticle {

    Codec<TooltipParticle> CODEC = TooltipRegistries.TOOLTIP_PARTICLE_TYPE.byNameCodec().dispatch(TooltipParticle::codec, Function.identity());

    Codec<? extends TooltipParticle> codec();

    void render(GuiGraphics graphics, TooltipContext context, ParticleData data);

    static void bootstrap(BootstrapContext<Codec<? extends TooltipParticle>> context) {

        context.register("texture", () -> TextureParticle.CODEC);
        context.register("line", () -> LineParticle.CODEC);
    }
}
