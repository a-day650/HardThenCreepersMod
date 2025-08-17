package com.awa.hardthencreepersmod.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class ClaySmeltingRecipeSerializer implements RecipeSerializer<ClaySmeltingRecipe> {
    private final int defaultCookingTime;

    public ClaySmeltingRecipeSerializer(int defaultCookingTime) {
        this.defaultCookingTime = defaultCookingTime;
    }

    @Override
    public ClaySmeltingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        // 从JSON解析配方
        String group = GsonHelper.getAsString(json, "group", "");
        CookingBookCategory category = CookingBookCategory.CODEC.byName(
                GsonHelper.getAsString(json, "category", null),
                CookingBookCategory.MISC
        );
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int cookingTime = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);

        return new ClaySmeltingRecipe(recipeId, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ClaySmeltingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        // 从网络数据包解析
        String group = buffer.readUtf();
        CookingBookCategory category = buffer.readEnum(CookingBookCategory.class);
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        float experience = buffer.readFloat();
        int cookingTime = buffer.readVarInt();

        return new ClaySmeltingRecipe(recipeId, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ClaySmeltingRecipe recipe) {
        // 序列化到网络数据包
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookingTime());
    }
}