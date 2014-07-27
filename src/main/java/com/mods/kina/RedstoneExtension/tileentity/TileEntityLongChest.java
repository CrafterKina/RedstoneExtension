package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.RedstoneExtension.blocks.BlockTransportDropper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLongChest extends TileEntity implements IInventory{
    private ItemStack itemStack;
    private long stackSize;

    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        for(int i = 0; i < nbttaglist.tagCount(); ++i){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            itemStack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            stackSize = nbttagcompound1.getLong("Size");
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();
        if(itemStack != null){
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            itemStack.writeToNBT(nbttagcompound1);
            nbttagcompound1.setLong("Size", stackSize);
            nbttaglist.appendTag(nbttagcompound1);
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getSizeInventory(){

        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i){
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int i, int j){
        if(itemStack != null){
            ItemStack itemstack;

            if(itemStack.stackSize <= j){
                itemstack = itemStack.copy();
                itemStack.stackSize = 0;
                itemStack = null;
                return itemstack;
            }else{
                itemstack = itemStack.splitStack(j);

                if(itemStack.stackSize == 0){
                    itemStack = null;
                }

                return itemstack;
            }
        }else{
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i){
        if(itemStack != null){
            ItemStack itemstack = itemStack;
            itemStack = null;
            return itemstack;
        }else{
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack){
        if(itemstack != null && itemstack.isStackable()){
            if(itemStack == null){
                itemStack = itemstack;
                stackSize = itemstack.stackSize;
            }
            else if(itemStack.isItemEqual(itemstack)) stackSize += itemstack.stackSize;
        }
    }

    /**
     Returns the name of the inventory
     */
    public String getInventoryName(){
        return new BlockTransportDropper().getLocalizedName();
    }

    /**
     Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
        return new BlockTransportDropper().getUnlocalizedName() != null;
    }

    @Override
    public int getInventoryStackLimit(){

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer){

        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory(){

    }

    public void closeInventory(){

    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack){
        return true;
    }
}
