// 在 com.awa.hardthencreepersmod.client.screen 包下创建
package com.awa.hardthencreepersmod.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CrashScreen extends Screen {
    public CrashScreen() {
        super(Component.literal(""));
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        // 全屏黑色背景
        gui.fill(0, 0, this.width, this.height, 0xFF000000);

        // 显示崩溃信息
        gui.drawCenteredString(
                this.font,
                "§4客户端已崩溃\n愚人节快乐！",
                this.width / 2,
                this.height / 2,
                0xFFFFFF
        );
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false; // 禁止按ESC退出
    }
}