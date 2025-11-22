package dev.obscuria.tooltips.client.component;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.decoration.ArmorStand;

public record ArmorPreviewComponent(ArmorStand armorStand) implements ClientTooltipComponent {

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
            graphics.pose().translate(x + 18, y + 57, 500f);
            graphics.pose().scale(-30, -30, 30);
            graphics.pose().mulPose(Axis.XP.rotationDegrees(25));
            graphics.pose().mulPose(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * 20f));
            Lighting.setupForEntityInInventory();
            final var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            dispatcher.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> dispatcher.render(armorStand, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, graphics.pose(), graphics.bufferSource(), 15728880));
            dispatcher.setRenderShadow(true);
            Lighting.setupFor3DItems();
        });
    }
}
