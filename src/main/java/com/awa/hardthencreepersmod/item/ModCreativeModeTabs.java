// ModCreativeModeTabs.java
package com.awa.hardthencreepersmod.item;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

public class ModCreativeModeTabs {
    // 1. 注册 CreativeModeTab 的 DeferredRegister
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HardThenCreepersMod.MODID);

    // 2. 定义你的创造标签页
    public static final RegistryObject<CreativeModeTab> HARDTHENCREEPERSMOD_TAB = CREATIVE_MODE_TABS.register("htc_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FLINT_HATCHET.get())) // 标签页图标
                    .title(Component.translatable("item_group." + HardThenCreepersMod.MODID + ".htc_tab"))
                    .displayItems((parameters, output) -> {
                        // 3. 在这里添加要显示在标签页中的物品
                        output.accept(ModItems.BARK.get());
                        output.accept(ModItems.PLANT_FIBER.get());

                        output.accept(ModItems.FLINT_HATCHET.get());
                        output.accept(ModItems.FLINT_SHEARS.get());

                        output.accept(ModItems.WATER_BAG.get());

                        output.accept(ModItems.SHARP_FLINT.get());
                        output.accept(ModItems.PEBBLE.get());
                        output.accept(ModItems.CLOTH.get());

                        output.accept(ModBlocks.CLAY_FURNACE.get());
                        output.accept(ModBlocks.SLEEPING_BAG.get());

                        output.accept(ModItems.FRIED_EGG_FOOD.get());
                        // 可以继续添加更多物品
                    })
                    .build());

    // 4. 注册方法
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}