package com.awa.hardthencreepersmod;

import com.awa.hardthencreepersmod.client.gui.ClayFurnaceScreen;
import com.awa.hardthencreepersmod.init.ModMenuTypes;
import com.awa.hardthencreepersmod.recipe.ModRecipeBookTypes;
import com.awa.hardthencreepersmod.recipe.ModRecipeTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy {

    // 定义配方书分类常量
    public static final RecipeBookCategories CLAY_FURNACE_SEARCH = RecipeBookCategories.create("CLAY_FURNACE_SEARCH", new ItemStack(Items.COMPASS));
    public static final RecipeBookCategories CLAY_FURNACE_FOOD = RecipeBookCategories.create("CLAY_FURNACE_FOOD", new ItemStack(Items.PORKCHOP));
    public static final RecipeBookCategories CLAY_FURNACE_BLOCKS = RecipeBookCategories.create("CLAY_FURNACE_BLOCKS", new ItemStack(Blocks.STONE));
    public static final RecipeBookCategories CLAY_FURNACE_MISC = RecipeBookCategories.create("CLAY_FURNACE_MISC", new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.EMERALD));
    public static final RecipeBookCategories CLAY_FURNACE_FUEL =
            RecipeBookCategories.create("CLAY_FURNACE_FUEL",
                    new ItemStack(Items.COAL),
                    new ItemStack(Items.LAVA_BUCKET));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        // 为陶瓦炉注册分类
        event.registerBookCategories(ModRecipeBookTypes.CLAY_FURNACE,
                ImmutableList.of(
                        CLAY_FURNACE_SEARCH,
                        CLAY_FURNACE_FOOD,
                        CLAY_FURNACE_BLOCKS,
                        CLAY_FURNACE_MISC,
                        CLAY_FURNACE_FUEL
                ));

        // 设置聚合分类
        event.registerAggregateCategory(CLAY_FURNACE_SEARCH, ImmutableList.of(CLAY_FURNACE_FOOD, CLAY_FURNACE_BLOCKS, CLAY_FURNACE_MISC,CLAY_FURNACE_FUEL));

        // 将配方类型映射到分类
        event.registerRecipeCategoryFinder(ModRecipeTypes.CLAY_SMELTING.get(), recipe -> {
            if (recipe instanceof AbstractCookingRecipe) {
                ItemStack result = recipe.getResultItem(null);
                if (result.getItem().isEdible()) {
                    return CLAY_FURNACE_FOOD;
                } else if (result.getItem() instanceof net.minecraft.world.item.BlockItem) {
                    return CLAY_FURNACE_BLOCKS;
                } else {
                    return CLAY_FURNACE_MISC;
                }
            }
            return CLAY_FURNACE_MISC;
        });
        event.registerRecipeCategoryFinder(
                RecipeType.SMELTING, // 使用原版熔炉类型检测燃料
                recipe -> {
                    // 只处理燃料类物品
                    if (AbstractFurnaceBlockEntity.getFuel().containsKey(recipe.getResultItem(null).getItem())) {
                        return CLAY_FURNACE_FUEL;
                    }
                    return null;
                }
        );
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 注册陶炉屏幕
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.CLAY_FURNACE_MENU.get(), ClayFurnaceScreen::new);
        });
    }

    // 在 onRegisterRecipeBookCategories 中添加燃料分类

}