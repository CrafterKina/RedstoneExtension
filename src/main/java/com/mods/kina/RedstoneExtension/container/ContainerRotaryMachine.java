package com.mods.kina.RedstoneExtension.container;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import com.mods.kina.RedstoneExtension.slot.SlotOutput;
import com.mods.kina.RedstoneExtension.slot.SlotParts;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityRotaryMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRotaryMachine extends Container{
    private TileEntityRotaryMachine rotaryMachine=new TileEntityRotaryMachine();
    private int lastOperationTimes;
    private int lastIsOperating;
    private int lastCurrentOperations;

    public ContainerRotaryMachine(InventoryPlayer par1InventoryPlayer,TileEntityRotaryMachine machine){
        rotaryMachine=machine;
        int i;
        int j;
        addSlotToContainer(new SlotParts(machine, 0, 154, 6));
        addSlotToContainer(new Slot(machine, 1, 38, 35));
        addSlotToContainer(new Slot(machine, 2, 56, 35));
        addSlotToContainer(new SlotOutput(machine, 3, 112, 35));
        addSlotToContainer(new SlotOutput(machine, 4, 130, 35));
        addSlotToContainer(new SlotOutput(machine, 5, 130, 53));

        for(i = 0; i < 3; ++i){
            for(j = 0; j < 9; ++j){
                addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i){
            addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_){
        return rotaryMachine.isUseableByPlayer(p_75145_1_);
    }
    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, rotaryMachine.operations);
        p_75132_1_.sendProgressBarUpdate(this,1,!rotaryMachine.isOperating?0:1);
        p_75132_1_.sendProgressBarUpdate(this,2,rotaryMachine.currentNeedOperations);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for(Object crafter : this.crafters){
            ICrafting icrafting = (ICrafting) crafter;
            if(lastOperationTimes != rotaryMachine.operations){
                icrafting.sendProgressBarUpdate(this, 0, rotaryMachine.operations);
            }
            if(lastIsOperating!=(!rotaryMachine.isOperating?0:1)){
                icrafting.sendProgressBarUpdate(this,1,!rotaryMachine.isOperating?0:1);
            }if(lastCurrentOperations!=rotaryMachine.currentNeedOperations){
                icrafting.sendProgressBarUpdate(this,2,rotaryMachine.currentNeedOperations);
            }
        }

        this.lastOperationTimes = rotaryMachine.operations;
        lastIsOperating=!rotaryMachine.isOperating?0:1;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
        if (p_75137_1_ == 0)
        {
            rotaryMachine.operations = p_75137_2_;
        }else if(p_75137_1_==1){
            rotaryMachine.isOperating= p_75137_2_ != 0;
        }else if(p_75137_1_==2){
            rotaryMachine.currentNeedOperations=p_75137_2_;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex) {

        //クリックされたスロットを取得
        Slot slot = (Slot)this.inventorySlots.get(clickedIndex);
        if(slot == null) {
            return null;
        }

        if(!slot.getHasStack()) return null;

        //クリックされたスロットのItemStackを取得
        ItemStack itemStack = slot.getStack();

        //書き換えるた後比較したいので変更前のItemStackの状態を保持しておく
        ItemStack itemStackOrg = slot.getStack().copy();

        //素材スロットがクリックされたらインベントリかホットバーの空いてるスロットに移動
        if(sourceIndex <= clickedIndex && clickedIndex <= sourceIndex + sourceSize) {
            if (!this.mergeItemStack(itemStack, inventoryIndex, inventoryIndex + inventorySize + hotbarSize, false)) {
                return null;
            }

            slot.onSlotChange(itemStack, itemStackOrg);
        }
        // 結果スロットがクリックされたらインベントリかホットバーの空いてるスロットに移動
        else if(productIndex <= clickedIndex && clickedIndex < productIndex + productSize) {
            if (!this.mergeItemStack(itemStack, inventoryIndex, inventoryIndex + inventorySize + hotbarSize, false)) {
                return null;
            }

            slot.onSlotChange(itemStack, itemStackOrg);
        }
        //インベントリがクリックされた
        else if(inventoryIndex <= clickedIndex && clickedIndex < inventoryIndex + inventorySize) {
            if(rotaryMachine.getStackInSlot(0)!=null&&isSourceItem((IRotaryMachineParts)rotaryMachine.getStackInSlot(0).getItem(),itemStack)) {
                //素材アイテムなので素材スロットへ移動
                if (!this.mergeItemStack(itemStack, sourceIndex, sourceIndex + sourceSize, false)) {
                    return null;
                }
            }
            else if(itemStack.getItem()instanceof IRotaryMachineParts) {
                //燃料アイテムなので燃料スロットへ移動
                if (!this.mergeItemStack(itemStack, partIndex, partIndex + partSize, false)) {
                    return null;
                }
            }
            else {
                //どちらでもないのでホットバーに移動
                if (!this.mergeItemStack(itemStack, hotbarIndex, hotbarIndex + hotbarSize, false)) {
                    return null;
                }
            }
        }
        //ホットバーがクリックされた
        else if(hotbarIndex <= clickedIndex && clickedIndex < hotbarIndex + hotbarSize) {
            if(rotaryMachine.getStackInSlot(0)!=null&&isSourceItem((IRotaryMachineParts)rotaryMachine.getStackInSlot(0).getItem(),itemStack)) {
                //素材アイテムなので素材スロットへ移動
                if (!this.mergeItemStack(itemStack, sourceIndex, sourceIndex + sourceSize, false)) {
                    return null;
                }
            }
            else if(itemStack.getItem()instanceof IRotaryMachineParts) {
                //燃料アイテムなので燃料スロットへ移動
                if (!this.mergeItemStack(itemStack, partIndex, partIndex + partSize, false)) {
                    return null;
                }
            }
            else {
                //どちらでもないのでインベントリに移動
                if (!this.mergeItemStack(itemStack, inventoryIndex, inventoryIndex + inventorySize, false)) {
                    return null;
                }
            }
        }

        //シフトクリックで移動先スロットが溢れなかった場合は移動元スロットを空にする
        if (itemStack.stackSize == 0) {
            slot.putStack(null);
        }
        //移動先スロットが溢れた場合は数だけ変わって元スロットにアイテムが残るので更新通知
        else {
            slot.onSlotChanged();
        }

        //シフトクリック前後で数が変わらなかった＝移動失敗
        if (itemStack.stackSize == itemStackOrg.stackSize) {
            return null;
        }

        slot.onPickupFromSlot(par1EntityPlayer, itemStack);

        return itemStackOrg;
    }
    private boolean isSourceItem(IRotaryMachineParts parts,ItemStack stack){
        return parts.getRecipes().containsKey(stack.getItem());
    }

    static final int partSize=1;
    static final int sourceSize = 2;
    //static final int fuelSize = 3;
    static final int productSize = 3;
    static final int inventorySize = 27;
    static final int hotbarSize = 9;

    static final int partIndex=0;
    static final int sourceIndex = partIndex+partSize;
    static final int productIndex = sourceIndex + sourceSize;
    //static final int productIndex = fuelIndex + fuelSize;
    static final int inventoryIndex = productIndex + productSize;
    static final int hotbarIndex = inventoryIndex + inventorySize;
}
