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

public record InwardParticleEffect(TooltipParticle particle) implements TooltipEffect {

    public static final Codec<InwardParticleEffect> CODEC;

    @Override
    public Codec<InwardParticleEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof InwardParticleEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {

        var lastParticleTime = 0f;
        for (var particle : state.particles) {
            if (!particle.source.equals(this)) continue;
            particle.render(graphics, state, this.particle, x, y);
            lastParticleTime = Math.max(lastParticleTime, particle.startTime);
        }

        if (state.timeInSeconds() - lastParticleTime < 0.2f) return;

        final var edge = RandomUtils.nextInt(1, 5);
        final var ratio = RandomUtils.nextFloat(0f, 1f);
        final var center = new Vec2(width * 0.5f, height * 0.5f);
        final var origin = switch (edge) {
            case 1 -> new Vec2(width * ratio, 0f);
            case 2 -> new Vec2(width * ratio, (float) height);
            case 3 -> new Vec2(0f, height * ratio);
            case 4 -> new Vec2((float) width, height * ratio);
            default -> Vec2.ZERO;
        };

        state.addParticle(new InwardParticle(this, state.timeInSeconds(), origin, center));
    }

    private static final class InwardParticle extends ParticleData {

        public InwardParticle(Object source, float startTime, Vec2 origin, Vec2 destination) {
            super(source, startTime, origin, destination);
        }

        @Override
        public float computeProgress(TooltipState state) {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(state));
        }

        @Override
        public Vec2 computeTranslation(TooltipState state, float progress) {
            return super.computeTranslation(state, progress).scale(0.4f);
        }

        @Override
        public float computeScale(TooltipState state, float progress) {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.25f);
        }

        @Override
        public float computeRotation(TooltipState state, float progress) {
            return (float) Math.atan2(destination.y - origin.y, destination.x - origin.x);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TooltipParticle.CODEC.fieldOf("particle").forGetter(InwardParticleEffect::particle)
        ).apply(codec, InwardParticleEffect::new));
    }
}
