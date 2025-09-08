// 文件：ThirstShakeEvent.java
package com.a_day650.hardthencreepersmod.event;

import net.minecraftforge.eventbus.api.Event;

/**
 * 用于触发口渴HUD抖动的事件
 * 可设置抖动持续时间和强度
 */
public class ThirstShakeEvent extends Event {
    private final int durationTicks; // 抖动持续时间（单位：tick）
    private final float intensity;   // 抖动强度（1.0=默认，2.0=更剧烈）

    public ThirstShakeEvent(int durationTicks, float intensity) {
        this.durationTicks = durationTicks;
        this.intensity = intensity;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public float getIntensity() {
        return intensity;
    }
}