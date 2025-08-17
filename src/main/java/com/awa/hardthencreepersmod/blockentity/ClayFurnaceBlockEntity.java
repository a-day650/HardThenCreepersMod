package com.awa.hardthencreepersmod.blockentity;

import com.awa.hardthencreepersmod.init.ModBlockEntities;
import com.awa.hardthencreepersmod.inventory.ClayFurnaceMenu;
import com.awa.hardthencreepersmod.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ClayFurnaceBlockEntity extends AlterAbstractFurnaceBlockEntity {

    // 修改构造函数，传入原版配方类型
    public ClayFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLAY_FURNACE.get(),
                pos,
                state,
                ModRecipeTypes.CLAY_SMELTING.get(), // 自定义配方类型
                RecipeType.SMELTING); // 原版熔炉配方类型
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.clay_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ClayFurnaceMenu(id, player, this, this.dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        // 自定义燃料燃烧时间
        if (fuel.is(Items.DRIED_KELP_BLOCK)) {
            return 4000; // 海带块燃烧4000ticks(200秒)
        }
        return super.getBurnDuration(fuel);
    }

    // 槽位交互逻辑
    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2}; // 输入(0), 燃料(1), 输出(2)
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot == 2 || (slot == 1 && stack.is(Items.BUCKET));
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 2) return false; // 不能直接放入输出槽
        if (slot == 1) return AbstractFurnaceBlockEntity.isFuel(stack); // 燃料槽只能放燃料
        return true; // 输入槽可以放任何物品
    }



    public RecipeType<?> getRecipeType() {
        return ModRecipeTypes.CLAY_SMELTING.get();
    }
}