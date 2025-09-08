package com.a_day650.hardthencreepersmod.block;

import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static com.a_day650.hardthencreepersmod.HardThenCreepersMod.MODID;

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
    public static final RegistryObject<Block> DESTROYED_ORE_BLOCK = registerBlock("destroyed_ore_block",
            () -> new StageBreakingBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE)
                            .requiresCorrectToolForDrops(),
                    2,
                    (stage) -> switch (stage) {
                        case 0 ->
                                List.of(new ItemStack(ModItems.PEBBLE.get(), 2), new ItemStack(ModItems.STONE_DUST.get(), 1));
                        case 1 ->
                                List.of(new ItemStack(ModItems.PEBBLE.get(), 1), new ItemStack(ModItems.STONE_DUST.get(), 2));
                        case 2 -> List.of(new ItemStack(ModItems.STONE_DUST.get(), 3));
                        default -> List.of();
                    },
                    (stage) -> switch (stage) {
                        case 0 -> 1.5f;
                        case 1 -> 1.0f;
                        case 2 -> 0.5f;
                        default -> 1.5f;
                    },
                    (stage) -> {
                        switch (stage) {
                            case 0:
                                return new StageBreakingBlock.ToolRequirement(
                                        ToolActions.PICKAXE_DIG, 1, 2, 8.0f,
                                        (currentStage) -> List.of(
                                                new ItemStack(ModItems.PEBBLE.get(), 1),
                                                new ItemStack(ModItems.STONE_DUST.get(), 2)
                                        )
                                );
                            case 1:
                                return new StageBreakingBlock.ToolRequirement(
                                        ToolActions.PICKAXE_DIG, 0, 1, 4.0f,
                                        (currentStage) -> List.of(
                                                new ItemStack(ModItems.STONE_DUST.get(), 3)
                                        )
                                );
                            case 2:
                                return new StageBreakingBlock.ToolRequirement(
                                        null, 0, 0, 2.0f,
                                        (currentStage) -> List.of(
                                                new ItemStack(ModItems.STONE_DUST.get(), 2)
                                        )
                                );
                            default:
                                return new StageBreakingBlock.ToolRequirement(null, 0, 10, 1.0f);
                        }
                    }
            ));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturnBlock = BLOCKS.register(name, block);
        registerBlockItem(name, toReturnBlock);
        return toReturnBlock;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}