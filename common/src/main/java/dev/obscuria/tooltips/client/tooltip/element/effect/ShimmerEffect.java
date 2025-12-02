package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;
import java.util.function.IntConsumer;

public record ShimmerEffect(
        ARGB innerColor,
        ARGB accentColor,
        ARGB outerColor,
        float frequency,
        float speed
) implements TooltipEffect {

    public static final Codec<ShimmerEffect> CODEC;

    @Override
    public Codec<ShimmerEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof ShimmerEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {

        final var actX = x - 3;
        final var actY = y - 3;
        final var actWidth = width + 6;
        final var actHeight = height + 6;

        final var ctrlWidth = actWidth - 24f;
        final var ctrlHeight = actHeight - 24f;
        final var ctrlLeft = actX + 12f;
        final var ctrlTop = actY + 12f;
        final var ctrlRight = ctrlLeft + ctrlWidth;
        final var CtrlBottom = ctrlTop + ctrlHeight;
        final var ctrlCenter = new Vector2f(ctrlLeft + ctrlWidth * 0.5f, ctrlTop + ctrlHeight * 0.5f);

        final var wSegments = 32;
        final var hSegments = Math.max(1, (int) (wSegments * ((float) actHeight / (float) actWidth)));

        final var control1 = new Vector2f();
        final var control2 = new Vector2f();
        final var inner1 = new Vector2f();
        final var inner2 = new Vector2f();
        final var outer1 = new Vector2f();
        final var outer2 = new Vector2f();

        final var matrix = graphics.pose().last().pose();
        final var buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay());
        final var time = state.timeInSeconds();

        // Top
        renderSide(wSegments,
                i -> inner1.set(actX + (float) actWidth * i / wSegments, actY),
                i -> inner2.set(actX + (float) actWidth * (i + 1) / wSegments, actY),
                i -> control1.set(ctrlLeft + ctrlWidth * i / wSegments, ctrlTop),
                i -> control2.set(ctrlLeft + ctrlWidth * (i + 1) / wSegments, ctrlTop),
                false, ctrlCenter, time, matrix, buffer, inner1, inner2, control1, control2, outer1, outer2);

        // Bottom
        renderSide(wSegments,
                i -> inner1.set(actX + (float) actWidth * (i + 1) / wSegments, actY + actHeight),
                i -> inner2.set(actX + (float) actWidth * i / wSegments, actY + actHeight),
                i -> control1.set(ctrlLeft + ctrlWidth * (i + 1) / wSegments, CtrlBottom),
                i -> control2.set(ctrlLeft + ctrlWidth * i / wSegments, CtrlBottom),
                false, ctrlCenter, time, matrix, buffer, inner1, inner2, control1, control2, outer1, outer2);

        // Left
        renderSide(hSegments,
                i -> inner1.set(actX, actY + (float) actHeight * i / hSegments),
                i -> inner2.set(actX, actY + (float) actHeight * (i + 1) / hSegments),
                i -> control1.set(ctrlLeft, ctrlTop + ctrlHeight * i / hSegments),
                i -> control2.set(ctrlLeft, ctrlTop + ctrlHeight * (i + 1) / hSegments),
                true, ctrlCenter, time, matrix, buffer, inner1, inner2, control1, control2, outer1, outer2);

        // Right
        renderSide(hSegments,
                i -> inner1.set(actX + actWidth, actY + (float) actHeight * (i + 1) / hSegments),
                i -> inner2.set(actX + actWidth, actY + (float) actHeight * i / hSegments),
                i -> control1.set(ctrlRight, ctrlTop + ctrlHeight * (i + 1) / hSegments),
                i -> control2.set(ctrlRight, ctrlTop + ctrlHeight * i / hSegments),
                true, ctrlCenter, time, matrix, buffer, inner1, inner2, control1, control2, outer1, outer2);
    }

    private void renderSide(int segments,
                            IntConsumer inner1Setter, IntConsumer inner2Setter,
                            IntConsumer ctrl1Setter, IntConsumer ctrl2Setter,
                            boolean flip, Vector2f ctrlCenter, float time,
                            Matrix4f matrix, VertexConsumer buffer,
                            Vector2f inner1, Vector2f inner2,
                            Vector2f ctrl1, Vector2f ctrl2,
                            Vector2f outer1, Vector2f outer2) {

        for (int i = 0; i < segments; i++) {

            inner1Setter.accept(i);
            inner2Setter.accept(i);
            ctrl1Setter.accept(i);
            ctrl2Setter.accept(i);

            final var a1 = ctrlCenter.angle(inner1);
            final var a2 = ctrlCenter.angle(inner2);

            final var t1 = 0.5f + 0.5f * (float) Math.cos(a1 * frequency + time * speed);
            final var t2 = 0.5f + 0.5f * (float) Math.cos(a2 * frequency + time * speed);

            outer1.set(inner1).lerp(ctrl1, 0.3f + 0.2f * t1);
            outer2.set(inner2).lerp(ctrl2, 0.3f + 0.2f * t2);

            if (flip) {
                GraphicUtils.color(buffer.vertex(matrix, inner1.x, inner1.y, 0f), innerColor.lerp(accentColor, t1)).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, inner2.x, inner2.y, 0f), innerColor.lerp(accentColor, t2)).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, outer2.x, outer2.y, 0f), outerColor).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, outer1.x, outer1.y, 0f), outerColor).endVertex();
            } else {
                GraphicUtils.color(buffer.vertex(matrix, inner2.x, inner2.y, 0f), innerColor.lerp(accentColor, t2)).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, inner1.x, inner1.y, 0f), innerColor.lerp(accentColor, t1)).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, outer1.x, outer1.y, 0f), outerColor).endVertex();
                GraphicUtils.color(buffer.vertex(matrix, outer2.x, outer2.y, 0f), outerColor).endVertex();
            }
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ARGB.CODEC.fieldOf("inner_color").forGetter(ShimmerEffect::innerColor),
                ARGB.CODEC.fieldOf("accent_color").forGetter(ShimmerEffect::accentColor),
                ARGB.CODEC.fieldOf("outer_color").forGetter(ShimmerEffect::outerColor),
                Codec.FLOAT.fieldOf("frequency").forGetter(ShimmerEffect::frequency),
                Codec.FLOAT.fieldOf("speed").forGetter(ShimmerEffect::speed)
        ).apply(codec, ShimmerEffect::new));
    }
}
