package com.a_day650.hardthencreepersmod.datagen;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HardThenCreepersMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleItem(ModItems.STONE_DUST);
        this.simpleItem(ModItems.PEBBLE);
        this.simpleItem(ModItems.BARK);
        this.simpleItem(ModItems.CLOTH);
        this.simpleItem(ModItems.FLINT_SHEARS);
        this.simpleItem(ModItems.FRIED_EGG_FOOD);
        this.simpleItem(ModItems.IRON_ORE_DUST);
        this.simpleItem(ModItems.MYSTERIOUS_RAW_MEAT);
        this.simpleItem(ModItems.PLANT_FIBER);
        this.simpleItem(ModItems.RAW_BRICK);
        this.simpleItem(ModItems.WATER_BAG);

        this.simpleHandheldItem(ModItems.SHARP_FLINT);

        this.simple2DBlockItem(ModBlocks.WET_RAW_BRICK);

        registerSleepingBagItem();
        registerStageBlockItems();
    }

    private ItemModelBuilder simple2DBlockItem(RegistryObject<Block> block) {
        String path = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
        return withExistingParent(path, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + path));
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HardThenCreepersMod.MODID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleHandheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(HardThenCreepersMod.MODID,"item/" + item.getId().getPath()));
    }
    private void registerStageBlockItems() {
        // 自动为所有阶段化方块注册物品形式
        registerAllStageBlockItems();
    }

    /**
     * 自动为所有阶段化方块注册物品形式（使用第0阶段）
     */
    private void registerAllStageBlockItems() {
        registerStageBlockItem(ModBlocks.DESTROYED_ORE_BLOCK.get(), "destroyed_ore_block", 0);
    }

    /**
     * 灵活的阶段化方块物品注册
     * @param block 目标方块
     * @param baseName 基础名称（去掉_stageX后缀）
     * @param displayStage 作为物品图标使用的阶段
     */
    private void registerStageBlockItem(Block block, String baseName, int displayStage) {
        String modelName = baseName + "_stage" + displayStage;
        withExistingParent(getBlockName(block), modLoc("block/" + modelName));
    }

    /**
     * 自动检测方块名称
     */
    private String getBlockName(Block block) {
        return block.getDescriptionId().replace("block." + HardThenCreepersMod.MODID + ".", "");
    }

    /**
     * 更智能的版本：自动推断基础名称
     */
    private void registerStageBlockItemAuto(Block block, int displayStage) {
        String blockName = getBlockName(block);

        // 尝试自动推断基础名称（去掉可能的阶段后缀）
        String baseName = blockName;
        if (blockName.contains("_stage")) {
            baseName = blockName.substring(0, blockName.lastIndexOf("_stage"));
        }

        String modelName = baseName + "_stage" + displayStage;
        withExistingParent(blockName, modLoc("block/" + modelName));
    }

    /**
     * 支持自定义模型名称的版本
     */
    private void registerStageBlockItemCustom(Block block, String modelName) {
        withExistingParent(getBlockName(block), modLoc("block/" + modelName));
    }

    private void registerSleepingBagItem() {
        // 创建基础模型文件
        String jsonContent = """
    {
      "parent": "item/generated",
      "textures": {
        "layer0": "hardthencreepersmod:item/sleeping_bag"
      },
      "display": {
        "thirdperson_righthand": {
          "rotation": [30, 160, 0],
          "translation": [0, 3, -2],
          "scale": [0.23, 0.23, 0.23]
        },
        "firstperson_righthand": {
          "rotation": [30, 160, 0],
          "translation": [0, 3, 0],
          "scale": [0.375, 0.375, 0.375]
        },
        "gui": {
          "rotation": [30, 160, 0],
          "translation": [2, 3, 0],
          "scale": [0.5325, 0.5325, 0.5325]
        },
        "ground": {
          "rotation": [0, 0, 0],
          "translation": [0, 1, 2],
          "scale": [0.25, 0.25, 0.25]
        },
        "head": {
          "rotation": [0, 180, 0],
          "translation": [0, 10, -8],
          "scale": [1, 1, 1]
        },
        "fixed": {
          "rotation": [270, 0, 0],
          "translation": [0, 4, -2],
          "scale": [0.5, 0.5, 0.5]
        }
      }
    }
    """;
        try {
            Path path = this.output.getOutputFolder()
                    .resolve("assets/" + HardThenCreepersMod.MODID + "/models/item/sleeping_bag.json");
            Files.createDirectories(path.getParent());
            Files.write(path, jsonContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
