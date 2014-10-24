package com.mods.kina.RedstoneExtension.link.amt;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartAMTProcessor extends Item implements IRotaryMachineParts{
    Map<ItemStack,Object[]> map = new HashMap<ItemStack,Object[]>();
    Map<ItemStack,Object[]> rmap=new HashMap<ItemStack,Object[]>();
    public PartAMTProcessor(){
        setUnlocalizedName("partAMTProcessor");
        setCreativeTab(CreativeTabs.tabTools);
    }

    public Map<ItemStack,Object[]> getRecipes(){
        return map;
    }

    public void addRecipe(ItemStack input, ItemStack output, int times){
        map.put(input, new Object[]{output, times});
    }

    public Map<ItemStack,Object[]> getRareRecipes(){
        return rmap;
    }

    public void addRareRecipe(ItemStack input, ItemStack output, float rarity){
        rmap.put(input,new Object[]{output,1f});
    }
}
