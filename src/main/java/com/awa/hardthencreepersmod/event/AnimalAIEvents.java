package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.entity.ai.ChickenPeckGoal;
import com.awa.hardthencreepersmod.entity.ai.CowKickGoal;
import com.awa.hardthencreepersmod.entity.ai.PigBiteGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class AnimalAIEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity().getType() == EntityType.COW) {
            Cow cow = (Cow) event.getEntity();
            cow.goalSelector.addGoal(1, new CowKickGoal(cow));
        }

        // 给鸡添加啄人AI（优先级2）
        if (event.getEntity().getType() == EntityType.CHICKEN) {
            Chicken chicken = (Chicken) event.getEntity();
            chicken.goalSelector.addGoal(2, new ChickenPeckGoal(chicken));
        }

        // 给猪添加啃咬AI（优先级3）
        if (event.getEntity().getType() == EntityType.PIG) {
            Pig pig = (Pig) event.getEntity();
            pig.goalSelector.addGoal(3, new PigBiteGoal(pig));
        }
    }
}