package com.awa.hardthencreepersmod.init;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.block.ModBlocks;
import com.awa.hardthencreepersmod.blockentity.ClayFurnaceBlockEntity;
import com.awa.hardthencreepersmod.blockentity.WetRawBrickBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HardThenCreepersMod.MODID);

    // 修改注册方式，直接包含ticker
    public static final RegistryObject<BlockEntityType<ClayFurnaceBlockEntity>> CLAY_FURNACE =
            BLOCK_ENTITIES.register("clay_furnace", () ->
                    BlockEntityType.Builder.of(
                            ClayFurnaceBlockEntity::new,
                            ModBlocks.CLAY_FURNACE.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<WetRawBrickBlockEntity>> WET_RAW_BRICK =
            BLOCK_ENTITIES.register("wet_raw_brick", () ->
                    BlockEntityType.Builder.of(
                            WetRawBrickBlockEntity::new,
                            ModBlocks.WET_RAW_BRICK.get()
                    ).build(null));
}