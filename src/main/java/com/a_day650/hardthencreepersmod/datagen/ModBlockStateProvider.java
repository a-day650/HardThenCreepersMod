package com.a_day650.hardthencreepersmod.datagen;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HardThenCreepersMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerSleepingBag();
        registerStageBlock(ModBlocks.DESTROYED_ORE_BLOCK.get(), "destroyed_ore_block","stage", 3);

        registerFurnaceBlock(ModBlocks.CLAY_FURNACE);
        registerWetRawBrick();
    }

    private void registerSleepingBag() {
        // 直接使用现有的JSON模型文件，不要重新生成模型
        ModelFile headModel = models().getExistingFile(modLoc("block/sleeping_bag_head"));
        ModelFile footModel = models().getExistingFile(modLoc("block/sleeping_bag_foot"));
        ModelFile useHeadModel = models().getExistingFile(modLoc("block/sleeping_bag_use_head"));

        // 只生成方块状态
        getVariantBuilder(ModBlocks.SLEEPING_BAG.get())
                .forAllStates(state -> {
                    boolean occupied = state.getValue(BlockStateProperties.OCCUPIED);
                    BedPart part = state.getValue(BlockStateProperties.BED_PART);
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

                    ModelFile model;
                    if (part == BedPart.HEAD) {
                        model = occupied ? useHeadModel : headModel;
                    } else {
                        model = footModel;
                    }

                    int rotationY = switch (facing) {
                        case EAST -> 270;
                        case SOUTH -> 0;
                        case WEST -> 90;
                        case NORTH -> 180;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(rotationY)
                            .build();
                });
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void registerFurnaceBlock(RegistryObject<Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();

        // 生成熔炉方块状态
        ModelFile furnace = models().orientable(block.getDescriptionId(),
                modLoc("block/" + getBlockName(block) + "_side"),
                modLoc("block/" + getBlockName(block) + "_front"),
                modLoc("block/" + getBlockName(block) + "_top"));

        ModelFile furnaceOn = models().orientable(block.getDescriptionId() + "_on",
                modLoc("block/" + getBlockName(block) + "_side"),
                modLoc("block/" + getBlockName(block) + "_front_on"),
                modLoc("block/" + getBlockName(block) + "_top"));

        getVariantBuilder(block)
                .forAllStates(state -> {
                    boolean lit = state.getValue(BlockStateProperties.LIT);
                    net.minecraft.core.Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

                    int rotationY = switch (facing) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case NORTH -> 0;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(lit ? furnaceOn : furnace)
                            .rotationY(rotationY)
                            .build();
                });

        // 生成物品形式
        simpleBlockItem(block, furnace);
    }

    private String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private void axisBlockWithItem(RegistryObject<Block> blockRegistryObject) {
        RotatedPillarBlock block = (RotatedPillarBlock) blockRegistryObject.get();
        axisBlock(block, blockTexture(block));
        simpleBlockItem(blockRegistryObject.get(), models().getExistingFile(blockTexture(block)));
    }

    private void registerWetRawBrick() {
        // 创建各阶段的模型
        ModelFile[] stageModels = new ModelFile[4];
        for (int i = 0; i < 4; i++) {
            stageModels[i] = createCustomModel("wet_raw_brick_stage" + i,
                    modLoc("block/wet_raw_brick_stage" + i));
        }

        // 获取属性
        IntegerProperty growthStage = (IntegerProperty) ModBlocks.WET_RAW_BRICK.get().getStateDefinition().getProperty("growth_stage");
        DirectionProperty facing = BlockStateProperties.HORIZONTAL_FACING;

        // 生成方块状态
        getVariantBuilder(ModBlocks.WET_RAW_BRICK.get())
                .forAllStates(state -> {
                    int stage = state.getValue(growthStage);
                    Direction direction = state.getValue(facing);

                    int rotationY = switch (direction) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case NORTH -> 0;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(stageModels[stage])
                            .rotationY(rotationY)
                            .build();
                });
    }

    /**
     * 创建自定义方块模型（带elements的复杂模型）
     */
    private ModelFile createCustomModel(String name, ResourceLocation texture) {
        return models().withExistingParent(name, mcLoc("block/block"))
                .texture("particle", texture)
                .texture("texture", texture)
                .element()
                .from(3, 0, 5)
                .to(12, 2, 10)
                .allFaces((direction, faceBuilder) ->
                        faceBuilder.texture("#texture").cullface(direction)
                )
                .end();
    }

    /**
     * 更通用的自定义模型创建方法
     */
    private ModelFile createCustomModel(String name, ResourceLocation texture,
                                        float fromX, float fromY, float fromZ,
                                        float toX, float toY, float toZ) {
        return models().withExistingParent(name, mcLoc("block/block"))
                .texture("particle", texture)
                .texture("texture", texture)
                .element()
                .from(fromX, fromY, fromZ)
                .to(toX, toY, toZ)
                .allFaces((direction, faceBuilder) ->
                        faceBuilder.texture("#texture").cullface(direction)
                )
                .end();
    }

    private void registerStageBlock(Block block, String baseName, String propertyName, int maxStage) {
        // 创建各阶段的模型文件
        ModelFile[] stageModels = new ModelFile[maxStage];
        for (int i = 0; i < maxStage; i++) {
            stageModels[i] = models().cubeAll(baseName + "_stage" + i,
                    modLoc("block/" + baseName + "_stage" + i));
        }

        // 动态获取阶段属性
        IntegerProperty stageProperty = findIntegerProperty(block, propertyName);

        // 生成方块状态
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int stage = 0;
                    if (stageProperty != null) {
                        stage = state.getValue(stageProperty);
                    }

                    // 确保阶段在有效范围内
                    stage = Math.min(stage, maxStage - 1);
                    stage = Math.max(stage, 0);

                    return ConfiguredModel.builder()
                            .modelFile(stageModels[stage])
                            .build();
                });
    }

    /**
     * 智能查找整数属性
     */
    private IntegerProperty findIntegerProperty(Block block, String preferredName) {
        // 首先尝试使用指定的属性名称
        var property = block.getStateDefinition().getProperty(preferredName);
        if (property instanceof IntegerProperty intProp) {
            return intProp;
        }

        // 如果没有找到，尝试常见的属性名称
        String[] commonPropertyNames = {"stage", "age", "level", "state"};
        for (String name : commonPropertyNames) {
            property = block.getStateDefinition().getProperty(name);
            if (property instanceof IntegerProperty intProp) {
                return intProp;
            }
        }

        // 如果还是没找到，尝试所有整数属性
        for (var prop : block.getStateDefinition().getProperties()) {
            if (prop instanceof IntegerProperty intProp) {
                return intProp;
            }
        }

        return null;
    }

    /**
     * 更灵活的版本：自动检测阶段范围
     */
    private void registerStageBlockAuto(Block block, String baseName) {
        // 查找整数属性
        IntegerProperty stageProperty = findIntegerProperty(block, "stage");
        if (stageProperty == null) {
            // 如果没有阶段属性，使用默认模型
            simpleBlockWithItem(block, cubeAll(block));
            return;
        }

        int minStage = stageProperty.getPossibleValues().stream().min(Integer::compare).orElse(0);
        int maxStage = stageProperty.getPossibleValues().stream().max(Integer::compare).orElse(0);
        int stageCount = maxStage - minStage + 1;

        // 创建各阶段的模型文件
        ModelFile[] stageModels = new ModelFile[stageCount];
        for (int i = 0; i < stageCount; i++) {
            stageModels[i] = models().cubeAll(baseName + "_stage" + i,
                    modLoc("block/" + baseName + "_stage" + i));
        }

        // 生成方块状态
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int stage = state.getValue(stageProperty) - minStage;
                    stage = Math.min(stage, stageCount - 1);
                    stage = Math.max(stage, 0);

                    return ConfiguredModel.builder()
                            .modelFile(stageModels[stage])
                            .build();
                });
    }

    /**
     * 最灵活的版本：支持自定义模型生成器
     */
    private void registerStageBlockCustom(Block block, String baseName, String propertyName,
                                          java.util.function.Function<Integer, ModelFile> modelCreator) {

        IntegerProperty stageProperty = findIntegerProperty(block, propertyName);
        if (stageProperty == null) return;

        int minStage = stageProperty.getPossibleValues().stream().min(Integer::compare).orElse(0);
        int maxStage = stageProperty.getPossibleValues().stream().max(Integer::compare).orElse(0);

        // 生成方块状态
        getVariantBuilder(block)
                .forAllStates(state -> {
                    int stage = state.getValue(stageProperty);
                    ModelFile model = modelCreator.apply(stage);

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .build();
                });
    }
}