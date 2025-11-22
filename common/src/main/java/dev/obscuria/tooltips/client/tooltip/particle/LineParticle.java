package dev.obscuria.tooltips.client.tooltip.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import dev.obscuria.tooltips.client.ParticleData;
import net.minecraft.client.gui.GuiGraphics;

public record LineParticle(
        ARGB centerColor,
        ARGB edgeColor,
        Transform transform
) implements TooltipParticle {

    public static final Codec<LineParticle> CODEC;

    @Override
    public Codec<LineParticle> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, TooltipState state, ParticleData data) {
        graphics.pose().pushPose();
        transform.apply(graphics);
        graphics.pose().translate(0f, -0.5f, 0f);
        GraphicUtils.drawHLineOverlay(graphics, -8, 0, 8, edgeColor, centerColor);
        GraphicUtils.drawHLineOverlay(graphics, 0, 0, 8, centerColor, edgeColor);
        graphics.pose().popPose();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ARGB.CODEC.fieldOf("center_color").forGetter(LineParticle::centerColor),
                ARGB.CODEC.fieldOf("edge_color").forGetter(LineParticle::edgeColor),
                Transform.CODEC.optionalFieldOf("transform", Transform.DEFAULT).forGetter(LineParticle::transform)
        ).apply(codec, LineParticle::new));
    }
}
