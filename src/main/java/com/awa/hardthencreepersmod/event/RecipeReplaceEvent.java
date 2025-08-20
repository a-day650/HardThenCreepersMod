package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class RecipeReplaceEvent {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() == Items.STICK) {
            for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
                ItemStack stackInSlot = event.getInventory().getItem(i);
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof AxeItem) {
                    // 创建斧头的"剩余物品"（耐久度-1的斧头）
                    ItemStack damagedAxe = stackInSlot.copy();
                    damagedAxe.setDamageValue(damagedAxe.getDamageValue() + 1);

                    // 清空原格子
                    event.getInventory().setItem(i, ItemStack.EMPTY);

                    // 手动处理返还
                    if (!event.getEntity().getInventory().add(damagedAxe)) {
                        // 背包满了，掉落在地上
                        event.getEntity().drop(damagedAxe, false);
                    }
                    break;
                }
            }
        }
    }
}