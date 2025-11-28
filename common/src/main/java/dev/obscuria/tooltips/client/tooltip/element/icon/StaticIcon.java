package dev.obscuria.tooltips.client.tooltip.element.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import net.minecraft.client.gui.GuiGraphics;

public record StaticIcon(Transform transform) implements TooltipIcon {

    public static final MapCodec<StaticIcon> CODEC;

    @Override
    public MapCodec<StaticIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(TooltipState state, GuiGraphics graphics, int x, int y) {
        pushTransform(state, transform, graphics, x, y);
        graphics.renderItem(state.stack, 0, 0);
        popTransform(graphics);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Transform.CODEC.fieldOf("transform").forGetter(StaticIcon::transform)
        ).apply(codec, StaticIcon::new));
    }
}
