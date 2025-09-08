package com.a_day650.hardthencreepersmod.client.gui;

import com.a_day650.hardthencreepersmod.inventory.ClayFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ClayFurnaceScreen extends AbstractFurnaceScreen<ClayFurnaceMenu> {
    private static final ResourceLocation TEXTURE =new ResourceLocation("textures/gui/container/furnace.png");

    public ClayFurnaceScreen(ClayFurnaceMenu menu, Inventory inventory, Component component) {
        super(menu, new SmeltingRecipeBookComponent(), inventory, component, TEXTURE);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getSlot(1).getItem().isEmpty() &&
                this.hoveredSlot != null && this.hoveredSlot.index == 1) {
            graphics.renderTooltip(this.font,
                    Component.translatable("tooltip.hardthencreepersmod.need_fuel"),
                    mouseX, mouseY);
        }
        super.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

    }
}