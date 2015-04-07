package com.mods.kina.RedstoneExtension.registry;

import com.mods.kina.RedstoneExtension.item.gate.ItemGatePowerIn;
import com.mods.kina.RedstoneExtension.list.EnumREBlock;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityMoonlightDetector;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityRotaryMachine;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static com.mods.kina.RedstoneExtension.list.EnumREBlock.*;

public class RERegistrarCore{
    public RERegistrarCore(){super();}
    public void registryBlock(){
        /*itemPartComminution=new PartComminution();
        itemPartAMTProcessor=new PartAMTProcessor();*/
        for(EnumREBlock enumBlock: EnumREBlock.values()){
            GameRegistry.registerBlock(enumBlock.getBlock(),enumBlock.name());
        }
        GameRegistry.registerItem(new ItemGatePowerIn(),"itemGate");
        /*GameRegistry.registerItem(itemPartComminution,"itemPartComminution");
        GameRegistry.registerItem(itemPartAMTProcessor,"itemPartAMTProcessor");*/
    }

    public void registryTile(){
        GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "TileEntityBlockDispenser");
        GameRegistry.registerTileEntity(TileEntityMoonlightDetector.class, "TileEntityMoonlightDetector");
        //GameRegistry.registerTileEntity(TileEntityCrank.class,"");
        //GameRegistry.registerTileEntity(TileEntityTransportDropper.class,"TileEntityTransportDropper");
        GameRegistry.registerTileEntity(TileEntityRotaryMachine.class,"TileEntityRotaryMachine");
    }

    public void registryRecipe(){
        GameRegistry.addRecipe(new ItemStack(blockMoonlightDetector.getBlock()), "XXX", "HDH", "HRH", 'X', Blocks.obsidian, 'H', Blocks.end_stone, 'D', Blocks.daylight_detector, 'R', Blocks.redstone_torch);
        GameRegistry.addRecipe(new ItemStack(blockBlockDispenser.getBlock()),"SSS","S S","SDS",'S',Blocks.mossy_cobblestone,'D',Blocks.dispenser);
        GameRegistry.addRecipe(new ItemStack(blockTransportDropper.getBlock()),"GGG","G G","SDS",'G',Blocks.glass,'S',Blocks.cobblestone,'D',Blocks.dropper);
    }
}
