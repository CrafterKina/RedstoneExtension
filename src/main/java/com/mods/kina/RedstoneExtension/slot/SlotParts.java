package com.mods.kina.RedstoneExtension.slot;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotParts extends Slot{
    public SlotParts(IInventory inventory, int id, int x, int y){
        super(inventory, id, x, y);
    }
    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemStack)
    {
        return itemStack.getItem()instanceof IRotaryMachineParts;
    }
}
