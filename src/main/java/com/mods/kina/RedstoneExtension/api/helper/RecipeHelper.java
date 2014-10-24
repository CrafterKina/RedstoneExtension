package com.mods.kina.RedstoneExtension.api.helper;

import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHelper{

    public static void addOreRecipeI(IRotaryMachineParts parts, String input, ItemStack output, int times){
        for(ItemStack itemStack : OreDictionary.getOres(input)){
            parts.addRecipe(getNonStackItemStack(itemStack), output, times);
        }
    }

    public static void addOreRecipeO(IRotaryMachineParts parts, ItemStack input, String output, int stackSize, int damage, int times){
        for(ItemStack itemStack : OreDictionary.getOres(output)){
            itemStack.stackSize = stackSize;
            itemStack.setItemDamage(damage);
            parts.addRecipe(getNonStackItemStack(input), itemStack, times);
        }
    }

    public static void addOreRecipeWithoutSizeO(IRotaryMachineParts parts, ItemStack input, String output, int damage, int times){
        addOreRecipeO(parts, getNonStackItemStack(input), output, 1, damage, times);
    }

    public static void addOreRecipeWithoutDamageO(IRotaryMachineParts parts, ItemStack input, String output, int stackSize, int times){
        addOreRecipeO(parts, getNonStackItemStack(input), output, stackSize, 0, times);
    }

    public static void addOreRecipeNoOptionO(IRotaryMachineParts parts, ItemStack input, String output, int times){
        addOreRecipeO(parts, getNonStackItemStack(input), output, 1, 0, times);
    }

    public static void addOreRecipeIO(IRotaryMachineParts parts, String input, String output, int stackSize, int damage, int times){
        for(ItemStack inStack : OreDictionary.getOres(input)){
            Item inItem = inStack.getItem();
            for(ItemStack outStack : OreDictionary.getOres(output)){
                outStack.stackSize = stackSize;
                outStack.setItemDamage(damage);
                parts.addRecipe(getNonStackItemStack(inStack), outStack, times);
            }
        }
    }

    public static void addOreRecipeWithoutSizeIO(IRotaryMachineParts parts, String input, String output, int damage, int times){
        addOreRecipeIO(parts, input, output, 1, damage, times);
    }

    public static void addOreRecipeWithoutDamageIO(IRotaryMachineParts parts, String input, String output, int stackSize, int times){
        addOreRecipeIO(parts, input, output, stackSize, 0, times);
    }

    public static void addOreRecipeNoOptionIO(IRotaryMachineParts parts, String input, String output, int times){
        addOreRecipeIO(parts, input, output, 1, 0, times);
    }

    public static void addOreRareRecipeNoOptionI(IRotaryMachineParts parts, String input, ItemStack rare, float rarity){
        for(ItemStack itemStack : OreDictionary.getOres(input)){
            Item item = itemStack.getItem();
            parts.addRareRecipe(getNonStackItemStack(itemStack), rare, rarity);
        }
    }

    private static ItemStack getNonStackItemStack(ItemStack stack){
        return new ItemStack(stack.getItem(),1,stack.getItemDamage());
    }
}
