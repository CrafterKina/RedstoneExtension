package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.RedstoneExtension.tileentity.TileEntityMoonlightDetector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMoonlightDetector extends BlockContainer{
    private IIcon[] field_149958_a = new IIcon[2];

    public BlockMoonlightDetector(){
        super(Material.wood);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("blockMoonlightDetector");
        setBlockTextureName("daylight_detector");
    }

    /**
     Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_){
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
    }

    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_){
        return p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);
    }

    /**
     Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_){
    }

    /**
     Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_){
    }

    /**
     Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_){
    }

    public void func_149957_e(World world, int x, int y, int z){
        if(!world.provider.hasNoSky){
            int l = world.getBlockMetadata(x, y, z);
            int i1;
            float b = world.getCurrentMoonPhaseFactor();
            long a = world.getWorldTime();
            if(world.isDaytime()){
                i1 = 0;
            }else{

                if(b == 1.0F){
                    i1 = 15;
                }else if(b == 0.75F){
                    i1 = 12;
                }else if(b == 0.5F){
                    i1 = 9;
                }else if(b == 0.25F){
                    i1 = 6;
                }else{
                    i1 = 3;
                }

            }
            //}
            if(l != i1){
                world.setBlockMetadataWithNotify(x, y, z, i1, 3);
            }
            //}
        }
    }

    /**
     If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock(){
        return false;
    }

    /**
     Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube(){
        return false;
    }

    /**
     Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower(){
        return true;
    }

    /**
     Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
        return new TileEntityMoonlightDetector();
    }

    /**
     Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_){
        return p_149691_1_ == 1 ? this.field_149958_a[0] : this.field_149958_a[1];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_){
        this.field_149958_a[0] = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.field_149958_a[1] = p_149651_1_.registerIcon(this.getTextureName() + "_side");
    }
}
