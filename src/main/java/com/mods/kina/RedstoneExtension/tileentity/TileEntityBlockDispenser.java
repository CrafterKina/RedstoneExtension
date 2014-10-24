package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.RedstoneExtension.blocks.BlockBlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntityBlockDispenser extends TileEntity implements IInventory{
    protected String field_146020_a;
    private ItemStack[] field_146022_i = new ItemStack[9];
    private Random field_146021_j = new Random();

    /**
     Returns the number of slots in the inventory.
     */
    public int getSizeInventory(){
        return 9;
    }

    /**
     Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1){
        return this.field_146022_i[par1];
    }

    /**
     Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a new
     stack.
     */
    public ItemStack decrStackSize(int par1, int par2){
        if(this.field_146022_i[par1] != null){
            ItemStack itemstack;

            if(this.field_146022_i[par1].stackSize <= par2){
                itemstack = this.field_146022_i[par1];
                this.field_146022_i[par1] = null;
                this.markDirty();
                return itemstack;
            }else{
                itemstack = this.field_146022_i[par1].splitStack(par2);

                if(this.field_146022_i[par1].stackSize == 0){
                    this.field_146022_i[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }else{
            return null;
        }
    }

    /**
     When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem - like
     when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1){
        if(this.field_146022_i[par1] != null){
            ItemStack itemstack = this.field_146022_i[par1];
            this.field_146022_i[par1] = null;
            return itemstack;
        }else{
            return null;
        }
    }

    public int func_146017_i(){
        int i = -1;
        int j = 1;

        for(int k = 0; k < this.field_146022_i.length; ++k){
            if(this.field_146022_i[k] != null && this.field_146021_j.nextInt(j++) == 0){
                i = k;
            }
        }

        return i;
    }

    /**
     Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack){
        this.field_146022_i[par1] = par2ItemStack;

        if(par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()){
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public int func_146019_a(ItemStack p_146019_1_){
        for(int i = 0; i < this.field_146022_i.length; ++i){
            if(this.field_146022_i[i] == null || this.field_146022_i[i].getItem() == null){
                this.setInventorySlotContents(i, p_146019_1_);
                return i;
            }
        }

        return -1;
    }

    /**
     Returns the name of the inventory
     */
    public String getInventoryName(){
        return "tile.blockDispenser.name";
    }

    public void func_146018_a(String p_146018_1_){
        this.field_146020_a = p_146018_1_;
    }

    /**
     Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
        return this.field_146020_a != null;
    }

    public void readFromNBT(NBTTagCompound p_145839_1_){
        super.readFromNBT(p_145839_1_);
        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.field_146022_i = new ItemStack[this.getSizeInventory()];
        NBTTagList tagList = p_145839_1_.getTagList("SlotI", 10);
        NBTTagCompound tagCompound = tagList.getCompoundTagAt(0);
        BlockBlockDispenser.setI(tagCompound.getInteger("SlotNow"));
        for(int i = 0; i < nbttaglist.tagCount(); ++i){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if(j >= 0 && j < this.field_146022_i.length){
                this.field_146022_i[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if(p_145839_1_.hasKey("CustomName", 8)){
            this.field_146020_a = p_145839_1_.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound p_145841_1_){
        super.writeToNBT(p_145841_1_);
        NBTTagList nbttaglist = new NBTTagList();
        NBTTagList nbttaglist2 = new NBTTagList();
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        nbttagcompound2.setInteger("SlotNow", BlockBlockDispenser.getI());
        p_145841_1_.setTag("SlotI", nbttaglist2);
        nbttaglist.appendTag(nbttagcompound2);
        for(int i = 0; i < this.field_146022_i.length; ++i){
            if(this.field_146022_i[i] != null){
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.field_146022_i[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_145841_1_.setTag("Items", nbttaglist);

        if(this.hasCustomInventoryName()){
            p_145841_1_.setString("CustomName", this.field_146020_a);
        }
    }

    /**
     Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit(){
        return 64;
    }

    /**
     Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer){
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory(){
    }

    public void closeInventory(){
    }

    /**
     Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack){
        return true;
    }
}
