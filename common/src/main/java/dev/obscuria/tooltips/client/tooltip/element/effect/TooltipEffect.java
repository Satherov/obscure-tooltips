package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.client.TooltipState;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.function.Function;

public interface TooltipEffect {

    Codec<TooltipEffect> DIRECT_CODEC = TooltipRegistries.TOOLTIP_EFFECT_TYPE.byNameCodec().dispatch(TooltipEffect::codec, Function.identity());
    Codec<TooltipEffect> CODEC = TooltipRegistries.Resource.TOOLTIP_EFFECT.byNameCodec();

    MapCodec<? extends TooltipEffect> codec();

    boolean canApply(List<TooltipEffect> effects);

    default void renderIcon(TooltipState state, GuiGraphics graphics, int x, int y) {}

    default void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {}

    default void renderFront(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {}

    static void bootstrap(BootstrapContext<MapCodec<? extends TooltipEffect>> context) {
        context.register("rim_light", () -> RimLightEffect.CODEC);
        context.register("ray_glow", () -> RayGlowEffect.CODEC);
        context.register("inward_particle", () -> InwardParticleEffect.CODEC);
        context.register("icon_particle", () -> IconParticleEffect.CODEC);
    }
}
