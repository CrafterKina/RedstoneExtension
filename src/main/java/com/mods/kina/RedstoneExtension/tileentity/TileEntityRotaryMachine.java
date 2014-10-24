package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.KinaCore.toExtends.IInventoryImpl;
import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import java.util.Random;

public class TileEntityRotaryMachine extends IInventoryImpl implements ISidedInventory{
    public int operations = 0;
    public boolean isOperating = false;
    public int currentNeedOperations=0;
    private Random random=new Random();
    ItemStack input = null;

    public TileEntityRotaryMachine(){
        ItemStack[] itemStacks = new ItemStack[6];
        setItemStack(itemStacks);
    }

    public void power(){
        if(!isOperating) input = getStackInSlot(1) != null ? getStackInSlot(1).copy() : getStackInSlot(2);
        System.out.println(getProgressScaled(24));
        if(input != null){
            System.out.println("PREPOWER");
            ItemStack partSlotStack = getStackInSlot(0);
            if(partSlotStack != null && partSlotStack.getItem() instanceof IRotaryMachineParts){
                IRotaryMachineParts parts = (IRotaryMachineParts) partSlotStack.getItem();
                System.out.println(parts.getRecipes());
                if(parts.getRecipes().containsKey(getNonStackItemStack(input))){
                    ItemStack out = (ItemStack) parts.getRecipes().get(getNonStackItemStack(input))[0];
                    ItemStack rare = parts.getRareRecipes().get(getNonStackItemStack(input)) != null ? (ItemStack) parts.getRareRecipes().get(getNonStackItemStack(input))[0] : null;
                    if(getStackInSlot(3)==null||getStackInSlot(4)==null||(out.isItemEqual(getStackInSlot(3))&&getStackInSlot(3).stackSize < getStackInSlot(3).getMaxStackSize())||(out.isItemEqual(getStackInSlot(4))&&getStackInSlot(4).stackSize < getStackInSlot(4).getMaxStackSize())){
                        if(!isOperating){
                            if(getStackInSlot(3) == null || getStackInSlot(4) == null || (out.isItemEqual(getStackInSlot(3)) && getStackInSlot(3).stackSize < getStackInSlot(3).getMaxStackSize()) || (out.isItemEqual(getStackInSlot(4)) && getStackInSlot(4).stackSize < getStackInSlot(4).getMaxStackSize())){
                                if(getStackInSlot(1) != null){
                                    getStackInSlot(1).stackSize -= 1;
                                    if(getStackInSlot(1).stackSize <= 0) setInventorySlotContents(1, null);
                                }else{
                                    getStackInSlot(2).stackSize -= 1;
                                    if(getStackInSlot(2).stackSize <= 0) setInventorySlotContents(2, null);
                                }
                                markDirty();
                            }
                        }
                        //for(; operations <= (Integer) parts.getRecipes().get(input.getItem())[1]; operations++){
                        worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
                        currentNeedOperations = (Integer) parts.getRecipes().get(getNonStackItemStack(input))[1];
                        isOperating = true;
                        operations++;
                        if(operations == (Integer) parts.getRecipes().get(getNonStackItemStack(input))[1]){
                            //if(getStackInSlot(3) == null || getStackInSlot(4) == null || (out.isItemEqual(getStackInSlot(3)) || out.isItemEqual(getStackInSlot(4))) && (getStackInSlot(3).stackSize >= getStackInSlot(3).getMaxStackSize() && getStackInSlot(4).stackSize >= getStackInSlot(4).getMaxStackSize())) && ((rare == null || rare.isItemEqual(getStackInSlot(6))) && getStackInSlot(6).stackSize >= getStackInSlot(6).getMaxStackSize()){
                            System.out.println("POWER");
                            for(int a = 3; a < 5; a++){
                                if(getStackInSlot(a) == null){
                                    setInventorySlotContents(a, out);
                                    markDirty();
                                    break;
                                }else if(getStackInSlot(a).isItemEqual(out) && getStackInSlot(a).stackSize+out.stackSize <= getStackInSlot(a).getMaxStackSize()){
                                    getStackInSlot(a).stackSize += out.stackSize;
                                    markDirty();
                                    break;
                                }
                            }
                            if(rare != null && (getStackInSlot(5) == null||((rare.isItemEqual(getStackInSlot(5)) && getStackInSlot(5).stackSize >= getStackInSlot(5).getMaxStackSize())))){
                                    if(random.nextInt(100) + 1 < (Float) parts.getRareRecipes().get(getNonStackItemStack(input))[1] * 100){
                                        System.out.println("ここまでOK");
                                        if(getStackInSlot(5) == null){
                                            System.out.println("挿入");
                                            setInventorySlotContents(5, rare);
                                            markDirty();
                                        }else/* if(getStackInSlot(5).getItem().equals(rare.getItem())&&getStackInSlot(5).stackSize+rare.stackSize<=rare.getMaxStackSize())*/{
                                            System.out.println(getStackInSlot(5));
                                            getStackInSlot(5).stackSize++;
                                            markDirty();
                                        }
                                    }
                            }
                        }else if(operations > (Integer) parts.getRecipes().get(getNonStackItemStack(input))[1]){
                            isOperating = false;
                            input = null;
                            operations = 0;
                            System.out.println("POSTPOWER");
                        }
                        //}
                    }else {
                        worldObj.playAuxSFX(1001,xCoord, yCoord, zCoord, 0);
                    }
                }else {
                    worldObj.playAuxSFX(1001,xCoord, yCoord, zCoord, 0);
                }
            }else {
                worldObj.playAuxSFX(1001,xCoord, yCoord, zCoord, 0);
            }
        }else {
            worldObj.playAuxSFX(1001, xCoord, yCoord, zCoord, 0);
        }
        System.out.println(isOperating+":"+getProgressScaled(24));
    }



    public static ItemStack getNonStackItemStack(ItemStack stack){
        return new ItemStack(stack.getItem(),1,stack.getItemDamage());
    }

    public int getProgressScaled(int par1){
        /*ItemStack input = getStackInSlot(1) != null ? getStackInSlot(1) : getStackInSlot(2);
        Integer times = (Integer) ((IRotaryMachineParts) getStackInSlot(0).getItem()).getRecipes().get(input.getItem())[1];
        return operations / times * par1;*/
        return operations!=0?operations * par1/currentNeedOperations:0;
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);

        for(int i = 0; i < nbttaglist.tagCount(); ++i){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if(b0 >= 0 && b0 < 6){
                setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        System.out.println(getStackInSlot(1));
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < 6; ++i){
            if(getStackInSlot(i) != null){
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        readFromNBT(pkt.func_148857_g());
    }

    /**
     Returns the name of the inventory
     */
    @Override
    public String getInventoryName(){
        return RedstoneExtensionCore.blockRotaryMachine.getLocalizedName();
    }

    public int[] getAccessibleSlotsFromSide(int p_94128_1_){
        return new int[0];
    }

    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_){
        return false;
    }

    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_){
        return false;
    }
    public boolean hasCustomInventoryName(){
        return !getInventoryName().isEmpty();
    }
}
