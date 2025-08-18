package com.awa.hardthencreepersmod;

import com.awa.hardthencreepersmod.block.ModBlocks;
import com.awa.hardthencreepersmod.effect.ModEffects;
import com.awa.hardthencreepersmod.event.Event;
import com.awa.hardthencreepersmod.init.ModBlockEntities;
import com.awa.hardthencreepersmod.init.ModMenuTypes;
import com.awa.hardthencreepersmod.item.ModCreativeModeTabs;

import com.awa.hardthencreepersmod.item.ModItems;
import com.awa.hardthencreepersmod.network.NetworkHandler;
import com.awa.hardthencreepersmod.recipe.ModRecipeSerializers;
import com.awa.hardthencreepersmod.recipe.ModRecipeTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HardThenCreepersMod.MODID)
public class HardThenCreepersMod {
    public static final String MODID = "hardthencreepersmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public HardThenCreepersMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册所有组件
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus); // 添加菜单类型注册

        ModRecipeSerializers.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(Event.class);
        NetworkHandler.register();
    }
    private void setupClient(final FMLClientSetupEvent event) {}
}