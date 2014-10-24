package com.mods.kina.RedstoneExtension.config;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static com.mods.kina.RedstoneExtension.RedstoneExtensionCore.ignoreItems;
import static com.mods.kina.RedstoneExtension.RedstoneExtensionCore.isEnable;

public class ConfigMaker{
    public static void createConfig(FMLPreInitializationEvent event){
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "DispensableBlockMod.cfg"));
        try{
            config.load();
            isEnable = config.get("VanillaDispenser", "isEnable", false).getBoolean(false);
            ignoreItems = config.get("VanillaDispenser", "blacklist_Item_Block", "minecraft:tnt", "set Ignoring Item and Block").getString();
        } catch(Exception e){
            FMLLog.severe("Error Message");
        }finally{
            config.save();
        }
    }
}
