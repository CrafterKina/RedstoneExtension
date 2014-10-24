package com.mods.kina.RedstoneExtension.api.implementations.tiles;

import net.minecraft.item.ItemStack;

import java.util.Map;

@Deprecated
public interface IRotaryMachineParts{
    /*
    * par1:Item:inputItem
    * par2:ItemStack,int:outputItem,times
    */
    Map<ItemStack,Object[]> getRecipes();
    void addRecipe(ItemStack input, ItemStack output, int times);
    /*
    * par1:Item:inputItem
    * par2:ItemStack,float:outputItem,Rarity
    */
    Map<ItemStack,Object[]> getRareRecipes();
    void addRareRecipe(ItemStack input, ItemStack output, float rarity);
}
