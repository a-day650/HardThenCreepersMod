package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.capability.ThirstProvider;
import com.awa.hardthencreepersmod.effect.ModEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class DrinkingWaterEvent {
    private static final int THIRST_DURATION = 20 * 40;
    private static final int WATER_RESTORE = 5;


    // 2. 药水饮用检测
    @SubscribeEvent
    public static void onDrinkPotion(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        ItemStack stack = event.getItem();
        if (stack.getItem() instanceof PotionItem && isWater(PotionUtils.getPotion(stack))) {
            applyThirstEffect(player);
        }
    }

    private static void applyThirstEffect(Player player) {
        if (player.level().isClientSide()) return;

        player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
            thirst.addThirst(WATER_RESTORE);
            player.addEffect(new MobEffectInstance(
                    ModEffects.THIRST.get(),
                    THIRST_DURATION,
                    0,
                    false, true, true
            ));
            player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
        });
    }

    private static boolean isWater(Potion potion) {
        return potion == Potions.WATER ||
                potion == Potions.WATER_BREATHING ||
                potion == Potions.LONG_WATER_BREATHING;
    }
}