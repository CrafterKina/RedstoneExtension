package com.mods.kina.RedstoneExtension.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDownwardPressurePlate extends BlockBasePressurePlate{
    private BlockPressurePlate.Sensitivity field_150069_a;

    public BlockDownwardPressurePlate(){
        super("stone", Material.rock);
        field_150069_a = BlockPressurePlate.Sensitivity.mobs;
    }

    protected int func_150066_d(int p_150066_1_){
        return p_150066_1_ > 0 ? 1 : 0;
    }

    protected int func_150060_c(int p_150060_1_){
        return p_150060_1_ == 1 ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return p_149748_5_ == 0 ? this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_) : 0;
    }

    protected int func_150065_e(World p_150065_1_, int p_150065_2_, int p_150065_3_, int p_150065_4_){
        List list = null;

        if(this.field_150069_a == BlockPressurePlate.Sensitivity.everything){
            list = p_150065_1_.getEntitiesWithinAABBExcludingEntity(null, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if(this.field_150069_a == BlockPressurePlate.Sensitivity.mobs){
            list = p_150065_1_.getEntitiesWithinAABB(EntityLivingBase.class, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if(this.field_150069_a == BlockPressurePlate.Sensitivity.players){
            list = p_150065_1_.getEntitiesWithinAABB(EntityPlayer.class, this.func_150061_a(p_150065_2_, p_150065_3_, p_150065_4_));
        }

        if(list != null && !list.isEmpty()){

            for(Object aList : list){
                Entity entity = (Entity) aList;

                if(!entity.doesEntityNotTriggerPressurePlate()){
                    return 15;
                }
            }
        }

        return 0;
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_){
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ + 1, p_149742_4_) || BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_, p_149742_3_ + 1, p_149742_4_));
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_){
        boolean flag = false;

        if(!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ + 1, p_149695_4_) && !BlockFence.func_149825_a(p_149695_1_.getBlock(p_149695_2_, p_149695_3_ + 1, p_149695_4_))){
            flag = true;
        }

        if(flag){
            this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    public void setBlockBoundsForItemRender(){
        float f = 0.5F;
        float f1 = 0.125F;
        float f2 = 0.5F;
        this.setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
        //this.setBlockBounds(0.5F + f, 0.5F + f1, 0.5F + f2, 0.5F - f, 0.5F - f1, 0.5F - f2);
    }

    protected void func_150063_b(int p_150063_1_){
        boolean flag = this.func_150060_c(p_150063_1_) > 0;
        float f = 0.0625F;

        if(flag){
            //this.setBlockBounds(f, 0.0F, f, 1.0F - f, f/2, 1.0F - f);
            setBlockBounds(f, 1.0F - f/2, f, 1.0F - f, 1.0F, 1.0F - f);
        }else{
            //this.setBlockBounds(f, 0.0F, f, 1.0F - f, f, 1.0F - f);
            setBlockBounds(f, 1.0F - f, f, 1.0F - f, 1.0F, 1.0F - f);
        }
    }
    //todo func_150061_a
}
