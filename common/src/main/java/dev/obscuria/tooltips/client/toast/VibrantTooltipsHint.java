package dev.obscuria.tooltips.client.toast;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.fragmentum.util.easing.EasingFunction;
import dev.obscuria.tooltips.ObscureTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class VibrantTooltipsHint implements Toast {

    private static final ResourceLocation ICON_TEXTURE;
    private static final Component VIBRANT_TOOLTIPS;
    private static final EasingFunction EASING_1;
    private static final EasingFunction EASING_2;
    private static final ARGB TRANSPARENT;
    private static final ARGB WHITE;

    private final MultiLineLabel label1;
    private final MultiLineLabel label2;

    public VibrantTooltipsHint() {
        final var font = Minecraft.getInstance().font;
        this.label1 = MultiLineLabel.create(font, Component.translatable("toast.obscure_tooltips.hint_1", VIBRANT_TOOLTIPS), 120);
        this.label2 = MultiLineLabel.create(font, Component.translatable("toast.obscure_tooltips.hint_2"), 120);
    }

    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toasts, long timer) {
        graphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        graphics.blit(ICON_TEXTURE, 6, 6, 0, 0, 0, 20, 20, 20, 20);

        final var progress = timer / 8000f;
        if (progress < 0.5f) {
            final var y = 16 - label1.getLineCount() * 4.5f;
            final var color = TRANSPARENT.lerp(WHITE, EASING_1.compute(progress / 0.5f));
            label1.renderLeftAlignedNoShadow(graphics, 30, (int) y, 9, color.decimal());
        } else {
            final var y = 16 - label2.getLineCount() * 4.5f;
            final var color = TRANSPARENT.lerp(WHITE, EASING_2.compute((progress - 0.5f) / 0.5f));
            label2.renderLeftAlignedNoShadow(graphics, 30, (int) y, 9, color.decimal());
        }

        return timer < 8000L ? Visibility.SHOW : Visibility.HIDE;
    }

    static {
        ICON_TEXTURE = ObscureTooltips.key("textures/gui/icon.png");
        VIBRANT_TOOLTIPS = Component.literal("Vibrant Tooltips").withStyle(ChatFormatting.GOLD);
        TRANSPARENT = Colors.argbOf(0.05f, 1f, 1f, 1f);
        WHITE = Colors.argbOf(1f, 1f, 1f, 1f);
        EASING_1 = Easing.CEIL.mergeOut(Easing.EASE_IN_CUBIC, 0.75f);
        EASING_2 = Easing.EASE_OUT_CUBIC.mergeOut(Easing.FLOOR, 0.25f);
    }
}
