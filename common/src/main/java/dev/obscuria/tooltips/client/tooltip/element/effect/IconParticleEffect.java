package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.tooltips.client.renderer.TooltipState;
import dev.obscuria.tooltips.client.tooltip.particle.TooltipParticle;
import dev.obscuria.tooltips.client.renderer.ParticleData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public record IconParticleEffect(TooltipParticle particle) implements TooltipEffect {

    public static final Codec<IconParticleEffect> CODEC;

    @Override
    public Codec<IconParticleEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof IconParticleEffect);
    }

    @Override
    public void renderIcon(TooltipState state, GuiGraphics graphics, int x, int y) {

        var lastParticleTime = 0f;
        for (var particle : state.particles) {
            if (!particle.source.equals(this)) continue;
            particle.render(graphics, state, this.particle, x, y);
            lastParticleTime = Math.max(lastParticleTime, particle.startTime);
        }

        if (state.timeInSeconds() - lastParticleTime < 0.15f) return;
        final var origin = Vec2.ZERO;
        final var direction = RandomUtils.nextFloat(0f, (float) Math.PI * 2f);
        final var destination = new Vec2(12f * (float) Math.sin(direction), 12f * (float) Math.cos(direction));
        state.addParticle(new IconParticle(this, state.timeInSeconds(), origin, destination));
    }

    private static final class IconParticle extends ParticleData {

        public IconParticle(Object source, float startTime, Vec2 origin, Vec2 destination) {
            super(source, startTime, origin, destination);
        }

        @Override
        public float computeProgress(TooltipState state) {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(state) * 0.5f);
        }

        @Override
        public float computeScale(TooltipState state, float progress) {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.2f);
        }

        @Override
        public float computeRotation(TooltipState state, float progress) {
            return progress * 2f;
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TooltipParticle.CODEC.fieldOf("particle").forGetter(IconParticleEffect::particle)
        ).apply(codec, IconParticleEffect::new));
    }
}
