package com.awa.hardthencreepersmod.inventory;

import com.awa.hardthencreepersmod.init.ModMenuTypes;
import com.awa.hardthencreepersmod.recipe.ModRecipeBookTypes;
import com.awa.hardthencreepersmod.recipe.ModRecipeTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;

public class ClayFurnaceMenu extends AbstractFurnaceMenu {
    private static final int SLOT_COUNT = 3; // 输入/燃料/输出

    public ClayFurnaceMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(4));
    }

    public ClayFurnaceMenu(int id, Inventory playerInv, Container container, ContainerData data) {
        super(ModMenuTypes.CLAY_FURNACE_MENU.get(),
                ModRecipeTypes.CLAY_SMELTING.get(),
                ModRecipeBookTypes.CLAY_FURNACE,
                id, playerInv, container, data);
    }
}