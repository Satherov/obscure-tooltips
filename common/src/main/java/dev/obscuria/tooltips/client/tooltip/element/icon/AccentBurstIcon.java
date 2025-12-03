package dev.obscuria.tooltips.client.tooltip.element.icon;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.SoundTemplate;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.Optional;

public record AccentBurstIcon(
        Transform transform,
        Optional<SoundTemplate> sound
) implements TooltipIcon {

    public static final Codec<AccentBurstIcon> CODEC;

    @Override
    public Codec<AccentBurstIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(TooltipState state, GuiGraphics graphics, int x, int y) {
        pushTransform(state, transform, graphics, x, y);
        graphics.renderItem(state.stack, 0, 0);
        popTransform(graphics);
        sound.ifPresent(state::maybePlayIconSound);
    }

    @Override
    public void applyScale(TooltipState state, GuiGraphics graphics, int x, int y) {
        final var time = state.timeInSeconds();
        final var scale = (time < 0.25f)
                ? Easing.EASE_OUT_CUBIC.compute(time / 0.25f) * 1.75f
                : (time < 0.5f)
                ? 1.75f - 0.75f * Easing.EASE_OUT_BACK.compute((time - 0.25f) / 0.25f)
                : 1f;
        graphics.pose().scale(scale, scale, scale);
    }

    @Override
    public void applyRotation(TooltipState state, GuiGraphics graphics, int x, int y) {
        final var rotation = 360f * Mth.clamp(Easing.EASE_OUT_EXPO.compute(state.timeInSeconds()), 0f, 1f);
        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Transform.CODEC.fieldOf("transform").forGetter(AccentBurstIcon::transform),
                SoundTemplate.CODEC.optionalFieldOf("sound").forGetter(AccentBurstIcon::sound)
        ).apply(codec, AccentBurstIcon::new));
    }
}
