package com.a_day650.hardthencreepersmod.capability;

import com.a_day650.hardthencreepersmod.thirst.IThirst;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ThirstStorage {
    public static final Capability<IThirst> THIRST_CAP = CapabilityManager.get(new CapabilityToken<>(){});

    // 新版本中不再需要实现IStorage接口
    public static CompoundTag writeNBT(IThirst instance) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ThirstLevel", instance.getThirstLevel());
        return tag;
    }

    public static void readNBT(IThirst instance, CompoundTag nbt) {
        instance.setThirstLevel(nbt.getInt("ThirstLevel"));
    }
}