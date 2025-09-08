package com.a_day650.hardthencreepersmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "hardthencreepersmod");

    public static final RegistryObject<MobEffect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
    public static final RegistryObject<MobEffect> GUILT = EFFECTS.register("guilt", GuiltEffect::new);
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}