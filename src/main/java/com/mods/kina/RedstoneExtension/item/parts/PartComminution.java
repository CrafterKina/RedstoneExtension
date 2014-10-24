package com.mods.kina.RedstoneExtension.item.parts;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartComminution extends Item implements IRotaryMachineParts{
    Map<ItemStack,Object[]> normalRecipes=new HashMap<ItemStack,Object[]>();
    Map<ItemStack,Object[]> rareRecipes=new HashMap<ItemStack,Object[]>();
    public PartComminution(){
        setUnlocalizedName("partComminution");
        setCreativeTab(CreativeTabs.tabTools);
        addRecipe(new ItemStack(Items.diamond),new ItemStack(Blocks.dirt),10);
    }
    public Map<ItemStack,Object[]> getRecipes(){
        return normalRecipes;
    }

    public void addRecipe(ItemStack input, ItemStack output,int times){
        normalRecipes.put(input,new Object[]{output,times});
    }

    public Map<ItemStack,Object[]> getRareRecipes(){
        return rareRecipes;
    }

    public void addRareRecipe(ItemStack input, ItemStack output, float rarity){
        rareRecipes.put(input,new Object[]{output,rarity});
    }
}
