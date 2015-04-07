package com.mods.kina.RedstoneExtension.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiMachineBase extends GuiContainer{
    public GuiMachineBase(Container p_i1072_1_){
        super(p_i1072_1_);
    }

    protected ResourceLocation guiTex;

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mX, int mY){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTex);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        drawSlot(p_146976_1_,mX,mY,false);
    }

    protected void drawGuiContainerForegroundLayer(int mX, int mY)
    {
        fontRendererObj.drawString(((ContainerMachineBase)inventorySlots).machineBase.getInventoryName(), this.xSize / 2 - this.fontRendererObj.getStringWidth(((ContainerMachineBase)inventorySlots).machineBase.getInventoryName()) / 2, 6, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        drawSlot(0f,mX,mY,true);
    }

    private void drawSlot(float par1,int mouseX,int mouseY,boolean isForeground){
        SlotBase slotBase;
        if (isForeground) for(Object slot : inventorySlots.inventorySlots){
            slotBase = (SlotBase) slot;
            if(slotBase.isVisible()) slotBase.drawForeground(mouseX, mouseY);
        }
        else for(Object slot : inventorySlots.inventorySlots){
            slotBase = (SlotBase) slot;
            if(slotBase.isVisible()) slotBase.drawBackground(mouseX, mouseY, par1);
        }
    }
    protected void mouseClicked(int x, int y, int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        Slot slot = getSlotAtPosition(x, y);
        if (slot instanceof SlotBase) {
            SlotBase slotBase=(SlotBase)slot;
            if(slotBase.clickBehaviorExtend((ContainerMachineBase)inventorySlots)){
                slotBase.onClicked((ContainerMachineBase)inventorySlots);
                if(slotBase.isFailed((ContainerMachineBase)inventorySlots)){
                    slotBase.onFailed((ContainerMachineBase)inventorySlots);
                }
            }
        }
    }

    private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        int left = this.guiLeft;
        int top = this.guiTop;
        int realMouseX = mouseX - left;
        int realMouseY = mouseY - top;
        return realMouseX >= slot.xDisplayPosition - 1 && realMouseX < slot.xDisplayPosition + 16 + 1 && realMouseY >= slot.yDisplayPosition - 1 && realMouseY < slot.yDisplayPosition + 16 + 1;
    }

    public Slot getSlotAtPosition(int x, int y) {
        for (int slotIndex = 0; slotIndex < this.inventorySlots.inventorySlots.size(); ++slotIndex) {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(slotIndex);
            if (isMouseOverSlot(slot, x, y)) {
                return slot;
            }
        }
        return null;
    }
}
