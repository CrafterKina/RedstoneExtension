package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import com.mods.kina.RedstoneExtension.api.implementations.tiles.IUseRightClickToSet;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityRotaryMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRotaryMachine extends BlockContainer{
    public boolean isPowering=false;
    public BlockRotaryMachine(){
        super(Material.rock);
        setBlockName("blockRotaryMachine");
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int p_149915_2_){
        return new TileEntityRotaryMachine();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz){
        if(player.getHeldItem()!=null&&player.getHeldItem().getItem()instanceof IUseRightClickToSet){
            IUseRightClickToSet item=(IUseRightClickToSet)player.getHeldItem().getItem();
            item.onRightClickToMachine(new BlockSourceImpl(world, x, y, z));
        }else if(player.getHeldItem()!=null&&player.getHeldItem().getItem()instanceof IRotaryMachineParts){
            ((TileEntityRotaryMachine)world.getTileEntity(x,y,z)).setInventorySlotContents(0,new ItemStack(player.getHeldItem().getItem()));
            player.inventory.getCurrentItem().stackSize--;
        }else if(!world.isRemote){
            player.openGui(RedstoneExtensionCore.core, 2, world, x, y, z);
        }
        return true;
    }

    /**
     Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_){
        TileEntity te=world.getTileEntity(x,y,z);
        if(te instanceof TileEntityRotaryMachine){
            TileEntityRotaryMachine rotaryMachine=(TileEntityRotaryMachine)te;
            if(isPowering&&!isIndirectlyPowered(world, x, y, z)){
                isPowering=false;
            }else if(!isPowering&&isIndirectlyPowered(world, x, y, z)){
                isPowering=true;
                rotaryMachine.power();
            }
        }
    }

    public static boolean isIndirectlyPowered(World world, int x, int y, int z){
        return (world.getIndirectPowerOutput(x, y - 1, z, 0)) || (world.getIndirectPowerOutput(x, y + 1, z, 1) || (world.getIndirectPowerOutput(x, y, z - 1, 2) || (world.getIndirectPowerOutput(x, y, z + 1, 3) || (world.getIndirectPowerOutput(x + 1, y, z, 5) || (world.getIndirectPowerOutput(x - 1, y, z, 4) || (world.getIndirectPowerOutput(x, y, z, 0) || (world.getIndirectPowerOutput(x, y + 2, z, 1) || (world.getIndirectPowerOutput(x, y + 1, z - 1, 2) || (world.getIndirectPowerOutput(x, y + 1, z + 1, 3) || (world.getIndirectPowerOutput(x - 1, y + 1, z, 4) || world.getIndirectPowerOutput(x + 1, y + 1, z, 5)))))))))));
    }
}
