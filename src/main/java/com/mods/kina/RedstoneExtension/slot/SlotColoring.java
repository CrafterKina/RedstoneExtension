package com.mods.kina.RedstoneExtension.slot;

import com.mods.kina.RedstoneExtension.base.ContainerMachineBase;
import com.mods.kina.RedstoneExtension.base.GuiMachineBase;
import com.mods.kina.RedstoneExtension.base.SlotBase;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SlotColoring extends SlotBase{
    private GuiMachineBase gui;
    public int color=-1;
    public int clickCount=0;
    public SlotColoring(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_,GuiMachineBase base){
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        gui=base;
    }

    public boolean clickBehaviorExtend(ContainerMachineBase container){
        return inventory.getStackInSlot(slotNumber)==null;
    }

    public ItemStack onClicked(ContainerMachineBase container){
        if(clickCount==16) clickCount=0;
        else clickCount++;
        if(clickCount==16) color = -1;
        else color=ItemDye.field_150922_c[clickCount];
        return getStack();
    }

    public boolean isFailed(ContainerMachineBase container){
        return getStack()!=null;
    }

    public void onFailed(ContainerMachineBase container){
        clickCount--;
    }

    public void drawForeground(int mX, int mY){}

    public void drawBackground(int mX, int mY, float par3){
        if (!isVisible()) {
            return;
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("kina:"));
        drawSlotColor(xDisplayPosition,yDisplayPosition);
    }

    private void drawSlotColor(int x,int y){
        if(color==-1)return;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        GL11.glColor4f(r,g,b, 0.75F);
        gui.drawTexturedModalRect(x,y,0,0,8,8);
    }

    public boolean isVisible(){
        return true;
    }
}
