package com.a_day650.hardthencreepersmod.core;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ConfigurableToolBlock;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistryOverrider {

    // 使用 RegisterEvent 来覆盖注册
    @SubscribeEvent
    public static void onRegisterBlocks(RegisterEvent event) {
        // 确保是注册方块
        if (!event.getRegistryKey().equals(ForgeRegistries.BLOCKS.getRegistryKey())) {
            return;
        }

        // 获取铜矿的注册名
        ResourceLocation copperOreId = BuiltInRegistries.BLOCK.getKey(Blocks.COPPER_ORE);
        ResourceLocation ironOreId = BuiltInRegistries.BLOCK.getKey(Blocks.IRON_ORE);

        // 覆盖铜矿
        event.register(ForgeRegistries.BLOCKS.getRegistryKey(), copperOreId, () ->
                new ConfigurableToolBlock(
                        BlockBehaviour.Properties.copy(Blocks.COPPER_ORE),
                        new ConfigurableToolBlock.ToolRequirement(ToolActions.PICKAXE_DIG, 1,2,1,2.0f),
                        ModBlocks.DESTROYED_ORE_BLOCK.get()
                ) {
                    @Override
                    protected List<ItemStack> getPartialDrops() {
                        return List.of(new ItemStack(Items.RAW_COPPER));
                    }

                    @Override
                    protected List<ItemStack> getFullDrops() {
                        return List.of(new ItemStack(Items.RAW_COPPER, 3));
                    }

                    @Override
                    protected List<ItemStack> getTooLittleDrops() {
                        return List.of();
                    }
                }
        );
        event.register(ForgeRegistries.BLOCKS.getRegistryKey(), ironOreId, () ->
                new ConfigurableToolBlock(
                        BlockBehaviour.Properties.copy(Blocks.IRON_ORE),
                        new ConfigurableToolBlock.ToolRequirement(ToolActions.PICKAXE_DIG, 1,2,1,2.0f),
                        ModBlocks.DESTROYED_ORE_BLOCK.get()
                ) {
                    @Override
                    protected List<ItemStack> getPartialDrops() {
                        return List.of(new ItemStack(Items.RAW_IRON));
                    }

                    @Override
                    protected List<ItemStack> getFullDrops() {
                        return List.of(new ItemStack(Items.RAW_IRON, 3));
                    }

                    @Override
                    protected List<ItemStack> getTooLittleDrops() {
                        return List.of();
                    }
                }
        );
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {}
}