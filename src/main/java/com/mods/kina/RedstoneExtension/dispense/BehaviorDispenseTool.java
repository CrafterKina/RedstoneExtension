package com.mods.kina.RedstoneExtension.dispense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorDispenseTool implements IBehaviorDispenseItem{
    public ItemStack dispense(IBlockSource var1, ItemStack var2){
        World world = var1.getWorld();
        IPosition iposition = BlockDispenser.func_149939_a(var1);
        int x = (int) iposition.getX();
        int y = (int) iposition.getY();
        int z = (int) iposition.getZ();
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        if((var2.func_150998_b(block) || block.getMaterial().isToolNotRequired()) && !world.isAirBlock(x, y, z)){
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block));
            block.dropBlockAsItem(world, x, y, z, metadata, 0);
            world.setBlockToAir(x, y, z);
            var2.setItemDamage(var2.getItemDamage() + 1);
        }
        return var2;
    }
}
