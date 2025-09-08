package com.a_day650.hardthencreepersmod.blockentity;


import com.a_day650.hardthencreepersmod.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AlterAbstractFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    protected final RecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    protected final RecipeType<? extends AbstractCookingRecipe> vanillaRecipeType;

    public AlterAbstractFurnaceBlockEntity(BlockEntityType<?> blockEntityTypeIn,
                                           BlockPos blockPos,
                                           BlockState blockState,
                                           RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn,
                                           RecipeType<? extends AbstractCookingRecipe> vanillaRecipeTypeIn) {
        super(blockEntityTypeIn, blockPos, blockState, vanillaRecipeTypeIn);
        this.specificRecipeType = specificRecipeTypeIn;
        this.vanillaRecipeType = vanillaRecipeTypeIn;
    }

    /* FOLLOWING Code helps the copied code below. */

    public static final int BURN_TIME = 0;        // 剩余燃烧时间(ticks)
    public static final int FUEL_TIME = 1;        // 燃料总燃烧时间(原版使用)
    public static final int COOK_TIME = 2;        // 当前烹饪进度
    public static final int COOK_TIME_TOTAL = 3;  // 总烹饪所需时间

    /* FOLLOWING Code is copied from "Shadows-of-Fire/FastFurnace" mod to enhance performance */

    // 确保这三个常量正确定义
    public static final int INPUT = 0;    // 必须是0（输入槽）
    public static final int FUEL = 1;     // 必须是1（燃料槽）
    public static final int OUTPUT = 2;   // 必须是2（输出槽）

    protected AbstractCookingRecipe curRecipe;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    private boolean isBurning() {
        return this.dataAccess.get(BURN_TIME) > 0; //changed because of private variable
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlterAbstractFurnaceBlockEntity entity) {
        boolean wasBurning = entity.isBurning();
        boolean dirty = false;
        if (entity.isBurning()) {
            entity.dataAccess.set(BURN_TIME, entity.dataAccess.get(BURN_TIME) - 1); //changed because of private variable
        }

        if (level == null || level.isClientSide) {
            return;
        }

        ItemStack fuel = entity.items.get(FUEL);
        if (entity.isBurning() || !fuel.isEmpty() && !entity.items.get(INPUT).isEmpty()) {
            AbstractCookingRecipe irecipe = entity.getRecipe();
            boolean valid = entity.canBurn(irecipe);
            if (!entity.isBurning() && valid) {
                entity.dataAccess.set(BURN_TIME, entity.getBurnDuration(fuel)); //changed because of private variable
                entity.dataAccess.set(FUEL_TIME, entity.dataAccess.get(BURN_TIME)); //changed because of private variable
                if (entity.isBurning()) {
                    dirty = true;
                    if (fuel.hasCraftingRemainingItem()) entity.items.set(1, fuel.getCraftingRemainingItem());
                    else if (!fuel.isEmpty()) {
                        fuel.shrink(1);
                        if (fuel.isEmpty()) {
                            entity.items.set(1, fuel.getCraftingRemainingItem());
                        }
                    }
                }
            }

            if (entity.isBurning() && valid) {
                entity.dataAccess.set(COOK_TIME, entity.dataAccess.get(COOK_TIME) + 1); //changed because of private variable
                if (entity.dataAccess.get(COOK_TIME) == entity.dataAccess.get(COOK_TIME_TOTAL)) { //changed because of private variable
                    entity.dataAccess.set(COOK_TIME, 0); //changed because of private variable
                    entity.dataAccess.set(COOK_TIME_TOTAL, entity.getTotalCookTime(entity.getRecipe())); //changed because of private variable
                    entity.smeltItem(irecipe);
                    dirty = true;
                }
            } else {
                entity.dataAccess.set(COOK_TIME, 0); //changed because of private variable
            }
        } else if (!entity.isBurning() && entity.dataAccess.get(COOK_TIME) > 0) { //changed because of private variable
            entity.dataAccess.set(COOK_TIME, Mth.clamp(entity.dataAccess.get(COOK_TIME) - 2, 0, entity.dataAccess.get(COOK_TIME_TOTAL))); //changed because of private variable
        }

        if (wasBurning != entity.isBurning()) {
            dirty = true;
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, entity.isBurning()), 3);
        }

        if (dirty) {
            entity.setChanged();
        }
    }

    private boolean canBurn(@Nullable Recipe<?> recipe) {
        if (!this.items.get(0).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getResultItem(this.getLevel().registryAccess());
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.items.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!ItemStack.isSameItem(output, recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void smeltItem(@Nullable Recipe<?> recipe) {
        if (recipe != null && this.canBurn(recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.getResultItem(this.getLevel().registryAccess());
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.level != null && !this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    private int getTotalCookTime(AbstractCookingRecipe rec) {
        if (rec == null) {
            return 200;
        } else if (this.specificRecipeType.getClass().isInstance(rec.getType())) {
            return rec.getCookingTime();
        }
        return (int) (rec.getCookingTime() * ServerConfig.COOK_TIME_FACTOR.get());
    }

    public AbstractCookingRecipe getRecipe() {
        ItemStack input = this.getItem(INPUT);
        if (input.isEmpty()) {
            return null;
        }

        // 清除无效的缓存配方
        if (curRecipe != null && !curRecipe.matches(this, level)) {
            curRecipe = null;
        }

        if (this.level != null) {
            // 优先尝试获取特定配方
            AbstractCookingRecipe rec = this.level.getRecipeManager()
                    .getRecipeFor(this.specificRecipeType, this, this.level)
                    .orElse(null);

            if (rec != null) {
                dataAccess.set(COOK_TIME_TOTAL, getTotalCookTime(rec));
                return curRecipe = rec;
            }
        }

        return null;
    }

    protected boolean canSmelt(@Nullable AbstractCookingRecipe recipe) {
        if (recipe == null || this.items.get(0).isEmpty()) {
            return false;
        }

        ItemStack result = recipe.getResultItem(this.level.registryAccess());
        if (result.isEmpty()) return false;

        ItemStack output = this.items.get(2);
        if (output.isEmpty()) return true;

        return ItemStack.isSameItemSameTags(output, result) &&
                output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

}
