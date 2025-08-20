package com.awa.hardthencreepersmod.block;

import com.awa.hardthencreepersmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.awa.hardthencreepersmod.HardThenCreepersMod.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> CLAY_FURNACE = registerBlock(
            "clay_furnace",
            () -> new ClayFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(1.1f, 6.0f)
                    .sound(SoundType.STONE))
    );

    public static final RegistryObject<Block> WET_RAW_BRICK = registerBlock("wet_raw_brick",
            () -> new WetRawBrickBlock(BlockBehaviour.Properties.copy(Blocks.CLAY).noOcclusion()));

    public static final RegistryObject<Block> SLEEPING_BAG = registerBlock(
            "sleeping_bag",
            () -> new CustomBedBlock(BlockBehaviour.Properties.copy(Blocks.RED_BED)
                    .strength(0.2F)
                    .sound(SoundType.WOOL)
                    .noOcclusion())
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturnBlock = BLOCKS.register(name, block);
        registerBlockItem(name, toReturnBlock);
        return toReturnBlock;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}