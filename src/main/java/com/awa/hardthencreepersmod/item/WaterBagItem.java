package com.awa.hardthencreepersmod.item;

import com.awa.hardthencreepersmod.capability.ThirstProvider;
import com.awa.hardthencreepersmod.effect.ModEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WaterBagItem extends Item {
    // 水袋最大容量
    public static final int MAX_WATER = 1000;
    // NBT标签键
    private static final String WATER_TAG = "WaterAmount";
    private static final String IS_CLEAN_TAG = "IsClean";

    public WaterBagItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // 检查是否对着水源方块右键
        if (level.getFluidState(pos).is(Fluids.WATER)) {
            // 填充水袋
            if (player != null) {
                int currentWater = getWaterAmount(stack);
                if (currentWater < MAX_WATER) {
                    // 增加水量（每次装100单位）
                    int added = Math.min(100, MAX_WATER - currentWater);
                    setWaterAmount(stack, currentWater + added);
                    setWaterClean(stack, false);

                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                    if (!level.isClientSide) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        // 首先检查是否对着水源方块（尝试装水）
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            InteractionResult onBlockResult = this.useOn(new UseOnContext(player, hand, hitResult));
            if (onBlockResult.consumesAction()) {
                return InteractionResultHolder.success(stack);
            }
        }

        // 检查水袋是否有水
        if (getWaterAmount(stack) > 0) {
            // 设置玩家使用物品的状态
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity entity) {
        if (!(entity instanceof Player)) return stack;

        Player player = (Player)entity;

        if (!level.isClientSide) {
            int waterAmount = getWaterAmount(stack);
            boolean isClean = isWaterClean(stack);

            // 播放喝水音效
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 0.5F,
                    level.random.nextFloat() * 0.1F + 0.9F);

            if (isClean) {
                player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
                    thirst.addThirst(4);
                });
            } else {
                player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
                    thirst.addThirst(3);
                });
                player.addEffect(new MobEffectInstance(ModEffects.THIRST.get(), 50*20, 1));
            }

            // 减少水量（每次消耗250单位）
            int newAmount = Math.max(0, waterAmount - 250);
            setWaterAmount(stack, newAmount);
            player.gameEvent(GameEvent.DRINK);
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32; // 喝水动画持续时间
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK; // 使用喝水动画
    }

    // 添加自定义物品名称显示水量
    @Override
    public Component getName(ItemStack stack) {
        int waterAmount = getWaterAmount(stack);
        String waterTypeKey = isWaterClean(stack) ?
                "item.hardthencreepersmod.water_bag.clean" :
                "item.hardthencreepersmod.water_bag.dirty";

        Component waterType = Component.translatable(waterTypeKey);
        Component status = Component.translatable("item.hardthencreepersmod.water_bag.status",
                waterType,
                waterAmount,
                MAX_WATER);

        return Component.translatable(this.getDescriptionId(stack))
                .append(" (")
                .append(status)
                .append(")");
    }

    // 添加这个方法让水袋有不同状态的外观
    @Override
    public boolean isFoil(ItemStack stack) {
        return isWaterClean(stack); // 只有纯净水会发光
    }

    // 在WaterBagItem中添加
    public static boolean canBePurified(ItemStack stack) {
        return getWaterAmount(stack) > 0 && !isWaterClean(stack);
    }

    // ===== 水量管理方法 =====
    public static int getWaterAmount(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(WATER_TAG) ? tag.getInt(WATER_TAG) : 0;
    }

    public static void setWaterAmount(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(WATER_TAG, Math.min(amount, MAX_WATER));
    }

    public static boolean isWaterClean(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(IS_CLEAN_TAG);
    }

    public static void setWaterClean(ItemStack stack, boolean isClean) {
        stack.getOrCreateTag().putBoolean(IS_CLEAN_TAG, isClean);
    }

    public static void purifyWater(ItemStack stack, Level level, BlockPos pos) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }

        // 确保同时保存水量和清洁状态
        stack.getTag().putBoolean(IS_CLEAN_TAG, true);
        stack.getTag().putInt(WATER_TAG, Math.min(getWaterAmount(stack), MAX_WATER));

        level.playSound(null, pos,
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.BLOCKS,
                0.5F, 2.0F);

        // 更新物品显示
        stack.setDamageValue(stack.getDamageValue()); // 强制更新显示
    }
}