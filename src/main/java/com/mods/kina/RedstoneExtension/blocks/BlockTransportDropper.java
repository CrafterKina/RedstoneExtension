package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.KinaCore.toExtends.BlockBreakableContainer;
import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.renderer.block.EnumRenderType;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTransportDropper extends BlockBreakableContainer{
    @SideOnly(Side.CLIENT)
        private IIcon face;
        private IIcon face2;
    public BlockTransportDropper(){
        super(Material.rock);
        setBlockName("blockTransportDropper");
        setHardness(1);
        setLightLevel(15f);
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister p){
       face=p.registerIcon("glass");
       face2=Blocks.dispenser.getIcon(0,0);
   }
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149673_2_, int p_149673_3_){
        Random r=new Random();
        return r.nextInt(6)!=1?face:face2;
    }

    public int getRenderType()
    {
        return EnumRenderType.TransportDropper.getType();
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean renderAsNormalBlock()
    {
        return true;
    }

    public int tickRate(World world){
        return 4;
    }

    public TileEntity createNewTileEntity(World var1, int var2){
        return new TileEntityTransportDropper();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz){
        if(!world.isRemote){
            player.openGui(RedstoneExtensionCore.core, 1, world, x, y, z);
        }
        return true;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack){
        int l = BlockPistonBase.determineOrientation(world, x, y, z, entity);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
        if(itemstack.hasDisplayName()){
            ((TileEntityBlockDispenser) world.getTileEntity(x, y, z)).func_146018_a(itemstack.getDisplayName());
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int par6){
        Random rand = new Random();
        TileEntityTransportDropper tileentitydispenser = (TileEntityTransportDropper) world.getTileEntity(x, y, z);

        if(tileentitydispenser != null){
            for(int i1 = 0; i1 < tileentitydispenser.getSizeInventory(); ++i1){
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i1);

                if(itemstack != null){
                    float f = rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = rand.nextFloat() * 0.8F + 0.1F;

                    while(itemstack.stackSize > 0){
                        int j1 = rand.nextInt(21) + 10;

                        if(j1 > itemstack.stackSize){
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if(itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, par6);
    }
}
