package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public interface ItemFilter {

    Codec<ItemFilter> CODEC = TooltipRegistries.ITEM_FILTER_TYPE.byNameCodec().dispatch(ItemFilter::codec, Function.identity());

    Codec<? extends ItemFilter> codec();

    boolean test(ItemStack stack);

    static void bootstrap(BootstrapContext<Codec<? extends ItemFilter>> context) {
        context.register("config", () -> ConfigFilter.CODEC);
        context.register("always", () -> AlwaysFilter.CODEC);
        context.register("never", () -> NeverFilter.CODEC);
        context.register("all_of", () -> AllOfFilter.CODEC);
        context.register("any_of", () -> AnyOfFilter.CODEC);
        context.register("none_of", () -> NoneOfFilter.CODEC);
        context.register("item", () -> ItemOrTagFilter.CODEC);
        context.register("mod", () -> ModFilter.CODEC);
        context.register("enchantment", () -> EnchantmentFilter.CODEC);
        context.register("rarity", () -> RarityFilter.CODEC);
        context.register("nbt", () -> NbtFilter.CODEC);
        context.register("property", () -> PropertyFilter.CODEC);
    }
}
