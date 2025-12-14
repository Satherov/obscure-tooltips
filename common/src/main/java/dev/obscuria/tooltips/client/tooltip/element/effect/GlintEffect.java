package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.TooltipHelper;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.config.ARGBProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

import java.util.List;

public record GlintEffect(
        int segments,
        boolean clipWaves,
        boolean clipRings,
        List<WaveSpecs> waves,
        List<RingSpecs> rings
) implements TooltipEffect {

    public static final Codec<GlintEffect> CODEC;
    private static final float TAU = (float) Math.PI * 2.0f;

    @Override
    public Codec<GlintEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof GlintEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {

        graphics.pose().pushPose();

        final var centerX = x + width * 0.5f;
        final var centerY = y + height * 0.5f;
        final var radius = (float) Math.hypot(width, height) * 0.85f;
        final var timer = state.timeInSeconds() * 0.1f;

        TooltipHelper.enableGlowingRenderer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        final var matrix = graphics.pose().last().pose();
        final var tesselator = Tesselator.getInstance();
        final var buffer = tesselator.getBuilder();

        if (clipWaves) graphics.enableScissor(x - 3, y - 3, x + width + 3, y + height + 3);
        renderWaves(buffer, matrix, centerX, centerY, width, height, radius, timer);
        if (clipWaves) graphics.disableScissor();

        if (clipRings) graphics.enableScissor(x - 3, y - 3, x + width + 3, y + height + 3);
        renderRings(buffer, matrix, centerX, centerY, radius, timer);
        if (clipRings) graphics.disableScissor();

        TooltipHelper.disableGlowingRenderer();

        graphics.pose().popPose();
    }

    private void renderWaves(BufferBuilder buffer, Matrix4f matrix, float x, float y, int width, int height, float radius, float timer) {
        if (waves.isEmpty()) return;
        final var aspect = height / (float) Math.max(1, width);

        for (var specs : waves) {
            final var color = specs.color.get();
            final var baseRadius = radius * specs.position();
            final var thickness = radius * specs.thickness() * 0.5f;
            final var progress = timer * TAU * specs.flowSpeed();

            buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

            for (var i = 0; i <= segments; i++) {
                final var segment = i / (float) segments;
                final var angle = segment * TAU + progress;

                final var sin = (float) Math.sin(angle);
                final var cos = (float) Math.cos(angle);

                final var swirl = computeSwirl(angle, progress, specs.flowOffset()) * radius;
                final var ringRadius = baseRadius + swirl;

                final var innerR = ringRadius + thickness * specs.innerBias();
                final var outerR = ringRadius + thickness * specs.outerBias();

                final var ix = x + cos * innerR;
                final var iy = y + sin * innerR * aspect;
                final var ox = x + cos * outerR;
                final var oy = y + sin * outerR * aspect;

                final var intensity = computeIntensity(sin, angle, progress, specs.intensityOffset(), specs.verticalFade());

                final var innerA = color.alpha() * intensity * specs.innerAlpha();
                final var outerA = color.alpha() * intensity * specs.outerAlpha();

                buffer.vertex(matrix, ox, oy, 0).color(color.red(), color.green(), color.blue(), outerA).endVertex();
                buffer.vertex(matrix, ix, iy, 0).color(color.red(), color.green(), color.blue(), innerA).endVertex();
            }

            BufferUploader.drawWithShader(buffer.end());
        }
    }

    private void renderRings(BufferBuilder buffer, Matrix4f matrix, float x, float y, float radius, float timer) {
        if (rings.isEmpty()) return;

        for (var ringIndex = 0; ringIndex < rings.size(); ringIndex++) {

            final var specs = rings.get(ringIndex);
            final var color = specs.color.get();
            final var baseRadius = radius * specs.radius();
            final var thickness = radius * specs.thickness();
            final var progress = timer * TAU * specs.spinSpeed();
            final var arcSpan = 4.3982296f;

            buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

            for (var i = 0; i <= segments; i++) {
                final var segment = i / (float) segments;
                final var angle = (segment - 0.5f) * arcSpan + progress + specs.arcOffset();
                final var sin = (float) Math.sin(angle);
                final var cos = (float) Math.cos(angle);

                final var wobble = computeWobble(segment, timer, ringIndex);
                final var rBase = baseRadius + wobble * radius * 0.18f;

                final var innerR = rBase - thickness * 0.5f;
                final var outerR = rBase + thickness * 0.5f;

                final var ix = x + cos * innerR;
                final var iy = y + sin * innerR;
                final var ox = x + cos * outerR;
                final var oy = y + sin * outerR;

                final var intensity = computeIntensity(segment, timer, ringIndex);

                final var edgeA = color.alpha() * intensity * 0.4f;
                final var coreA = color.alpha() * intensity * 0.9f;

                buffer.vertex(matrix, ox, oy, 0).color(color.red(), color.green(), color.blue(), edgeA).endVertex();
                buffer.vertex(matrix, ix, iy, 0).color(color.red(), color.green(), color.blue(), coreA).endVertex();
            }

            BufferUploader.drawWithShader(buffer.end());
        }
    }

    private float computeSwirl(float angle, float progress, float offset) {
        final var swirl1 = 0.12f * (float) Math.sin(angle * 3f + progress * 2.1f + offset * 0.7f);
        final var swirl2 = 0.07f * (float) Math.sin(angle * 7f - progress * 1.4f);
        return swirl1 + swirl2;
    }

    private float computeIntensity(float sin, float angle, float progress, float offset, boolean verticalFade) {
        final var band = 0.5f + 0.5f * (float) Math.sin(angle * 2f - progress * 1.3f + offset);
        if (verticalFade) {
            final var vertical = Math.max(0f, sin * 0.8f + 0.2f);
            return vertical * (0.4f + 0.6f * band);
        } else {
            final var hMask = (float) Math.pow(Math.abs(sin), 1.4f);
            return hMask * (0.35f + 0.65f * band);
        }
    }

    private float computeWobble(float segment, float timer, int ringIndex) {
        return 0.08f * (float) Math.sin(segment * 6f + timer * TAU * 2f + ringIndex * 1.7f);
    }

    private float computeIntensity(float segment, float timer, int ringIndex) {
        final var arcMask = (float) Math.sin(segment * Math.PI);
        final var pulse = 0.6f + 0.4f * (float) Math.sin(timer * TAU * 3f + segment * 5f + ringIndex * 1.3f);
        return arcMask * pulse;
    }

    public record WaveSpecs(
            ARGBProvider color,
            float position, float thickness,
            float innerAlpha, float innerBias,
            float outerAlpha, float outerBias,
            float flowSpeed, float flowOffset,
            float intensityOffset,
            boolean verticalFade
    ) {

        public static final Codec<WaveSpecs> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    ARGBProvider.CODEC.fieldOf("color").forGetter(WaveSpecs::color),
                    Codec.FLOAT.fieldOf("position").forGetter(WaveSpecs::position),
                    Codec.FLOAT.fieldOf("thickness").forGetter(WaveSpecs::thickness),
                    Codec.FLOAT.fieldOf("inner_alpha").forGetter(WaveSpecs::innerAlpha),
                    Codec.FLOAT.fieldOf("inner_bias").forGetter(WaveSpecs::innerBias),
                    Codec.FLOAT.fieldOf("outer_alpha").forGetter(WaveSpecs::outerAlpha),
                    Codec.FLOAT.fieldOf("outer_bias").forGetter(WaveSpecs::outerBias),
                    Codec.FLOAT.fieldOf("flow_speed").forGetter(WaveSpecs::flowSpeed),
                    Codec.FLOAT.fieldOf("flow_offset").forGetter(WaveSpecs::flowOffset),
                    Codec.FLOAT.fieldOf("intensity_offset").forGetter(WaveSpecs::intensityOffset),
                    Codec.BOOL.fieldOf("vertical_fade").forGetter(WaveSpecs::verticalFade)
            ).apply(codec, WaveSpecs::new));
        }
    }

    public record RingSpecs(
            ARGBProvider color,
            float spinSpeed, float radius,
            float thickness, float arcOffset
    ) {

        public static final Codec<RingSpecs> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    ARGBProvider.CODEC.fieldOf("color").forGetter(RingSpecs::color),
                    Codec.FLOAT.fieldOf("spin_speed").forGetter(RingSpecs::spinSpeed),
                    Codec.FLOAT.fieldOf("radius").forGetter(RingSpecs::radius),
                    Codec.FLOAT.fieldOf("thickness").forGetter(RingSpecs::thickness),
                    Codec.FLOAT.fieldOf("arc_offset").forGetter(RingSpecs::arcOffset)
            ).apply(codec, RingSpecs::new));
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("segments").forGetter(GlintEffect::segments),
                Codec.BOOL.optionalFieldOf("clip_waves", true).forGetter(GlintEffect::clipWaves),
                Codec.BOOL.optionalFieldOf("clip_rings", true).forGetter(GlintEffect::clipRings),
                WaveSpecs.CODEC.listOf().fieldOf("waves").forGetter(GlintEffect::waves),
                RingSpecs.CODEC.listOf().fieldOf("rings").forGetter(GlintEffect::rings)
        ).apply(codec, GlintEffect::new));
    }
}
