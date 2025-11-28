package dev.obscuria.tooltips.client;

import dev.obscuria.tooltips.client.component.BlankComponent;
import dev.obscuria.tooltips.client.tooltip.TooltipDefinition;
import dev.obscuria.tooltips.client.tooltip.TooltipLabel;
import dev.obscuria.tooltips.client.tooltip.TooltipStyle;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

public abstract class TooltipState {

    public final ItemStack stack;
    public final TooltipStyle style;
    public final @Nullable TooltipLabel label;
    public final Long startTime;
    public final List<ParticleData> particles;

    protected TooltipState(ItemStack stack) {
        this.stack = stack;
        this.style = TooltipDefinition.aggregateStyleFor(stack);
        this.label = TooltipLabel.findFor(stack);
        this.startTime = Util.getMillis();
        this.particles = new ArrayList<>();
    }

    public boolean isInitialFrame() {
        return Util.getMillis() == startTime;
    }

    public float timeInSeconds() {
        return (Util.getMillis() - startTime) * 0.001f;
    }

    public ClientTooltipComponent createLabel() {
        return label != null ? label.create(stack) : BlankComponent.INSTANCE;
    }

    public void addParticle(ParticleData particle) {
        particles.add(particle);
    }

    public void renderPanel(GuiGraphics graphics, Vector2ic pos, int width, int height) {
        if (style.panel().isEmpty()) return;
        style.panel().get().render(graphics, pos.x(), pos.y(), width, height);
    }

    public void renderEffects(GuiGraphics graphics, Vector2ic pos, int width, int height) {
        for (var effect : style.effects()) {
            effect.renderBack(this, graphics, pos.x(), pos.y(), width, height);
        }
    }

    public void renderFrame(GuiGraphics graphics, Vector2ic pos, int width, int height) {
        if (style.frame().isEmpty()) return;
        style.frame().get().render(graphics, pos.x(), pos.y(), width, height);
    }

    public void removeExpiredParticles() {
        particles.removeIf(ParticleData::isExpired);
    }
}
