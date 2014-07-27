package com.mods.kina.RedstoneExtension.blocks;

import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.dispense.BehaviorDispenseBlock;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBlockDispenser extends BlockContainer{
    private static int i = 0;
    private final IBehaviorDispenseItem behaviorDispenseBlock = new BehaviorDispenseBlock();
    protected Random rand = new Random();
    @SideOnly(Side.CLIENT)
    protected IIcon top,face,face2;

    public BlockBlockDispenser(){
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("blockBlockDispenser");
        setBlockTextureName("kina:block_blockdispenser");
    }

    public static int getI(){
        return i;
    }

    public static void setI(int l){
        i = l;
    }

    public static int getDispenserOrientation(int p_150076_0_){
        return p_150076_0_ & 7;
    }

    public static IPosition func_149939_a(IBlockSource blockSource){
        EnumFacing enumfacing = func_149937_b(blockSource.getBlockMetadata());
        double d0 = blockSource.getX() + 0.7D * (double) enumfacing.getFrontOffsetX();
        double d1 = blockSource.getY() + 0.7D * (double) enumfacing.getFrontOffsetY();
        double d2 = blockSource.getZ() + 0.7D * (double) enumfacing.getFrontOffsetZ();
        return new PositionImpl(d0, d1, d2);
    }

    public static EnumFacing func_149937_b(int metadata){
        return EnumFacing.getFront(metadata & 7);
    }

    /**
     How many world ticks before ticking
     */
    public int tickRate(World p_149738_1_){
        return 4;
    }

    /**
     Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z){
        super.onBlockAdded(world, x, y, z);
        this.func_149938_m(world, x, y, z);
    }

    private void func_149938_m(World world, int x, int y, int z){
        if(!world.isRemote){
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if(block.func_149730_j() && !block1.func_149730_j()){
                b0 = 3;
            }

            if(block1.func_149730_j() && !block.func_149730_j()){
                b0 = 2;
            }

            if(block2.func_149730_j() && !block3.func_149730_j()){
                b0 = 5;
            }

            if(block3.func_149730_j() && !block2.func_149730_j()){
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }

    /**
     Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int meta, int side){
        int k = getDispenserOrientation(side);
        return meta == k ? (k != 1 && k != 0 ? face : face2) : (k != 1 && k != 0 ? (meta != 1 && meta != 0 ? this.blockIcon : top) : top);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_){
        this.blockIcon = p_149651_1_.registerIcon(getTextureName()+"_side");
        this.top = p_149651_1_.registerIcon(getTextureName()+"_top");
        this.face = p_149651_1_.registerIcon(getTextureName() + "_front_horizontal");
        this.face2 = p_149651_1_.registerIcon(getTextureName() + "_front_vertical");
    }

    /**
     Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz){
        if(!world.isRemote){
            player.openGui(RedstoneExtensionCore.core, 0, world, x, y, z);
        }
        return true;
    }

    protected void func_149941_e(World world, int dx, int dy, int dz){
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, dx, dy, dz);
        TileEntityBlockDispenser tileentitydispenser = (TileEntityBlockDispenser) blocksourceimpl.getBlockTileEntity();
        if(tileentitydispenser != null){
            IPosition position = func_149939_a(blocksourceimpl);
            int x = (int) position.getX();
            int y = (int) position.getY();
            int z = (int) position.getZ();
            Block block = world.getBlock(x, y, z);
            if(tileentitydispenser.getStackInSlot(i) == null){
                if(world.isAirBlock(x, y, z)){
                    world.playAuxSFX(1001, dx, dy, dz, 0);
                    if(i <= 7) i += 1;
                    else i = 0;
                }else if(block.getBlockHardness(world, x, y, z) >= 0){
                    breakBlock(blocksourceimpl);
                }
            }else{
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                IBehaviorDispenseItem ibehaviordispenseitem = this.func_149940_a(itemstack);
                if(ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider){
                    if(world.isAirBlock(x, y, z)){
                        ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
                        tileentitydispenser.setInventorySlotContents(i, itemstack1.stackSize == 0 ? null : itemstack1);
                        if(i <= 7) i += 1;
                        else i = 0;
                    }else if(block.getBlockHardness(world, x, y, z) >= 0){
                        breakBlock(blocksourceimpl);
                    }
                }
            }
        }
    }

    private void breakBlock(BlockSourceImpl blocksourceimpl){
        int a = 0;
        boolean b = false;
        TileEntityBlockDispenser tileentitydispenser = (TileEntityBlockDispenser) blocksourceimpl.getBlockTileEntity();
        IPosition position = func_149939_a(blocksourceimpl);
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();
        World world = blocksourceimpl.getWorld();
        Block block = world.getBlock(x, y, z);
        for(int j = 0; j < tileentitydispenser.getSizeInventory(); j++){
            ItemStack stack = tileentitydispenser.getStackInSlot(j);
            if(stack == null){
                for(int k = j; k < tileentitydispenser.getSizeInventory(); ++k){
                    ItemStack stack2 = tileentitydispenser.getStackInSlot(k);
                    if(stack2 != null && stack2.getItem().equals(Item.getItemFromBlock(block)) && getStackSize(stack2) < stack2.getMaxStackSize()){
                        a = getStackSize(stack2) + 1;
                        b = true;
                        j = k;
                        break;
                    }else{
                        a = 1;
                        b = true;
                    }
                }
            }else if(stack.getItem().equals(Item.getItemFromBlock(block)) && getStackSize(stack) < stack.getMaxStackSize()){
                a = getStackSize(stack) + 1;
                b = true;
            }
            if(b){
                tileentitydispenser.setInventorySlotContents(j, new ItemStack(block, a, world.getBlockMetadata(x, y, z)));
                break;
            }
        }
        if(!b){
            block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        }
        world.playAuxSFX(2001, x, y, z, getMetadataBlock(block, world, x, y, z));
        world.setBlockToAir(x, y, z);
    }

    private int getStackSize(ItemStack stack){
        return Integer.valueOf(stack.toString().split("x")[0]);
    }

    private int getMetadataBlock(Block block, World world, int x, int y, int z){
        int a = Block.getIdFromBlock(block);
        int b = world.getBlockMetadata(x, y, z);
        int c = b << 12;
        return a + c;
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_){
        return behaviorDispenseBlock;
    }

    /**
     Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_){
        int l = world.getBlockMetadata(x, y, z);
        boolean flag1 = (l & 8) != 0;
        int l1 = getDispenserOrientation(l);
        if(l1 != 7){
            boolean flag = isIndirectlyPowered(world, x, y, z, l1);
            if(flag && !flag1){
                world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
                world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
            }else if(!flag && flag1){
                world.setBlockMetadataWithNotify(x, y, z, l & -9, 4);
            }
        }
    }

    private boolean isIndirectlyPowered(World world, int x, int y, int z, int par5){
        return (par5 != 0 && world.getIndirectPowerOutput(x, y - 1, z, 0)) || (par5 != 1 && world.getIndirectPowerOutput(x, y + 1, z, 1) || (par5 != 2 && world.getIndirectPowerOutput(x, y, z - 1, 2) || (par5 != 3 && world.getIndirectPowerOutput(x, y, z + 1, 3) || (par5 != 5 && world.getIndirectPowerOutput(x + 1, y, z, 5) || (par5 != 4 && world.getIndirectPowerOutput(x - 1, y, z, 4) || (world.getIndirectPowerOutput(x, y, z, 0) || (world.getIndirectPowerOutput(x, y + 2, z, 1) || (world.getIndirectPowerOutput(x, y + 1, z - 1, 2) || (world.getIndirectPowerOutput(x, y + 1, z + 1, 3) || (world.getIndirectPowerOutput(x - 1, y + 1, z, 4) || world.getIndirectPowerOutput(x + 1, y + 1, z, 5)))))))))));
    }

    /**
     Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random p_149674_5_){
        if(!world.isRemote){
            this.func_149941_e(world, x, y, z);
        }
    }

    /**
     Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
        return new TileEntityBlockDispenser();
    }

    /**
     Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack){
        int l = BlockPistonBase.determineOrientation(world, x, y, z, entity);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if(itemstack.hasDisplayName()){
            ((TileEntityBlockDispenser) world.getTileEntity(x, y, z)).func_146018_a(itemstack.getDisplayName());
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int par6){
        TileEntityBlockDispenser tileentitydispenser = (TileEntityBlockDispenser) world.getTileEntity(x, y, z);

        if(tileentitydispenser != null){
            for(int i1 = 0; i1 < tileentitydispenser.getSizeInventory(); ++i1){
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i1);

                if(itemstack != null){
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while(itemstack.stackSize > 0){
                        int j1 = this.rand.nextInt(21) + 10;

                        if(j1 > itemstack.stackSize){
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if(itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, par6);
    }

    /**
     If this returns true, then comparators facing away from this block will use the value from
     getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride(){
        return true;
    }

    /**
     If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_){
        return Container.calcRedstoneFromInventory((IInventory) p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
    }
}
