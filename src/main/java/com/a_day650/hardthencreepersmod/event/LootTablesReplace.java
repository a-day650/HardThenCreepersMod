package com.a_day650.hardthencreepersmod.event;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class LootTablesReplace {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation grassTable = new ResourceLocation("minecraft", "blocks/grass");
        ResourceLocation tallGrassTable = new ResourceLocation("minecraft", "blocks/tall_grass");

        if (event.getName().equals(grassTable)) {
            LootPool.Builder pool = LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.PLANT_FIBER.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                            .when(LootItemRandomChanceCondition.randomChance(0.07f))
                    )
                    .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                            .when(LootItemRandomChanceCondition.randomChance(0.07f))
                    );
            event.setTable(new LootTable.Builder().withPool(pool).build());
        }

        if (event.getName().equals(tallGrassTable)) {
            LootPool.Builder pool = LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.PLANT_FIBER.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                            .when(LootItemRandomChanceCondition.randomChance(0.07f))
                    )
                    .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                    .when(LootItemRandomChanceCondition.randomChance(0.07f))
                    );
            event.setTable(new LootTable.Builder().withPool(pool).build());
        }
    }
}