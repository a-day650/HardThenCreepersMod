package com.awa.hardthencreepersmod.init;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.inventory.ClayFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, HardThenCreepersMod.MODID);

    public static final RegistryObject<MenuType<ClayFurnaceMenu>> CLAY_FURNACE_MENU =
            MENU_TYPES.register("clay_furnace_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new ClayFurnaceMenu(windowId, inv)));
}