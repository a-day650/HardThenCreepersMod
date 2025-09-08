package com.a_day650.hardthencreepersmod;

import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.effect.ModEffects;
import com.a_day650.hardthencreepersmod.entity.ModEntities;
import com.a_day650.hardthencreepersmod.event.Event;
import com.a_day650.hardthencreepersmod.init.ModBlockEntities;
import com.a_day650.hardthencreepersmod.init.ModMenuTypes;
import com.a_day650.hardthencreepersmod.item.ModCreativeModeTabs;
import com.a_day650.hardthencreepersmod.item.ModItems;
import com.a_day650.hardthencreepersmod.network.NetworkHandler;
import com.a_day650.hardthencreepersmod.recipe.ModRecipeSerializers;
import com.a_day650.hardthencreepersmod.recipe.ModRecipeTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HardThenCreepersMod.MODID)
public class HardThenCreepersMod {
    public static final String MODID = "hardthencreepersmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public HardThenCreepersMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 注册所有组件
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        ModRecipeSerializers.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(Event.class);
        NetworkHandler.register();
    }
}