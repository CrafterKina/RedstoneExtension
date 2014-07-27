package com.mods.kina.RedstoneExtension;

import com.mods.kina.KinaCore.toExtends.KinaMod;
import com.mods.kina.RedstoneExtension.config.ConfigMaker;
import com.mods.kina.RedstoneExtension.dispense.BehaviorDispenseTool;
import com.mods.kina.RedstoneExtension.dispense.BehaviorSimpleDispenseBlock;
import com.mods.kina.RedstoneExtension.proxy.CommonProxy;
import com.mods.kina.RedstoneExtension.registry.RERegistrarCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;

@KinaMod
@Mod(modid = RedstoneExtensionCore.MODID)
public class RedstoneExtensionCore{
    public static final String MODID = "kina_RedstoneExtension";
    @Mod.Instance("kina_RedstoneExtension")
    public static RedstoneExtensionCore core;
    @SidedProxy(modId = MODID, clientSide = "com.mods.kina.RedstoneExtension.proxy.ClientProxy", serverSide = "com.mods.kina.RedstoneExtension.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static boolean isEnable;
    public static String ignoreItems;
    public static int RenderType;
    public static Block blockBlockDispenser;
    public static Block blockMoonlightDetector;
    public static Block blockTransportDropper;
    public static Block blockLongChest;
    public static RERegistrarCore RERegistrarCore = new RERegistrarCore();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigMaker.createConfig(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        RERegistrarCore.registryBlock();
        RERegistrarCore.registryTile();
        proxy.registerTileEntitySpecialRenderer();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        RERegistrarCore.registryRecipe();
        if(isEnable){
            setTool();
            setBlock();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
    }

    public void setBlock(){
        for(int i = 1; i <= 4095; i++){
            if(Item.getItemById(i) != null && isNotIgnoreItem(i) && Item.getItemById(i) instanceof ItemBlock){
                BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemById(i), new BehaviorSimpleDispenseBlock());
            }
        }
    }

    public void setTool(){
        for(int i = 256; i <= 31999; i++){
            if(Item.getItemById(i) != null && Item.getItemById(i) instanceof ItemTool && isNotIgnoreItem(i)){
                BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemById(i), new BehaviorDispenseTool());
            }
        }
    }

    public boolean isNotIgnoreItem(int i){
        if(!ignoreItems.equals("")){
            for(String a : getIgnoreString(ignoreItems)){
                Item item1 = (Item) Item.itemRegistry.getObject(a);
                Item item = Item.getItemById(i);
                if(item.equals(item1)){
                    return false;
                }
            }
        }
        return true;
    }

    public String[] getIgnoreString(String s){
        return s.split(",");
    }
}
