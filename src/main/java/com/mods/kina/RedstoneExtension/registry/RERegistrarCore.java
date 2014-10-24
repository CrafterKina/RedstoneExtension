package com.mods.kina.RedstoneExtension.registry;

import com.mods.kina.RedstoneExtension.blocks.*;
import com.mods.kina.RedstoneExtension.item.parts.PartComminution;
import com.mods.kina.RedstoneExtension.link.amt.PartAMTProcessor;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityMoonlightDetector;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityRotaryMachine;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static com.mods.kina.RedstoneExtension.RedstoneExtensionCore.*;

public class RERegistrarCore{
    public RERegistrarCore(){super();}
    public void registryBlock(){
        blockBlockDispenser = new BlockBlockDispenser();
        blockMoonlightDetector = new BlockMoonlightDetector();
        blockTransportDropper = new BlockTransportDropper();
        //blockCrank=new BlockCrank();
        blockDownwardPressurePlate=new BlockDownwardPressurePlate();
        blockRotaryMachine=new BlockRotaryMachine();
        itemPartComminution=new PartComminution();
        itemPartAMTProcessor=new PartAMTProcessor();
        GameRegistry.registerBlock(blockBlockDispenser, "blockBlockDispenser");
        GameRegistry.registerBlock(blockMoonlightDetector, "blockMoonlightDetector");
        GameRegistry.registerBlock(blockTransportDropper, "blockTransportDropper");
        //GameRegistry.registerBlock(blockCrank,"blockCrank");
        GameRegistry.registerBlock(blockDownwardPressurePlate,"blockDownwardPressurePlate");
        GameRegistry.registerBlock(blockRotaryMachine,"blockRotaryMachine");
        GameRegistry.registerItem(itemPartComminution,"itemPartComminution");
        GameRegistry.registerItem(itemPartAMTProcessor,"itemPartAMTProcessor");
    }

    public void registryTile(){
        GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "TileEntityBlockDispenser");
        GameRegistry.registerTileEntity(TileEntityMoonlightDetector.class, "TileEntityMoonlightDetector");
        //GameRegistry.registerTileEntity(TileEntityCrank.class,"");
        //GameRegistry.registerTileEntity(TileEntityTransportDropper.class,"TileEntityTransportDropper");
        GameRegistry.registerTileEntity(TileEntityRotaryMachine.class,"TileEntityRotaryMachine");
    }

    public void registryRecipe(){
        GameRegistry.addRecipe(new ItemStack(blockMoonlightDetector), "XXX", "HDH", "HRH", 'X', Blocks.obsidian, 'H', Blocks.end_stone, 'D', Blocks.daylight_detector, 'R', Blocks.redstone_torch);
        GameRegistry.addRecipe(new ItemStack(blockBlockDispenser),"SSS","S S","SDS",'S',Blocks.mossy_cobblestone,'D',Blocks.dispenser);
        GameRegistry.addRecipe(new ItemStack(blockTransportDropper),"GGG","G G","SDS",'G',Blocks.glass,'S',Blocks.cobblestone,'D',Blocks.dropper);
    }
}
