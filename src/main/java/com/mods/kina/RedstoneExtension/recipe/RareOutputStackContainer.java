package com.mods.kina.RedstoneExtension.recipe;

import net.minecraft.item.ItemStack;

public class RareOutputStackContainer{
    private ItemStack rare;
    private int per;
    public RareOutputStackContainer(ItemStack rareOutput,int percentage){
        rare=rareOutput;
        per=percentage;
    }

    public ItemStack getRare(){
        return rare;
    }

    public int getPercentage(){
        return per;
    }
}
