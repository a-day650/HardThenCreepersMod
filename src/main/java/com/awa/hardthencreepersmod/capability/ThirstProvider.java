package com.awa.hardthencreepersmod.capability;

import com.awa.hardthencreepersmod.thirst.IThirst;
import com.awa.hardthencreepersmod.thirst.ThirstImpl;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = "hardthencreepersmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThirstProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation THIRST_CAPABILITY_ID = new ResourceLocation("hardthencreepersmod", "thirst");

    public static final Capability<IThirst> THIRST_CAP =
            CapabilityManager.get(new CapabilityToken<>(){});

    private final IThirst instance = new ThirstImpl();
    private final LazyOptional<IThirst> optional = LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return THIRST_CAP.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return ThirstStorage.writeNBT(instance);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ThirstStorage.readNBT(instance, nbt);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            ThirstProvider provider = new ThirstProvider();
            event.addCapability(THIRST_CAPABILITY_ID, provider);
        }
    }

    public void invalidate() {
        optional.invalidate();
    }
}