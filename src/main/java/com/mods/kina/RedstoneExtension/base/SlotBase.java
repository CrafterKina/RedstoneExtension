package com.mods.kina.RedstoneExtension.base;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class SlotBase extends Slot{
    public SlotBase(IInventory inventory, int num, int x, int y){
        super(inventory,num,x,y);
    }
    public abstract boolean clickBehaviorExtend(ContainerMachineBase container);
    public abstract ItemStack onClicked(ContainerMachineBase container);
    public abstract boolean isFailed(ContainerMachineBase container);
    public abstract void onFailed(ContainerMachineBase container);
    public abstract void drawForeground(int mX,int mY);
    public abstract void drawBackground(int mX,int mY,float par3);
    public abstract boolean isVisible();
}
