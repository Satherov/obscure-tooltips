package dev.obscuria.tooltips.client.tooltip.element.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.SoundTemplate;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Optional;

public record StaticIcon(
        Transform transform,
        Optional<SoundTemplate> sound
) implements TooltipIcon {

    public static final Codec<StaticIcon> CODEC;

    @Override
    public Codec<StaticIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(TooltipState state, GuiGraphics graphics, int x, int y) {
        pushTransform(state, transform, graphics, x, y);
        graphics.renderItem(state.stack, 0, 0);
        popTransform(graphics);
        sound.ifPresent(state::maybePlayIconSound);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Transform.CODEC.fieldOf("transform").forGetter(StaticIcon::transform),
                SoundTemplate.CODEC.optionalFieldOf("sound").forGetter(StaticIcon::sound)
        ).apply(codec, StaticIcon::new));
    }
}
