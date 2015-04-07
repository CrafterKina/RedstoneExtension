package com.mods.kina.RedstoneExtension.recipe;

import com.mods.kina.RedstoneExtension.power.VoltageAndCountContainer;
import net.minecraft.item.ItemStack;

public class RotaryMachineRecipeContainer{
    private ItemStack[] input;
    private ItemStack[] output;
    private RareOutputStackContainer[] rare;
    private VoltageAndCountContainer powerAmount;
    private boolean isInside;

    /**
     @param inputStacks 材料。isInsideがfalseなら複数種類可。
     @param outputStacks 完成品。isInsideがtrueなら複数種類可。
     @param rareStacks 確率のからむ完成品。isInsideがtrueなら複数選択可。
     @param needPower 必要エネルギー。(CC/tick)
     @param isInsideSlot isInside
     */
    public RotaryMachineRecipeContainer(ItemStack[] inputStacks,ItemStack[] outputStacks,RareOutputStackContainer[] rareStacks,VoltageAndCountContainer needPower,boolean isInsideSlot){
        input=inputStacks;
        output=outputStacks;
        rare=rareStacks;
        powerAmount=needPower;
        isInside=isInsideSlot;
    }

    public ItemStack[] getInput(){
        return input;
    }

    public ItemStack[] getOutput(){
        return output;
    }

    public RareOutputStackContainer[] getRare(){
        return rare;
    }

    public VoltageAndCountContainer getPowerAmount(){
        return powerAmount;
    }

    public boolean isInside(){
        return isInside;
    }
}
