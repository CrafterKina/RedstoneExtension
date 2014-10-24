/*
package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IReceivablePower;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityCrank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrank extends Block{
    public BlockCrank(){
        super(Material.rock);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_){
        TileEntity tile = world.getTileEntity(x,y,z);
        if (tile instanceof TileEntityCrank)
        {
            ((TileEntityCrank)tile).power();
        }
        return true;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z){
        return world.getTileEntity(x,y,z)!=null&&world.getTileEntity(x,y,z) instanceof IReceivablePower;
    }
}
*/
