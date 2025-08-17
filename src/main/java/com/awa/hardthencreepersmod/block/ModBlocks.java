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
            () -> new ClayFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)
                    .strength(2.0f, 6.0f) // 比石炉稍弱
                    .sound(SoundType.STONE))
    );

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> ToReturnBlock = BLOCKS.register(name, block);
        registerBlockItem(name,ToReturnBlock);
        return ToReturnBlock;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name,() -> new BlockItem(block.get(), new Item.Properties()));
    }
}