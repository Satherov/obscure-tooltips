package dev.obscuria.tooltips.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.tooltips.config.ClientConfig;
import dev.obscuria.tooltips.mixin.ClientTextTooltipAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface TooltipHelper {

    static void enableGlowingRenderer() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
    }

    static void disableGlowingRenderer() {
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    static int widthOf(List<ClientTooltipComponent> components, Font font) {
        var max = 0;
        for (var component : components) {
            final var width = component.getWidth(font);
            if (width <= max) continue;
            max = width;
        }
        return max;
    }

    static int heightOf(List<ClientTooltipComponent> components) {
        var sum = 0;
        for (var component : components) {
            sum += component.getHeight();
        }
        return sum;
    }

    static List<ClientTooltipComponent> wrapLines(GuiGraphics graphics, List<ClientTooltipComponent> components, Font font) {
        final var maxWidth = (int) (graphics.guiWidth() * 0.5);
        if (!shouldWrap(components, font, maxWidth)) return components;
        final var result = new ArrayList<ClientTooltipComponent>(components.size() * 2);
        for (var component : components) {
            if (!(component instanceof ClientTextTooltip tooltip)) {
                result.add(component);
            } else if (tooltip.getWidth(font) <= maxWidth) {
                result.add(tooltip);
            } else for (var line : font.getSplitter().splitLines(textOf(tooltip), maxWidth, Style.EMPTY)) {
                result.add(new ClientTextTooltip(Language.getInstance().getVisualOrder(line)));
            }
        }
        return result;
    }

    private static boolean shouldWrap(List<ClientTooltipComponent> components, Font font, int maxWidth) {
        if (!ClientConfig.AUTO_WRAPPING_ENABLED.get()) return false;
        for (var component : components) {
            if (!(component instanceof ClientTextTooltip tooltip)) continue;
            if (tooltip.getWidth(font) <= maxWidth) continue;
            return true;
        }
        return false;
    }

    private static FormattedText textOf(ClientTextTooltip tooltip) {

        final var sequence = ((ClientTextTooltipAccessor) tooltip).getText();
        final var elements = new ArrayList<String>();
        final var styles = new ArrayList<Style>();
        final var builder = new StringBuilder();
        final var currentStyle = new AtomicReference<>(Style.EMPTY);

        sequence.accept((i, style, codePoint) -> {

            if (!style.equals(currentStyle.get())) {
                if (!builder.isEmpty()) {
                    elements.add(builder.toString());
                    styles.add(currentStyle.get());
                    builder.setLength(0);
                }
                currentStyle.set(style);
            }

            builder.appendCodePoint(codePoint);
            return true;
        });

        if (!builder.isEmpty()) {
            elements.add(builder.toString());
            styles.add(currentStyle.get());
        }

        return new LineComponent(elements, styles);
    }

    record LineComponent(
            List<String> elements,
            List<Style> styles
    ) implements FormattedText {

        @Override
        public <T> Optional<T> visit(ContentConsumer<T> consumer) {
            for (var element : elements) {
                consumer.accept(element);
            }
            return Optional.empty();
        }

        @Override
        public <T> Optional<T> visit(StyledContentConsumer<T> consumer, Style base) {
            for (var i = 0; i < elements.size(); i++) {
                final var style = styles.get(i).applyTo(base);
                consumer.accept(style, elements.get(i));
            }
            return Optional.empty();
        }
    }
}
