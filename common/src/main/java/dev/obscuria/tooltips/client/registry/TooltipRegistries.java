package dev.obscuria.tooltips.client.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.fragmentum.content.registry.DelegatedRegistry;
import dev.obscuria.fragmentum.content.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.content.registry.Registrar;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.tooltip.TooltipDefinition;
import dev.obscuria.tooltips.client.tooltip.TooltipLabel;
import dev.obscuria.tooltips.client.tooltip.TooltipStyle;
import dev.obscuria.tooltips.client.tooltip.element.effect.TooltipEffect;
import dev.obscuria.tooltips.client.tooltip.element.frame.TooltipFrame;
import dev.obscuria.tooltips.client.tooltip.element.icon.TooltipIcon;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import dev.obscuria.tooltips.client.tooltip.element.slot.TooltipSlot;
import dev.obscuria.tooltips.client.tooltip.filter.ItemFilter;
import dev.obscuria.tooltips.client.tooltip.label.LabelProvider;
import dev.obscuria.tooltips.client.tooltip.layout.TooltipLayout;
import dev.obscuria.tooltips.client.tooltip.particle.TooltipParticle;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface TooltipRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(ObscureTooltips.MOD_ID);

    DelegatedRegistry<MapCodec<? extends ItemFilter>> ITEM_FILTER_TYPE = REGISTRAR.createRegistry(Key.ITEM_FILTER_TYPE);
    DelegatedRegistry<MapCodec<? extends LabelProvider>> LABEL_PROVIDER_TYPE = REGISTRAR.createRegistry(Key.LABEL_PROVIDER_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipParticle>> TOOLTIP_PARTICLE_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_PARTICLE_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipPanel>> TOOLTIP_PANEL_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_PANEL_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipFrame>> TOOLTIP_FRAME_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_FRAME_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipSlot>> TOOLTIP_SLOT_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_SLOT_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipIcon>> TOOLTIP_ICON_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_ICON_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipEffect>> TOOLTIP_EFFECT_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_EFFECT_TYPE);
    DelegatedRegistry<MapCodec<? extends TooltipLayout<?>>> TOOLTIP_LAYOUT_TYPE = REGISTRAR.createRegistry(Key.TOOLTIP_LAYOUT_TYPE);

    interface Resource {

        ResourceRegistry<TooltipPanel> TOOLTIP_PANEL = new ResourceRegistry<>("panel");
        ResourceRegistry<TooltipFrame> TOOLTIP_FRAME = new ResourceRegistry<>("frame");
        ResourceRegistry<TooltipSlot> TOOLTIP_SLOT = new ResourceRegistry<>("slot");
        ResourceRegistry<TooltipIcon> TOOLTIP_ICON = new ResourceRegistry<>("icon");
        ResourceRegistry<TooltipEffect> TOOLTIP_EFFECT = new ResourceRegistry<>("effect");
        ResourceRegistry<TooltipStyle> TOOLTIP_STYLE = new ResourceRegistry<>("style");
        ResourceRegistry<TooltipDefinition> TOOLTIP_DEFINITION = new ResourceRegistry.Ordered<>("definition");
        ResourceRegistry<TooltipLabel> TOOLTIP_LABEL = new ResourceRegistry.Ordered<>("label");
    }

    interface Key {

        ResourceKey<Registry<MapCodec<? extends ItemFilter>>> ITEM_FILTER_TYPE = create("item_filter_type");
        ResourceKey<Registry<MapCodec<? extends LabelProvider>>> LABEL_PROVIDER_TYPE = create("label_provider_type");
        ResourceKey<Registry<MapCodec<? extends TooltipParticle>>> TOOLTIP_PARTICLE_TYPE = create("tooltip_particle_type");
        ResourceKey<Registry<MapCodec<? extends TooltipPanel>>> TOOLTIP_PANEL_TYPE = create("tooltip_panel_type");
        ResourceKey<Registry<MapCodec<? extends TooltipFrame>>> TOOLTIP_FRAME_TYPE = create("tooltip_frame_type");
        ResourceKey<Registry<MapCodec<? extends TooltipSlot>>> TOOLTIP_SLOT_TYPE = create("tooltip_slot_type");
        ResourceKey<Registry<MapCodec<? extends TooltipIcon>>> TOOLTIP_ICON_TYPE = create("tooltip_icon_type");
        ResourceKey<Registry<MapCodec<? extends TooltipEffect>>> TOOLTIP_EFFECT_TYPE = create("tooltip_effect_type");
        ResourceKey<Registry<MapCodec<? extends TooltipLayout<?>>>> TOOLTIP_LAYOUT_TYPE = create("tooltip_layout_type");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(ObscureTooltips.key(name));
        }
    }

    static void init() {
        ItemFilter.bootstrap(BootstrapContext.create(REGISTRAR, Key.ITEM_FILTER_TYPE, ObscureTooltips::key));
        LabelProvider.bootstrap(BootstrapContext.create(REGISTRAR, Key.LABEL_PROVIDER_TYPE, ObscureTooltips::key));
        TooltipParticle.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_PARTICLE_TYPE, ObscureTooltips::key));
        TooltipPanel.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_PANEL_TYPE, ObscureTooltips::key));
        TooltipFrame.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_FRAME_TYPE, ObscureTooltips::key));
        TooltipSlot.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_SLOT_TYPE, ObscureTooltips::key));
        TooltipIcon.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_ICON_TYPE, ObscureTooltips::key));
        TooltipEffect.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_EFFECT_TYPE, ObscureTooltips::key));
        TooltipLayout.bootstrap(BootstrapContext.create(REGISTRAR, Key.TOOLTIP_LAYOUT_TYPE, ObscureTooltips::key));
    }
}
