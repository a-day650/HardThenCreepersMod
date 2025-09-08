package com.a_day650.hardthencreepersmod.datagen.loot;

import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.block.WetRawBrickBlock;
import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    private ItemStack createItem(Item item) {
        return new ItemStack(item);
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.CLAY_FURNACE.get(), createClayFurnaceLootTable());
        this.add(ModBlocks.WET_RAW_BRICK.get(), createWetRawBrickLootTable() );
        this.dropSelf(ModBlocks.SLEEPING_BAG.get());

        this.add(ModBlocks.DESTROYED_ORE_BLOCK.get(), createAgeDrops(
                ModBlocks.DESTROYED_ORE_BLOCK.get(),
                4, // maxAge
                0, // minAge
                "stage", // 属性名称
                new AgeDropInfo[]{
                        // 年龄0
                        new AgeDropInfo(0, List.of(
                                new ItemDrop(createItem(ModItems.PEBBLE.get()), 2, 3)
                        ), false),

                        // 年龄1
                        new AgeDropInfo(1, List.of(
                                new ItemDrop(createItem(ModItems.PEBBLE.get()), 1, 1),
                                new ItemDrop(createItem(ModItems.STONE_DUST.get()), 1, 2)
                        ), false),

                        // 年龄2
                        new AgeDropInfo(2, List.of(
                                new ItemDrop(createItem(ModItems.PEBBLE.get()), 1, 1),
                                new ItemDrop(createItem(ModItems.STONE_DUST.get()), 2, 3)
                        ), false),

                        // 年龄3
                        new AgeDropInfo(3, List.of(
                                new ItemDrop(createItem(ModItems.PEBBLE.get()), 1, 1),
                                new ItemDrop(createItem(ModItems.STONE_DUST.get()), 2, 4)
                        ), false),
                }
        ));
    }

    private LootTable.Builder createWetRawBrickLootTable() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_BRICK.get())  // 阶段3掉落生砖
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.WET_RAW_BRICK.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(WetRawBrickBlock.GROWTH_STAGE, 3)
                                        )
                                )
                                .when(ExplosionCondition.survivesExplosion())
                        )
                        .add(LootItem.lootTableItem(Items.CLAY_BALL)  // 阶段0-2掉落粘土球
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.WET_RAW_BRICK.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(WetRawBrickBlock.GROWTH_STAGE, 1)  // 阶段1
                                        )
                                )
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))  // 掉落1-3个粘土球
                                .when(ExplosionCondition.survivesExplosion())
                        )
                        .add(LootItem.lootTableItem(Items.CLAY_BALL)  // 阶段0-2掉落粘土球
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.WET_RAW_BRICK.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(WetRawBrickBlock.GROWTH_STAGE, 2)  // 阶段2
                                        )
                                )
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))  // 掉落1-3个粘土球
                                .when(ExplosionCondition.survivesExplosion())
                        )
                );
    }

    private LootTable.Builder createClayFurnaceLootTable() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.CLAY_BALL)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(5)))
                                .when(ExplosionCondition.survivesExplosion())
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModBlocks.CLAY_FURNACE.get())
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item()
                                        .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
                                ))
                        )
                );
    }

    protected LootTable.Builder createAgeDrops(Block block, int maxAge, int minAge, String agePropertyName, AgeDropInfo[] ageDropInfos) {
        LootTable.Builder builder = LootTable.lootTable();

        IntegerProperty ageProperty = (IntegerProperty) block.getStateDefinition().getProperty(agePropertyName);
        if (ageProperty == null) {
            return builder;
        }

        for (AgeDropInfo dropInfo : ageDropInfos) {
            if (dropInfo.age < minAge || dropInfo.age > maxAge) {
                continue;
            }

            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(ageProperty, dropInfo.age)));

            // 为列表中的每个 ItemDrop 创建掉落物
            for (ItemDrop itemDrop : dropInfo.itemDrops) {
                LootItem.Builder<?> lootItemBuilder = LootItem.lootTableItem(itemDrop.itemStack.getItem())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(itemDrop.minCount, itemDrop.maxCount)));

                if (itemDrop.itemStack.hasTag()) {
                    lootItemBuilder.apply(SetNbtFunction.setTag(itemDrop.itemStack.getTag()));
                }

                if (dropInfo.applyFortune) {
                    lootItemBuilder.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE));
                }

                poolBuilder.add(lootItemBuilder);
            }

            builder.withPool(poolBuilder);
        }

        return builder;
    }

    /**
     * @param itemDrops 改为 ItemDrop 列表！
     */ // 修改后的类
        public record AgeDropInfo(int age, List<ItemDrop> itemDrops, boolean applyFortune) {
    }

    // 新的 ItemDrop 类，每个物品有自己的数量范围
        public record ItemDrop(ItemStack itemStack, int minCount, int maxCount) {
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}