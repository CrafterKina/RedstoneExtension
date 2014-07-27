package com.mods.kina.RedstoneExtension.dispense;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BehaviorSimpleDispenseBlock implements IBehaviorDispenseItem{
    public ItemStack dispense(IBlockSource var1, ItemStack var2){
        World world = var1.getWorld();
        Block block = Block.getBlockFromItem(var2.getItem());
        IPosition iposition = BlockDispenser.func_149939_a(var1);
        int x = (int) iposition.getX();
        int y = (int) iposition.getY();
        int z = (int) iposition.getZ();
        if(world.isAirBlock(x, y, z)){
            Minecraft.getMinecraft().getSoundHandler().playSound((new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F)));
            world.setBlock(x, y, z, block);
            return var2.splitStack(var2.stackSize - 1);
        }else{
            return var2;
        }
    }
}
