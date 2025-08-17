package com.awa.hardthencreepersmod.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ClaySmeltingRecipe extends AbstractCookingRecipe {
    public ClaySmeltingRecipe(ResourceLocation id, String group, CookingBookCategory category,
                              Ingredient ingredient, ItemStack result,
                              float experience, int cookingTime) {
        super(ModRecipeTypes.CLAY_SMELTING.get(), id, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CLAY_SMELTING.get();
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }
}