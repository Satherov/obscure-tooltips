package dev.obscuria.tooltips.client.tooltip.layout;

import com.mojang.serialization.MapCodec;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.ArmorPreviewComponent;
import dev.obscuria.tooltips.client.component.SplitComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ArmorPreviewLayout extends AbstractHeaderLayout<ArmorPreviewLayout.State> {

    public static final ArmorPreviewLayout INSTANCE = new ArmorPreviewLayout();
    public static final MapCodec<ArmorPreviewLayout> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ArmorPreviewLayout> codec() {
        return CODEC;
    }

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<ClientTooltipComponent> processPostWrap(State state, List<ClientTooltipComponent> components, Font font) {
        if (state.armorStand == null) return components;
        return List.of(new SplitComponent(new ArmorPreviewComponent(state.armorStand), components));
    }

    public static class State extends TooltipState {

        public final @Nullable ArmorStand armorStand;

        protected State(ItemStack stack) {
            super(stack);
            armorStand = makeArmorStand(stack);
        }

        private @Nullable ArmorStand makeArmorStand(ItemStack stack) {
            if (Minecraft.getInstance().level == null) return null;
            final var armorStand = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
            if (!(stack.getItem() instanceof ArmorItem armorItem)) return armorStand;
            armorStand.setItemSlot(armorItem.getEquipmentSlot(), stack);
            return armorStand;
        }
    }
}
