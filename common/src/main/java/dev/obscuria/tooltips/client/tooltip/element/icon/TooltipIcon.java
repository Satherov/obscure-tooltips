package dev.obscuria.tooltips.client.tooltip.element.icon;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.renderer.TooltipState;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface TooltipIcon {

    Codec<TooltipIcon> DIRECT_CODEC = TooltipRegistries.TOOLTIP_ICON_TYPE.byNameCodec().dispatch(TooltipIcon::codec, Function.identity());
    Codec<TooltipIcon> CODEC = TooltipRegistries.Resource.TOOLTIP_ICON.byNameCodec();

    Codec<? extends TooltipIcon> codec();

    void render(TooltipState state, GuiGraphics graphics, int x, int y);

    default void pushTransform(TooltipState state, Transform transform, GuiGraphics graphics, int x, int y) {
        graphics.pose().pushPose();
        graphics.pose().translate((float) x, (float) y, 150f);
        graphics.pose().translate(transform.offset().x, transform.offset().y, transform.offset().z);
        graphics.pose().scale(transform.scale(), transform.scale(), transform.scale());
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(transform.rotation()));
        applyScale(state, graphics, x, y);
        applyRotation(state, graphics, x, y);

        graphics.pose().pushPose();
        graphics.pose().translate(-8f, -8f, -150f);
    }

    default void popTransform(GuiGraphics graphics) {
        graphics.pose().popPose();
        graphics.pose().popPose();
    }

    default void applyScale(TooltipState state, GuiGraphics graphics, int x, int y) {}

    default void applyRotation(TooltipState state, GuiGraphics graphics, int x, int y) {}

    static void bootstrap(BootstrapContext<Codec<? extends TooltipIcon>> context) {

        context.register("static", () -> StaticIcon.CODEC);
        context.register("accent", () -> AccentIcon.CODEC);
        context.register("accent_spin", () -> AccentSpinIcon.CODEC);
        context.register("accent_burst", () -> AccentBurstIcon.CODEC);
    }
}
