package com.mods.kina.RedstoneExtension.tileentity;

import com.mods.kina.RedstoneExtension.blocks.BlockTransportDropper;
import com.mods.kina.RedstoneExtension.renderer.BlockTransportDropperRender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityTransportDropper extends TileEntity implements IHopper{
    protected static short mode = 0;

    protected int transferCooldown = -1;

    public ItemStack[] hopperItemStacks = new ItemStack[1];
    public ItemStack viewItem;

    public NBTTagCompound stackTagCompound;

    //NBT
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        hopperItemStacks = new ItemStack[getSizeInventory()];

        transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");
        mode = par1NBTTagCompound.getShort("Mode");

        for(int i = 0; i < nbttaglist.tagCount(); ++i){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if(b0 >= 0 && b0 < hopperItemStacks.length){
                hopperItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        stackTagCompound = par1NBTTagCompound;

    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < hopperItemStacks.length; ++i){
            if(hopperItemStacks[i] != null){
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                hopperItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
        par1NBTTagCompound.setInteger("TransferCooldown", transferCooldown);
        par1NBTTagCompound.setShort("Mode", mode);
    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
        readFromNBT(pkt.func_148857_g());
    }

    //read
    public void setTransferCooldown(int par1){
        transferCooldown = par1;
    }

    public boolean isCoolingDown(){
        return transferCooldown > 0;
    }

    public short getMode(){
        return mode;
    }

    public void setMode(short par1){
        mode = par1;
    }

    public void setItems(ItemStack[] _items){
        if(_items != null){
            for(int i = 0; i < getSizeInventory(); ++i){
                if(_items[i] != null){
                    hopperItemStacks[i] = _items[i].copy();
                }else{
                    hopperItemStacks[i] = null;
                }
            }
        }
    }

    //write
    public ItemStack[] getItems(){

        return hopperItemStacks;
    }

    public int getCoolTime(){

        return transferCooldown;
    }


    //count cooldown
    public void updateEntity(){
        if(worldObj != null && !worldObj.isRemote){
            --transferCooldown;

            if(!isCoolingDown()){
                setTransferCooldown(0);
                updateHopper();
            }
        }
    }

    public boolean updateHopper(){
        if(!isCoolingDown()){
            boolean flag = /*suckItemsIntoHopper(this) ||*/ insertItemToInventory();

            if(flag){
                setTransferCooldown(4);
                markDirty();
                return true;
            }
        }

        return false;
    }

    //for hopper contents
    //RHopperから送り出す
    protected boolean insertItemToInventory(){
        //バニラホッパーの挙動
        IInventory iinventory = getOutputInventory();

        if(iinventory == null){
            return false;
        }else{
            for(int i = 0; i < getSizeInventory(); ++i)//バニラホッパーと同じ挙動
            {
                if(getStackInSlot(i) != null){
                    ItemStack itemstack = getStackInSlot(i).copy();
                    ItemStack itemstack1 = insertStack(iinventory, decrStackSize(i, 1), Facing.oppositeSide[BlockTransportDropperRender.getFace(getBlockMetadata())]);
                    if(itemstack1 == null || itemstack1.stackSize == 0){
                        iinventory.markDirty();
                        return true;
                    }
                    setInventorySlotContents(i, itemstack);
                }
            }
            return false;
        }
    }

    //インベントリから
    protected static boolean insertStackFromInventory(IHopper par0Hopper, IInventory par1IInventory, int par2, int par3){
        ItemStack itemstack = par1IInventory.getStackInSlot(par2);

        if(itemstack != null && canExtractItemFromInventory(par1IInventory, itemstack, par2, par3)){
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = insertStack(par0Hopper, par1IInventory.decrStackSize(par2, 1), -1);

            if(itemstack2 == null || itemstack2.stackSize == 0){
                par1IInventory.markDirty();
                return true;
            }

            par1IInventory.setInventorySlotContents(par2, itemstack1);
        }

        return false;
    }

    /**
     Inserts a stack into an inventory. Args: Inventory, stack, side. Returns leftover items.
     */
    public static ItemStack insertStack(IInventory par0IInventory, ItemStack par1ItemStack, int par2){
        if(par0IInventory instanceof ISidedInventory && par2 > -1){
            ISidedInventory isidedinventory = (ISidedInventory) par0IInventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(par2);

            for(int j = 0; j < aint.length && par1ItemStack != null && par1ItemStack.stackSize > 0; ++j){
                par1ItemStack = insertItemstack(par0IInventory, par1ItemStack, aint[j], par2);
            }
        }else{
            int k = par0IInventory.getSizeInventory();

            for(int l = 0; l < k && par1ItemStack != null && par1ItemStack.stackSize > 0; ++l){
                par1ItemStack = insertItemstack(par0IInventory, par1ItemStack, l, par2);
            }
        }

        if(par1ItemStack != null && par1ItemStack.stackSize == 0){
            par1ItemStack = null;
        }

        return par1ItemStack;
    }

    /**
     Args: inventory, item, slot, side
     */
    protected static boolean canInsertItemToInventory(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3){
        return par0IInventory.isItemValidForSlot(par2, par1ItemStack) && (!(par0IInventory instanceof ISidedInventory) || ((ISidedInventory) par0IInventory).canInsertItem(par2, par1ItemStack, par3));
    }

    //吸い込み対象のインベントリ。SidedInventoryの材料スロットやホッパー系からの搬入は禁止。
    protected static boolean canExtractItemFromInventory(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3){
        if(par0IInventory instanceof ISidedInventory){
            return ((ISidedInventory) par0IInventory).canExtractItem(par2, par1ItemStack, par3);
        }else{
            return !(par0IInventory instanceof TileEntityTransportDropper || par0IInventory instanceof TileEntityHopper);
        }
    }

    private static ItemStack insertItemstack(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3){
        ItemStack itemstack1 = par0IInventory.getStackInSlot(par2);

        if(canInsertItemToInventory(par0IInventory, par1ItemStack, par2, par3)){
            boolean flag = false;

            if(itemstack1 == null){
                par0IInventory.setInventorySlotContents(par2, par1ItemStack);
                par1ItemStack = null;
                flag = true;
            }else if(areItemStacksEqualItem(itemstack1, par1ItemStack)){
                int k = par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                int l = Math.min(par1ItemStack.stackSize, k);
                par1ItemStack.stackSize -= l;
                itemstack1.stackSize += l;
                flag = l > 0;
            }

            if(flag){
                if(par0IInventory instanceof TileEntityTransportDropper){
                    ((TileEntityTransportDropper) par0IInventory).setTransferCooldown(4);
                    par0IInventory.markDirty();
                }

                par0IInventory.markDirty();
            }
        }

        return par1ItemStack;
    }

    //搬出先インベントリ
    protected IInventory getOutputInventory(){
        int i = BlockTransportDropperRender.getFace(getBlockMetadata());
        return getInventoryAtLocation(getWorldObj(), (double) (xCoord + Facing.offsetsXForSide[i]), (double) (yCoord + Facing.offsetsYForSide[i]), (double) (zCoord + Facing.offsetsZForSide[i]));
    }

    //1ブロック下のインベントリ取得
    public static IInventory getInventoryAboveHopper(IHopper par0Hopper){
        return getInventoryAtLocation(par0Hopper.getWorldObj(), par0Hopper.getXPos(), par0Hopper.getYPos() - 1.0D, par0Hopper.getZPos());
    }

    /**
     Gets an inventory at the given location to extract items into or take items from. Can find either a tile entity or
     regular entity implementing IInventory.
     */
    public static IInventory getInventoryAtLocation(World par0World, double par1, double par3, double par5){
        IInventory iinventory = null;
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        TileEntity tileentity = par0World.getTileEntity(i, j, k);

        if(tileentity != null && tileentity instanceof IInventory){
            iinventory = (IInventory) tileentity;

            if(iinventory instanceof TileEntityChest){
                Block block = par0World.getBlock(i, j, k);

                if(block instanceof BlockChest){
                    iinventory = ((BlockChest) block).func_149951_m(par0World, i, j, k);
                }
            }
        }

        if(iinventory == null){
            List list = par0World.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D), IEntitySelector.selectInventories);

            if(list != null && list.size() > 0){
                iinventory = (IInventory) list.get(par0World.rand.nextInt(list.size()));
            }
        }

        return iinventory;
    }

    protected static boolean areItemStacksEqualItem(ItemStack par0ItemStack, ItemStack par1ItemStack){
        return par0ItemStack.isItemEqual(par1ItemStack) && par0ItemStack.stackSize <= par0ItemStack.getMaxStackSize() && ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack);
    }

    //Inventory
    @Override
    public int getSizeInventory(){
        return hopperItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i){
        if(i < getSizeInventory()){
            return hopperItemStacks[i];
        }else return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j){
        if(hopperItemStacks[i] != null){
            ItemStack itemstack;
            if(hopperItemStacks[i].stackSize <= j){
                itemstack = hopperItemStacks[i].copy();
                hopperItemStacks[i].stackSize = 0;
                hopperItemStacks[i] = null;
                return itemstack;
            }else{
                itemstack = hopperItemStacks[i].splitStack(j);

                if(hopperItemStacks[i].stackSize == 0){
                    hopperItemStacks[i] = null;
                }

                return itemstack;
            }
        }else{
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i){
        if(hopperItemStacks[i] != null){
            ItemStack itemstack = hopperItemStacks[i];
            hopperItemStacks[i] = null;
            return itemstack;
        }else{
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack){
        if(i < 5){
            hopperItemStacks[i] = itemstack;
            if(itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
                itemstack.stackSize = getInventoryStackLimit();
            }
        }
    }

    /**
     Returns the name of the inventory
     */
    public String getInventoryName(){
        return new BlockTransportDropper().getLocalizedName();
    }

    /**
     Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
        return new BlockTransportDropper().getUnlocalizedName() != null;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer){
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory(){
    }

    public void closeInventory(){
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack){
        return true;
    }

    /**
     Gets the world X position for this hopper entity.
     */
    public double getXPos(){
        return (double) xCoord;
    }

    /**
     Gets the world Y position for this hopper entity.
     */
    public double getYPos(){
        return (double) yCoord;
    }

    /**
     Gets the world Z position for this hopper entity.
     */
    public double getZPos(){
        return (double) zCoord;
    }

    @Override
    public World getWorldObj(){
        return worldObj;
    }

    @Override
    public void markDirty(){
        super.markDirty();
        @SuppressWarnings("unchecked") List<EntityPlayer> list = worldObj.playerEntities;
        for(EntityPlayer player : list)
            if(player instanceof EntityPlayerMP)
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(getDescriptionPacket());
    }
}
