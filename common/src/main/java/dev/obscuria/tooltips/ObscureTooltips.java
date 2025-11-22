package dev.obscuria.tooltips;

import dev.obscuria.fragmentum.client.FragmentumClientRegistry;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.config.TooltipConfig;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ObscureTooltips {

    String MOD_ID = "obscure_tooltips";
    String MOD_NAME = "Obscure Tooltips";
    Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    static ResourceLocation key(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    static void init() {
        TooltipConfig.init();
        TooltipRegistries.init();
        FragmentumClientRegistry.registerTooltipComponent(StackBuffer.class, StackBuffer::asClient);
    }
}