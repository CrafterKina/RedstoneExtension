package com.mods.kina.RedstoneExtension.item.gate;

import com.mods.kina.KinaCore.toExtends.ItemChangeColorPartiallyFromDye;
import com.mods.kina.RedstoneExtension.api.implementations.IGate;
import net.minecraft.creativetab.CreativeTabs;

public class ItemGatePowerIn extends ItemChangeColorPartiallyFromDye implements IGate{
    public ItemGatePowerIn(){
        super("potion_overlay");
        setTextureName("potion_bottle_drinkable");
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public String getGateRole(){
        return "IPower";
    }
}