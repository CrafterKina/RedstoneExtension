package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.RedstoneExtension.blocks.BlockMoonlightDetector;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMoonlightDetector extends TileEntity{
    public void updateEntity(){
        if(this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L){
            this.blockType = this.getBlockType();

            if(this.blockType instanceof BlockMoonlightDetector){
                ((BlockMoonlightDetector) this.blockType).func_149957_e(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }
}