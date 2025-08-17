package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.awa.hardthencreepersmod.event.Event.*;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class TreeDestroyEvent {
    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.is(BlockTags.LOGS) && event.getPlayer().getMainHandItem().is(ItemTags.TOOLS)) {
            if (isStrippedLog(state)) return;
            event.setCanceled(true);

            if (event.getLevel() instanceof Level level) {
                BlockState strippedState = getStrippedLogState(state);
                if (strippedState != null) {
                    // 保持原木方向
                    if (state.hasProperty(RotatedPillarBlock.AXIS)) {
                        strippedState = strippedState.setValue(RotatedPillarBlock.AXIS,
                                state.getValue(RotatedPillarBlock.AXIS));
                    }
                    level.setBlock(event.getPos(), strippedState, Block.UPDATE_ALL);
                }

                // 掉落树皮
                level.addFreshEntity(new ItemEntity(
                        level,
                        event.getPos().getX() + 0.5,
                        event.getPos().getY() + 0.5,
                        event.getPos().getZ() + 0.5,
                        new ItemStack(ModItems.BARK.get(), 1)
                ));
            }
        }
        Player player = event.getPlayer();
        ItemStack tool = player.getMainHandItem();
        CompoundTag tag = tool.getTag();
        int quality = tag != null && tag.contains("CraftLevel") ? tag.getInt("CraftLevel") : 15;
        applyDurabilityPenalty(player, tool, quality);
    }
}
