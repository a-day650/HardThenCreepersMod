// ModCreativeModeTabs.java
package com.a_day650.hardthencreepersmod.item;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HardThenCreepersMod.MODID);

    public static final RegistryObject<CreativeModeTab> HARDTHENCREEPERSMOD_TAB = CREATIVE_MODE_TABS.register("htc_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FLINT_HATCHET.get())) // 标签页图标
                    .title(Component.translatable("item_group." + HardThenCreepersMod.MODID + ".htc_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.FLINT_HATCHET.get());
                        output.accept(ModItems.FLINT_SHEARS.get());

                        output.accept(ModItems.WATER_BAG.get());

                        output.accept(ModItems.BARK.get());
                        output.accept(ModItems.CLOTH.get());
                        output.accept(ModItems.PEBBLE.get());
                        output.accept(ModItems.STONE_DUST.get());
                        output.accept(ModItems.IRON_ORE_DUST.get());
                        output.accept(ModItems.PLANT_FIBER.get());
                        output.accept(ModItems.SHARP_FLINT.get());
                        output.accept(ModItems.RAW_BRICK.get());

                        output.accept(ModBlocks.CLAY_FURNACE.get());
                        output.accept(ModBlocks.WET_RAW_BRICK.get());
                        output.accept(ModBlocks.SLEEPING_BAG.get());
                        output.accept(ModBlocks.DESTROYED_ORE_BLOCK.get());

                        output.accept(ModItems.FRIED_EGG_FOOD.get());
                        output.accept(ModItems.MYSTERIOUS_RAW_MEAT.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {CREATIVE_MODE_TABS.register(eventBus);}
}