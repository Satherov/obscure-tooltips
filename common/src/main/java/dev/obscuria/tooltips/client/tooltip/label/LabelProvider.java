package dev.obscuria.tooltips.client.tooltip.label;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface LabelProvider {

    Codec<LabelProvider> CODEC = TooltipRegistries.LABEL_PROVIDER_TYPE.byNameCodec().dispatch(LabelProvider::codec, Function.identity());

    Codec<? extends LabelProvider> codec();

    ClientTooltipComponent create(ItemStack stack);

    static void bootstrap(BootstrapContext<Codec<? extends LabelProvider>> context) {
        context.register("literal", () -> LiteralLabelProvider.CODEC);
        context.register("translatable", () -> TranslatableLabelProvider.CODEC);
        context.register("rarity", () -> RarityLabelProvider.CODEC);
    }
}
