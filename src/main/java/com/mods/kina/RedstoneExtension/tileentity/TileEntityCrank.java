/*
package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.KinaCore.toExtends.IInventoryImpl;
import com.mods.kina.RedstoneExtension.api.implementations.tiles.IReceivablePower;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCrank extends IInventoryImpl{
    public float visibleRotation = 0.0F;
    public int charge = 0;

    public int hits = 0;
    public int rotation = 0;

    public void power(){
        if(!worldObj.isRemote&&rotation<3){
            TileEntity tileEntity=worldObj.getTileEntity(xCoord,yCoord,zCoord);
            if(tileEntity!=null&&tileEntity instanceof IReceivablePower){
                IReceivablePower rotary=(IReceivablePower)tileEntity;
                if(rotary.canReceivePower()){
                    hits=0;
                    rotation+=18;
                }else {
                    hits+=1;
                    if(hits>10){
                        worldObj.func_147480_a(xCoord,yCoord,zCoord,false);
                    }
                }
            }
        }
    }

    */
/**
     Returns the name of the inventory
     *//*

    public String getInventoryName(){
        return null;
    }
}
*/
