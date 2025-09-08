// ConfigurableQualityAxe.java
package com.a_day650.hardthencreepersmod.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigurableQualityAxe extends AxeItem {
    private final QualityConfig qualityConfig;

    public ConfigurableQualityAxe(Tier tier, float attackDamage, float attackSpeed,
                                  Properties properties, QualityConfig qualityConfig) {
        super(tier, attackDamage, attackSpeed, properties);
        this.qualityConfig = qualityConfig;
    }

    @Deprecated(
            forRemoval = true,
            since = "1.5.0a"
    )
    public ConfigurableQualityAxe(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        this(tier, attackDamage, attackSpeed, properties, QualityConfig.DEFAULT);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (!level.isClientSide) {
            applyQualityTags(stack, player);
        }
    }

    protected int calculateQualityLevel(Player player) {
        return qualityConfig.calculateQuality(player.experienceLevel);
    }

    // ConfigurableQualityAxe.java - 添加合成状态检测
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // 只有在物品已经合成后才显示品质信息
        if (isCrafted(stack)) {
            int qualityLevel = getQualityLevel(stack);
            qualityConfig.addQualityTooltip(qualityLevel, tooltip);
        } else {
            // 未合成时显示通用提示
            tooltip.add(Component.translatable("tooltip.crafting_note")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }

    // 检查物品是否已经合成
    protected boolean isCrafted(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("CraftLevel");
    }

    // 修改获取品质等级的方法，只在已合成时返回实际值
    protected int getQualityLevel(ItemStack stack) {
        if (isCrafted(stack)) {
            return stack.getTag().getInt("CraftLevel");
        }
        return -1; // 未合成时返回特殊值，避免误导
    }

    // 修改品质标签应用
    protected void applyQualityTags(ItemStack stack, Player player) {
        int qualityLevel = calculateQualityLevel(player);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CraftLevel", qualityLevel);

        QualityTier tier = qualityConfig.getTier(qualityLevel);
        tag.putString("QualityKey", tier.langKey());
        tag.putString("QualityColor", tier.color().getName());
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state,
                             BlockPos pos, LivingEntity entity) {
        boolean result = super.mineBlock(stack, level, state, pos, entity);
        if (!level.isClientSide && entity instanceof Player player && state.getDestroySpeed(level,pos) != 0.0f) {
            applyDurabilityPenalty(stack, player);
        }
        return result;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide && attacker instanceof Player player) {
            applyDurabilityPenalty(stack, player);
        }
        return result;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            applyDurabilityPenalty(stack, player);
        }
        return super.use(level, player, hand);
    }

    protected void applyDurabilityPenalty(ItemStack stack, Player player) {
        int qualityLevel = getQualityLevel(stack);
        int damage = qualityConfig.getDurabilityPenalty(qualityLevel);

        if (damage > 0) {
            stack.hurtAndBreak(damage, player,
                    p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }
}