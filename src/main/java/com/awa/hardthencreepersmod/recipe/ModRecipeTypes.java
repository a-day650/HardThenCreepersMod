package com.awa.hardthencreepersmod.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.awa.hardthencreepersmod.HardThenCreepersMod.MODID;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);

    public static final RegistryObject<RecipeType<ClaySmeltingRecipe>> CLAY_SMELTING =
            RECIPE_TYPES.register("clay_smelting",
                    () -> RecipeType.simple(new ResourceLocation(MODID, "clay_smelting")));
}