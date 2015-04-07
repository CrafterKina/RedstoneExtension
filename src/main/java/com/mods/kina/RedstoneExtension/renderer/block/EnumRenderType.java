package com.mods.kina.RedstoneExtension.renderer.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum EnumRenderType{
    MachineBase,
    TransportDropper,;
    private int type;
    EnumRenderType(){
        type=getNextAvailableID();
    }
    @SideOnly(Side.CLIENT)
    static int getNextAvailableID(){
        return RenderingRegistry.getNextAvailableRenderId();
    }
    public int getType(){
        return type;
    }
}
