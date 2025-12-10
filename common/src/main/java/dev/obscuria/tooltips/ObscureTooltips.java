package dev.obscuria.tooltips;

import dev.obscuria.fragmentum.client.FragmentumClientRegistry;
import dev.obscuria.fragmentum.content.packs.BuiltInPackBuilder;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ObscureTooltips {

    String MODID = "obscure_tooltips";
    String DISPLAY_NAME = "Obscure Tooltips";
    Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

    static ResourceLocation key(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    static void init() {
        ClientConfig.init();
        TooltipRegistries.init();
        FragmentumClientRegistry.registerTooltipComponent(StackBuffer.class, StackBuffer::asClient);
        BuiltInPackBuilder.resourcePack("packs/vibrant_tooltips")
                .displayName(Component.literal("Vibrant Tooltips"))
                .packSource(PackSource.BUILT_IN)
                .register(ObscureTooltips.class, MODID);
    }
}