package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.RedstoneExtension.base.BlockMachineBase;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityRotaryMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRotaryMachine extends BlockMachineBase{
    public BlockRotaryMachine(){
        super();
    }

    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
        return new TileEntityRotaryMachine();
    }
}
