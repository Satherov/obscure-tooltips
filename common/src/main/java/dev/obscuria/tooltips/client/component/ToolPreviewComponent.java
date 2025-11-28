package dev.obscuria.tooltips.client.component;

import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ToolPreviewComponent(ItemStack stack) implements ClientTooltipComponent {

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public int getWidth(Font font) {
        return 40;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {

        graphics.drawManaged(() -> {
            graphics.pose().pushPose();
            graphics.pose().translate(x + 18, y + 30, 500f);
            graphics.pose().scale(2.75f, 2.75f, 2.75f);
            graphics.pose().mulPose(Axis.XP.rotationDegrees(-30));
            graphics.pose().mulPose(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * -20f));
            graphics.pose().mulPose(Axis.ZP.rotationDegrees(-45));
            graphics.pose().pushPose();
            graphics.pose().translate(-8, -8, -150);
            graphics.renderItem(stack, 0, 0);
            graphics.pose().popPose();
            graphics.pose().popPose();
        });
    }
}
