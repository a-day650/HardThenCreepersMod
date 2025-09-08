package com.a_day650.hardthencreepersmod.event;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.entity.ai.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class AnimalAIEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!event.getEntity().isAlive()) return;

        // 直接在主线程中执行，不需要 execute()
        if (event.getEntity() instanceof PathfinderMob pathfinderMob) {
            pathfinderMob.goalSelector.addGoal(4, new AttractToWetBrickGoal(pathfinderMob, 1.0D));
        }

        if (event.getEntity().getType() == EntityType.COW) {
            Cow cow = (Cow) event.getEntity();
            cow.goalSelector.addGoal(1, new CowKickGoal(cow));
        }

        if (event.getEntity().getType() == EntityType.HORSE) {
            Horse horse = (Horse) event.getEntity();
            horse.goalSelector.addGoal(1, new HorseKickGoal(horse));
        }

        if (event.getEntity().getType() == EntityType.CHICKEN) {
            Chicken chicken = (Chicken) event.getEntity();
            chicken.goalSelector.addGoal(2, new ChickenPeckGoal(chicken));
        }

        if (event.getEntity().getType() == EntityType.PIG) {
            Pig pig = (Pig) event.getEntity();
            pig.goalSelector.addGoal(3, new PigBiteGoal(pig));
        }
    }
}