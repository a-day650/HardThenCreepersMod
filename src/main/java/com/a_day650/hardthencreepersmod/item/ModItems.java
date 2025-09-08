package com.a_day650.hardthencreepersmod.item;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.effect.ModEffects;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HardThenCreepersMod.MODID);

    public static final RegistryObject<Item> BARK = ITEMS.register("bark",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHARP_FLINT = ITEMS.register("sharp_flint",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEBBLE = ITEMS.register("pebble",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CLOTH = ITEMS.register("cloth",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WATER_BAG = ITEMS.register("water_bag",
            () -> new WaterBagItem(new WaterBagItem.Properties().stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .fireResistant()
            ));

    public static final RegistryObject<Item> FRIED_EGG_FOOD = ITEMS.register("fried_egg",
            () -> new Item(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(4) // 恢复4点饥饿值
                            .saturationMod(0.3f) // 饱和度修正值
                            .build()
            )));

    public static final RegistryObject<Item> FLINT_SHEARS = ITEMS.register("flint_shears",
            () -> new ShearsItem(new Item.Properties()
                    .durability(40)
                    .rarity(Rarity.COMMON)
            ));

    public static final RegistryObject<Item> MYSTERIOUS_RAW_MEAT = ITEMS.register("mysterious_raw_meat",
            () -> new Item(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.2f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 0.8f)
                            .effect(() -> new MobEffectInstance(ModEffects.GUILT.get(), 200, 0),1.0f)
                            .meat()
                            .build()
            )));

    public static final RegistryObject<Item> STONE_DUST = ITEMS.register("stone_dust",
            () -> new Item(new Item.Properties()));

    public static final Tier FLINT_HATCHET_TIER = new ForgeTier(
            1, // 挖掘等级（1 = 石头级）
            50, // 耐久度
            3.0f, // 攻击伤害加成（+3 攻击伤害）
            1.5f, // 攻击速度（比斧头稍慢）
            5, // 附魔能力（低）
            BlockTags.NEEDS_STONE_TOOL, // 需要石头级工具才能挖掘的方块
            () -> Ingredient.of(Items.FLINT) // 修复材料（燧石）
    );

    // 注册 "燧石手斧"
    public static final RegistryObject<AxeItem> FLINT_HATCHET = ITEMS.register(
            "flint_hatchet",
            () -> new ConfigurableQualityAxe(FLINT_HATCHET_TIER, 5.0f, -3.0f,new Item.Properties().durability(15).rarity(Rarity.COMMON),QualityConfig.DEFAULT){
            });

    public static final RegistryObject<Item> RAW_BRICK = ITEMS.register("raw_brick",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_ORE_DUST = ITEMS.register("raw_iron_dust",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){ITEMS.register(eventBus);}
}
