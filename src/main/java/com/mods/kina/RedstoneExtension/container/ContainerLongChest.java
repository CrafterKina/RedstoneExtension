package com.mods.kina.RedstoneExtension.container;

import com.mods.kina.RedstoneExtension.tileentity.TileEntityLongChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLongChest extends Container{
    //Todo Release
    private TileEntityLongChest tileChest;
    private IInventory guiInventory = new InventoryBasic("slots", false, 3);
    public ContainerLongChest(IInventory par1IInventory, TileEntityLongChest par2TileEntityChest){
        tileChest = par2TileEntityChest;
        int i;
        int j;

        addSlotToContainer(new Slot(par2TileEntityChest, 0, 12, 21));
        addSlotToContainer(new Slot(par2TileEntityChest, 1, 80, 63));
        addSlotToContainer(new Slot(guiInventory, 1, 134, 63));

        for(i = 0; i < 3; ++i){
            for(j = 0; j < 9; ++j){
                this.addSlotToContainer(new Slot(par1IInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i){
            this.addSlotToContainer(new Slot(par1IInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer){
        return this.tileChest.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if(slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(par2 < 9){
                if(!this.mergeItemStack(itemstack1, 9, 45, true)){
                    return null;
                }
            }else if(!this.mergeItemStack(itemstack1, 0, 9, false)){
                return null;
            }

            if(itemstack1.stackSize == 0){
                slot.putStack(null);
            }else{
                slot.onSlotChanged();
            }

            if(itemstack1.stackSize == itemstack.stackSize){
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
