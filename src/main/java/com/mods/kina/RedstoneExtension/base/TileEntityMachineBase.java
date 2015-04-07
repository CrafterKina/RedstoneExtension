package com.mods.kina.RedstoneExtension.base;

import com.google.common.primitives.Ints;
import com.mods.kina.KinaCore.toExtends.IInventoryImpl;
import com.mods.kina.RedstoneExtension.api.implementations.IGate;
import com.mods.kina.RedstoneExtension.renderer.block.BlockTransportDropperRender;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public abstract class TileEntityMachineBase extends IInventoryImpl implements ISidedInventory{
    //DOWN, UP, NORTH, SOUTH, WEST, EAST
    public ItemStack[] gates=new ItemStack[6];

    /*====================================Event From Block=======================================*/
    public ItemStack onClickByGate(ItemStack stack,int side){
        if(gates[side]==null/*&&side!=getBlockMetadata()*/){
            gates[side]= stack.splitStack(stack.stackSize - 1);
            worldObj.func_147479_m(xCoord,yCoord,zCoord);
            return stack;
        }
        return stack;
    }

    public ItemStack onClickByWrench(ItemStack stack,int side){
        if(gates[side]!=null){
            float f = 0.7F;
            double d0 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            worldObj.spawnEntityInWorld(new EntityItem(worldObj,xCoord+d0,yCoord+d1,zCoord+d2,gates[side]));
            gates[side]=null;
            stack.setItemDamage(stack.getItemDamage()-1);
            return stack;
        }else if(side==getBlockMetadata()){
            worldObj.getBlock(xCoord,yCoord,zCoord).dropBlockAsItem(worldObj,xCoord,yCoord,zCoord,getBlockMetadata(),0);
            worldObj.setBlockToAir(xCoord,yCoord,zCoord);
            stack.setItemDamage(stack.getItemDamage()-1);
            return stack;
        }else{
            blockType.rotateBlock(worldObj, xCoord, yCoord, zCoord, ForgeDirection.getOrientation(side));
            return stack;
        }
    }

    public ItemStack onClickByDye(ItemStack stack,int side){
        if(gates[side]!=null&&gates[side].getItemDamage()!=stack.getItemDamage()){
            gates[side].setItemDamage(stack.getItemDamage());
            return stack.stackSize-- != 0 ? stack.splitStack(1) : null;
        }
        return stack;
    }

    public ItemStack onClickByFilledContainer(ItemStack stack,int side){
        return stack;
    }
    /*=======================================Item Transfer=======================================*/
    protected boolean insertItemToInventory(){
        IInventory iinventory = getOutputInventory();
        if(iinventory == null){
            return false;
        }else{
            for(int i = 0; i < getOutputSlots().length; ++i)
            {
                if(getStackInSlot(getOutputSlots()[i]) != null){
                    ItemStack itemstack = getStackInSlot(getOutputSlots()[i]).copy();
                    ItemStack itemstack1 = TileEntityTransportDropper.insertStack(iinventory, decrStackSize(getOutputSlots()[i], 1), Facing.oppositeSide[BlockTransportDropperRender.getFace(getBlockMetadata())]);
                    if(itemstack1 == null || itemstack1.stackSize == 0){
                        iinventory.markDirty();
                        return true;
                    }
                    setInventorySlotContents(getOutputSlots()[i], itemstack);
                }
            }
            return false;
        }
    }

    protected IInventory getOutputInventory(){
        int i = BlockTransportDropperRender.getFace(getBlockMetadata());
        return TileEntityTransportDropper.getInventoryAtLocation(getWorldObj(), (double) (xCoord + Facing.offsetsXForSide[i]), (double) (yCoord + Facing.offsetsYForSide[i]), (double) (zCoord + Facing.offsetsZForSide[i]));
    }

    public int[] getOutputSlots(){
        ArrayList<Integer> l=new ArrayList<Integer>();
        for(int i = 0; i < getSlotRole().length; i++)
            if(getSlotRole()[i].equals("OutItem")) l.add(i);
        return Ints.toArray(l);
    }

    public String[] getSlotRole(){
        String[] role=new String[5];
        for(int i = 0; i < gates.length; i++){
            role[i]=gates[i]!=null?((IGate)gates[i].getItem()).getGateRole():"nothing";
        }
        return role;
    }

    /**
     Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     block.
     */
    public int[] getAccessibleSlotsFromSide(int p_94128_1_){
        return new int[0];
    }

    /**
     Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item, side
     */
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_){
        return false;
    }

    /**
     Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item, side
     */
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_){
        return false;
    }

    /*===========================================Misc===========================================*/

    public IIcon getIconSideOverlay(int side){
        return gates[side].getItem().getIconFromDamageForRenderPass(0, 1);
    }

    public boolean isOverrideIconAvailable(int side){
        return gates[side]!=null;
    }
}
