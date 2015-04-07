package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.RedstoneExtension.base.TileEntityMachineBase;
import com.mods.kina.RedstoneExtension.list.EnumREBlock;

public class TileEntityRotaryMachine extends TileEntityMachineBase{



    /**
     Returns the name of the inventory
     */
    public String getInventoryName(){
        return EnumREBlock.blockRotaryMachine.getBlock().getLocalizedName();
    }

    /**
     Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
        return getInventoryName()!=null&&!getInventoryName().isEmpty();
    }
}
