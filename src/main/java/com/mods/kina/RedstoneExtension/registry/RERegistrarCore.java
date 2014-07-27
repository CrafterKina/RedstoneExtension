package com.mods.kina.RedstoneExtension.registry;

import com.mods.kina.RedstoneExtension.blocks.BlockBlockDispenser;
import com.mods.kina.RedstoneExtension.blocks.BlockMoonlightDetector;
import com.mods.kina.RedstoneExtension.blocks.BlockTransportDropper;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityMoonlightDetector;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import static com.mods.kina.RedstoneExtension.RedstoneExtensionCore.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class RERegistrarCore{
    public RERegistrarCore(){super();}
    public void registryBlock(){
        blockBlockDispenser = new BlockBlockDispenser();
        blockMoonlightDetector = new BlockMoonlightDetector();
        blockTransportDropper = new BlockTransportDropper();
        GameRegistry.registerBlock(blockBlockDispenser, "kina_blockBlockDispenser");
        GameRegistry.registerBlock(blockMoonlightDetector, "kina_blockMoonlightDetector");
        GameRegistry.registerBlock(blockTransportDropper, "kina_blockTransportDropper");
    }

    public void registryTile(){
        GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "TileEntityBlockDispenser");
        GameRegistry.registerTileEntity(TileEntityMoonlightDetector.class, "TileEntityMoonlightDetector");
        //GameRegistry.registerTileEntity(TileEntityTransportDropper.class,"TileEntityTransportDropper");
    }

    public void registryRecipe(){
        GameRegistry.addRecipe(new ItemStack(blockMoonlightDetector), "XXX", "HDH", "HRH", 'X', Blocks.obsidian, 'H', Blocks.end_stone, 'D', Blocks.daylight_detector, 'R', Blocks.redstone_torch);
        GameRegistry.addRecipe(new ItemStack(blockBlockDispenser),"SSS","S S","SDS",'S',Blocks.mossy_cobblestone,'D',Blocks.dispenser);
        GameRegistry.addRecipe(new ItemStack(blockTransportDropper),"GGG","G G","SDS",'G',Blocks.glass,'S',Blocks.cobblestone,'D',Blocks.dropper);
    }
}
