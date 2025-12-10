package dev.obscuria.tooltips.config;

import dev.obscuria.fragmentum.config.ConfigBuilder;
import dev.obscuria.fragmentum.config.ConfigValue;
import dev.obscuria.tooltips.ObscureTooltips;
import net.minecraft.world.item.Item;

import java.util.List;

public final class ClientConfig {

    public static final ConfigValue<Boolean> ENABLED;
    public static final ConfigValue<Boolean> LABELS_ENABLED;
    public static final ConfigValue<Boolean> SHADOWS_ENABLED;
    public static final ConfigValue<Double> SHADOW_OPACITY;
    public static final ConfigValue<Integer> CONTENT_MARGIN;
    public static final ConfigValue<List<? extends String>> IGNORED_ITEMS;

    public static final ConfigValue<Boolean> AUTO_WRAPPING_ENABLED;

    public static final ConfigValue<Boolean> SCROLL_ENABLED;
    public static final ConfigValue<Integer> SCROLL_SPEED;
    public static final ConfigValue<Integer> SCROLL_MARGIN;

    public static final ConfigValue<Boolean> ARMOR_PREVIEW_ENABLED;
    public static final ConfigValue<List<? extends String>> ARMOR_PREVIEW_WHITELIST;
    public static final ConfigValue<List<? extends String>> ARMOR_PREVIEW_BLACKLIST;

    public static final ConfigValue<Boolean> TOOL_PREVIEW_ENABLED;
    public static final ConfigValue<List<? extends String>> TOOL_PREVIEW_WHITELIST;
    public static final ConfigValue<List<? extends String>> TOOL_PREVIEW_BLACKLIST;

    private static final List<String> DEFAULT_IGNORED_ITEMS = List.of("quality_equipment:reforge_gui_button");
    private static final List<String> DEFAULT_TOOL_PREVIEW_WHITELIST = List.of("minecraft:trident", "minecraft:bow", "minecraft:crossbow", "minecraft:mace");

    public static boolean isIgnored(Item item) {
        return IGNORED_ITEMS.get().contains(idOf(item));
    }

    public static boolean isInArmorPreviewWhitelist(Item item) {
        return ARMOR_PREVIEW_WHITELIST.get().contains(idOf(item));
    }

    public static boolean isInArmorPreviewBlacklist(Item item) {
        return ARMOR_PREVIEW_BLACKLIST.get().contains(idOf(item));
    }

    public static boolean isInToolPreviewWhitelist(Item item) {
        return TOOL_PREVIEW_WHITELIST.get().contains(idOf(item));
    }

    public static boolean isInToolPreviewBlacklist(Item item) {
        return TOOL_PREVIEW_BLACKLIST.get().contains(idOf(item));
    }

    @SuppressWarnings("deprecation")
    private static String idOf(Item item) {
        return item.builtInRegistryHolder().key().location().toString();
    }

    public static void init() {}

    static {
        final var builder = new ConfigBuilder("obscuria/obscure_tooltips-client.toml");

        ENABLED = builder
                .comment("Whether Obscure Tooltips should be active at all.")
                .defineBoolean("enabled", true);
        LABELS_ENABLED = builder
                .comment("Whether Obscure Tooltips should display the second line in the tooltip header.")
                .defineBoolean("labelsEnabled", true);
        SHADOWS_ENABLED = builder
                .comment("Whether Obscure Tooltips should display drop shadows.")
                .defineBoolean("shadowsEnabled", true);
        SHADOW_OPACITY = builder
                .comment("The opacity of the drop shadows.")
                .defineDouble("shadowOpacity", 0.3, 0.0, 1.0);
        CONTENT_MARGIN = builder
                .comment("The margin (in pixels) between the tooltip frame and its content.")
                .defineInt("contentMargin", 3, 0, 16);
        IGNORED_ITEMS = builder
                .comment("List of item IDs that should be ignored by Obscure Tooltips.")
                .defineList("ignoredItems", DEFAULT_IGNORED_ITEMS, () -> "minecraft:apple", String.class::isInstance);

        builder.push("AutoWrapping");
        AUTO_WRAPPING_ENABLED = builder
                .comment("Whether Obscure Tooltips should wrap lines in tooltips.")
                .defineBoolean("autoWrappingEnabled", true);
        builder.pop();

        builder.push("Scrolling");
        SCROLL_ENABLED = builder
                .comment("Whether long tooltips should become scrollable when they exceed the screen height.")
                .defineBoolean("scrollEnabled", true);
        SCROLL_SPEED = builder
                .comment("How fast the tooltip scrolls when using the mouse wheel.")
                .defineInt("scrollSpeed", 5, 1, 64);
        SCROLL_MARGIN = builder
                .comment("Extra spacing (in pixels) above and below tooltip when scrolling is enabled.")
                .defineInt("scrollMargin", 10, 0, 64);
        builder.pop();

        builder.push("ArmorPreview");
        ARMOR_PREVIEW_ENABLED = builder
                .comment("Whether Obscure Tooltips should display 3D armor previews.")
                .defineBoolean("armorPreviewEnabled", true);
        ARMOR_PREVIEW_WHITELIST = builder
                .comment("List of item IDs that should always display a 3D armor preview.")
                .defineList("armorPreviewWhitelist", List.of(), () -> "minecraft:diamond_helmet", String.class::isInstance);
        ARMOR_PREVIEW_BLACKLIST = builder
                .comment("List of item IDs that should never display a 3D armor preview.")
                .defineList("armorPreviewBlacklist", List.of(), () -> "minecraft:diamond_helmet", String.class::isInstance);
        builder.pop();

        builder.push("ToolPreview");
        TOOL_PREVIEW_ENABLED = builder
                .comment("Whether Obscure Tooltips should display 3D tool/weapon previews.")
                .defineBoolean("toolPreviewEnabled", false);
        TOOL_PREVIEW_WHITELIST = builder
                .comment("List of item IDs that should always display a 3D tool/weapon preview.")
                .defineList("toolPreviewWhitelist", DEFAULT_TOOL_PREVIEW_WHITELIST, () -> "minecraft:trident", String.class::isInstance);
        TOOL_PREVIEW_BLACKLIST = builder
                .comment("List of item IDs that should never display a 3D tool/weapon preview.")
                .defineList("toolPreviewBlacklist", List.of(), () -> "minecraft:trident", String.class::isInstance);
        builder.pop();

        builder.buildClient(ObscureTooltips.MODID);
    }
}
