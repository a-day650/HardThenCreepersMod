package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FoodModifier {

    // 使用反射来修改物品的食物属性
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            modifyFoodProperties(Items.WHEAT_SEEDS, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.1F).build());

            modifyFoodProperties(Items.MELON_SEEDS, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.2F).build());

            modifyFoodProperties(Items.PUMPKIN_SEEDS, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.2F).build());

            modifyFoodProperties(Items.BEETROOT_SEEDS, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.3F).build());

            modifyFoodProperties(Items.TORCHFLOWER_SEEDS, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 200, 0), 0.4F).build());

            modifyFoodProperties(Items.PITCHER_POD, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.4F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0), 0.5F).build());

            // ==================== 常见花 ====================
            modifyFoodProperties(Items.DANDELION, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 60, 0), 0.3F).build());

            modifyFoodProperties(Items.POPPY, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.2F).build());

            modifyFoodProperties(Items.BLUE_ORCHID, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 120, 0), 0.3F).build());

            modifyFoodProperties(Items.ALLIUM, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 100, 0), 0.4F).build());

            modifyFoodProperties(Items.AZURE_BLUET, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 0), 0.3F).build());

            modifyFoodProperties(Items.RED_TULIP, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0), 0.2F).build());

            modifyFoodProperties(Items.ORANGE_TULIP, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0), 0.3F).build());

            modifyFoodProperties(Items.WHITE_TULIP, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 0), 0.2F).build());

            modifyFoodProperties(Items.PINK_TULIP, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 0.4F).build());

            modifyFoodProperties(Items.OXEYE_DAISY, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.3F).build());

            modifyFoodProperties(Items.CORNFLOWER, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK, 6000, 0), 0.1F).build()); // 幸运效果

            modifyFoodProperties(Items.LILY_OF_THE_VALLEY, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6F) // 有毒的铃兰
                    .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 80, 0), 0.3F).build());

            modifyFoodProperties(Items.TORCHFLOWER, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 300, 0), 0.8F).build());

            modifyFoodProperties(Items.PITCHER_PLANT, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 120, 0), 0.7F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.4F).build());

            // ==================== 稀有和特殊花 ====================
            modifyFoodProperties(Items.SUNFLOWER, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 0.5F).build()); // 吸收效果

            modifyFoodProperties(Items.LILAC, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0), 0.4F).build());

            modifyFoodProperties(Items.ROSE_BUSH, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.6F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 80, 0), 0.3F).build()); // 玫瑰有刺，可能中毒

            modifyFoodProperties(Items.PEONY, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 0.5F).build());

            modifyFoodProperties(Items.BIG_DRIPLEAF, new FoodProperties.Builder() // 大型垂滴叶
                    .nutrition(3).saturationMod(0.4F)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0), 0.7F).build());

            modifyFoodProperties(Items.SMALL_DRIPLEAF, new FoodProperties.Builder() // 小型垂滴叶
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0), 0.5F).build());

            modifyFoodProperties(Items.SPORE_BLOSSOM, new FoodProperties.Builder() // 孢子花
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 40, 0), 0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0), 0.6F).build());

            modifyFoodProperties(Items.FLOWERING_AZALEA, new FoodProperties.Builder() // 开花杜鹃花丛
                    .nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 80, 0), 0.4F).build());

            modifyFoodProperties(Items.AZALEA, new FoodProperties.Builder() // 杜鹃花丛
                    .nutrition(1).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0), 0.3F).build());

            modifyFoodProperties(Items.MOSS_CARPET, new FoodProperties.Builder() // 苔藓地毯
                    .nutrition(1).saturationMod(0.1F).build());

            modifyFoodProperties(Items.HANGING_ROOTS, new FoodProperties.Builder() // 垂根
                    .nutrition(1).saturationMod(0.1F).build());

            modifyFoodProperties(Items.ROOTED_DIRT, new FoodProperties.Builder() // 缠根泥土
                    .nutrition(0).saturationMod(0.0F) // 不能吃，只是标记
                    .alwaysEat().build());
            modifyFoodProperties(Items.BROWN_MUSHROOM, new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 80, 0), 0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.1F).build());

            modifyFoodProperties(Items.RED_MUSHROOM, new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 120, 0), 0.8F)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.6F).build());

            modifyFoodProperties(Items.CRIMSON_FUNGUS, new FoodProperties.Builder() // 绯红菌
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0), 0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.4F).build());

            modifyFoodProperties(Items.WARPED_FUNGUS, new FoodProperties.Builder() // 诡异菌
                    .nutrition(2).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 40, 0), 0.5F)
                    .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 80, 0), 0.3F).build());
        });
    }

    // 使用反射来修改物品的foodProperties字段
    private static void modifyFoodProperties(Item item, FoodProperties foodProperties) {
        try {
            // 获取Item类中的foodProperties字段
            java.lang.reflect.Field foodPropertiesField = Item.class.getDeclaredField("foodProperties");
            foodPropertiesField.setAccessible(true);

            // 设置物品的食物属性
            foodPropertiesField.set(item, foodProperties);
        } catch (Exception e) {
            HardThenCreepersMod.LOGGER.error("error modification!: " + item.getDescription().getString());
            HardThenCreepersMod.LOGGER.error("error!: " + e.getMessage());
        }
    }
}