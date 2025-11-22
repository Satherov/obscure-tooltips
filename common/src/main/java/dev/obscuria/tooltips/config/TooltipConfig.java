package dev.obscuria.tooltips.config;

import dev.obscuria.fragmentum.config.ConfigLayout;
import dev.obscuria.fragmentum.config.ConfigOptions;
import dev.obscuria.fragmentum.config.ConfigRegistry;
import dev.obscuria.tooltips.ObscureTooltips;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TooltipConfig {

    public static final Layout client = new Layout();
    public static final List<Item> ignoredItems = new ArrayList<>();
    public static final List<Item> armorPreviewWhitelist = new ArrayList<>();
    public static final List<Item> armorPreviewBlacklist = new ArrayList<>();
    public static final List<Item> toolPreviewWhitelist = new ArrayList<>();
    public static final List<Item> toolPreviewBlacklist = new ArrayList<>();

    public static void init() {
        final var fileName = "obscuria/obscure_tooltips-client.toml";
        ConfigRegistry.registerClient(ObscureTooltips.MOD_ID, fileName, client, TooltipConfig::update);
    }

    private static void update(Layout layout) {

        ignoredItems.clear();
        armorPreviewWhitelist.clear();
        armorPreviewBlacklist.clear();
        toolPreviewWhitelist.clear();
        toolPreviewBlacklist.clear();

        layout.ignoredItems.forEach(id -> resolveOptionalItem(id, ignoredItems::add));
        layout.armorPreview.whitelist.forEach(id -> resolveOptionalItem(id, armorPreviewWhitelist::add));
        layout.armorPreview.blacklist.forEach(id -> resolveOptionalItem(id, armorPreviewBlacklist::add));
        layout.toolPreview.whitelist.forEach(id -> resolveOptionalItem(id, toolPreviewWhitelist::add));
        layout.toolPreview.blacklist.forEach(id -> resolveOptionalItem(id, toolPreviewBlacklist::add));
    }

    private static void resolveOptionalItem(String id, Consumer<Item> consumer) {
        try {
            final var location = new ResourceLocation(id);
            final var resourceKey = ResourceKey.create(Registries.ITEM, location);
            final var item = BuiltInRegistries.ITEM.getOrThrow(resourceKey);
            consumer.accept(item);
        } catch (Exception exception) {
            //ObscureTooltips.LOGGER.error("Config failed to load item '{}': {}", id, exception.getMessage());
        }
    }

    public static class Layout implements ConfigLayout {

        @ConfigOptions.Value("enabled")
        @ConfigOptions.Comment("Whether Obscure Tooltips should be active at all.")
        public boolean enabled = true;
        @ConfigOptions.Value("contentMargin")
        @ConfigOptions.Comment("The margin (in pixels) between the tooltip frame and its content.")
        public int contentMargin = 3;
        @ConfigOptions.Value("ignoredItems")
        @ConfigOptions.Comment("List of item IDs that should be ignored by Obscure Tooltips.")
        public List<String> ignoredItems = new ArrayList<>(List.of("quality_equipment:reforge_gui_button"));

        @ConfigOptions.Section("AutoWrapping")
        public final AutoWrapping autowrapping = new AutoWrapping();
        @ConfigOptions.Section("Scrolling")
        public final Scrolling scrolling = new Scrolling();
        @ConfigOptions.Section("ArmorPreview")
        public final ArmorPreview armorPreview = new ArmorPreview();
        @ConfigOptions.Section("ToolPreview")
        public final ToolPreview toolPreview = new ToolPreview();

        public static final class ArmorPreview {

            @ConfigOptions.Value("enabled")
            @ConfigOptions.Comment("Whether Obscure Tooltips should display 3D armor previews.")
            public boolean enabled = true;
            @ConfigOptions.Value("whitelist")
            @ConfigOptions.Comment("List of item IDs that should always display a 3D armor preview.")
            public List<String> whitelist = new ArrayList<>();
            @ConfigOptions.Value("blacklist")
            @ConfigOptions.Comment("List of item IDs that should never display a 3D armor preview.")
            public List<String> blacklist = new ArrayList<>();
        }

        public static final class ToolPreview {

            @ConfigOptions.Value("enabled")
            @ConfigOptions.Comment("Whether Obscure Tooltips should display 3D tool/weapon previews.")
            public boolean enabled = false;
            @ConfigOptions.Value("whitelist")
            @ConfigOptions.Comment("List of item IDs that should always display a 3D tool/weapon preview.")
            public List<String> whitelist = new ArrayList<>(List.of("minecraft:trident", "minecraft:bow", "minecraft:crossbow"));
            @ConfigOptions.Value("blacklist")
            @ConfigOptions.Comment("List of item IDs that should never display a 3D tool/weapon preview.")
            public List<String> blacklist = new ArrayList<>();
        }

        public static final class AutoWrapping {

            @ConfigOptions.Value("enabled")
            @ConfigOptions.Comment("Whether Obscure Tooltips should wrap lines in tooltips.")
            public boolean enabled = true;
            @ConfigOptions.Value("maxWidth")
            @ConfigOptions.Comment("The maximum width (in pixels) of a line in tooltips.")
            public int maxWidth = 300;
        }

        public static final class Scrolling {

            @ConfigOptions.Value("enabled")
            @ConfigOptions.Comment("Whether long tooltips should become scrollable when they exceed the screen height.")
            public boolean enabled = true;
            @ConfigOptions.Value("scrollSpeed")
            @ConfigOptions.Comment("How fast the tooltip scrolls when using the mouse wheel.")
            public int scrollSpeed = 5;
            @ConfigOptions.Value("scrollMargin")
            @ConfigOptions.Comment("Extra spacing (in pixels) above and below tooltip when scrolling is enabled.")
            public int scrollMargin = 10;
        }
    }
}
