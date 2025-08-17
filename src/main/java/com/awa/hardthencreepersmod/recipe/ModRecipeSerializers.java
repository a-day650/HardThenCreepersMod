package com.awa.hardthencreepersmod.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.awa.hardthencreepersmod.HardThenCreepersMod.MODID;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<RecipeSerializer<ClaySmeltingRecipe>> CLAY_SMELTING =
            SERIALIZERS.register("clay_smelting", () -> new ClaySmeltingRecipeSerializer(200));

    // 在Mod主类中注册
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}