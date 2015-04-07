package com.mods.kina.RedstoneExtension.base;

import com.mods.kina.RedstoneExtension.api.implementations.IGate;
import com.mods.kina.RedstoneExtension.api.implementations.IWrench;
import com.mods.kina.RedstoneExtension.renderer.block.EnumRenderType;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;

public abstract class BlockMachineBase extends BlockContainer{
    protected BlockMachineBase(){
        super(Material.rock);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float disX, float disY, float disZ){
        TileEntityMachineBase entity=(TileEntityMachineBase)world.getTileEntity(x,y,z);
        try{
            if(player.getHeldItem()!=null){
                if(player.getHeldItem().getItem() instanceof IGate){
                    player.getHeldItem().setTagCompound((NBTTagCompound) entity.onClickByGate(player.getHeldItem(), side).getTagCompound().copy());
                }else if(player.getHeldItem().getItem() instanceof IWrench){
                    player.getHeldItem().setTagCompound((NBTTagCompound) entity.onClickByWrench(player.getHeldItem(), side).getTagCompound().copy());
                }else if(player.getHeldItem().getItem() instanceof ItemDye){
                    player.getHeldItem().setTagCompound((NBTTagCompound) entity.onClickByDye(player.getHeldItem(), side).getTagCompound().copy());
                }else if(FluidContainerRegistry.isFilledContainer(player.getHeldItem())){
                    player.getHeldItem().setTagCompound((NBTTagCompound) entity.onClickByFilledContainer(player.getHeldItem(), side).getTagCompound().copy());
                }
            }
        }catch(NullPointerException e){
            player.getHeldItem().stackSize--;
        }
        onExtendBlockActivated(world, x, y, z, player, side, disX, disY, disZ);
        return true;
    }

    protected boolean onExtendBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float disX, float disY, float disZ){return false;}

    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_){
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 2, 2);
        }

        if (l == 1)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 5, 2);
        }

        if (l == 2)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 3, 2);
        }

        if (l == 3)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 4, 2);
        }
    }

    /**
     The type of render function that is called for this block
     */
    public int getRenderType(){
        return EnumRenderType.MachineBase.getType();
    }
}
