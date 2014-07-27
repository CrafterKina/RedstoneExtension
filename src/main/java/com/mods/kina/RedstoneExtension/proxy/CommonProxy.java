package com.mods.kina.RedstoneExtension.proxy;

import com.mods.kina.RedstoneExtension.container.ContainerBlockDispenser;
import com.mods.kina.RedstoneExtension.container.ContainerTransportDropper;
import com.mods.kina.RedstoneExtension.gui.GuiBlockDispenser;
import com.mods.kina.RedstoneExtension.gui.GuiTransportDropper;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler{
    public void registerTileEntitySpecialRenderer(){
    }

    public World getClientWorld(){
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(world.getTileEntity(x,y,z)!=null){
            switch(ID){
                case 0:return new ContainerBlockDispenser(player.inventory, (TileEntityBlockDispenser) world.getTileEntity(x, y, z));
                case 1:return new ContainerTransportDropper(player.inventory, (TileEntityTransportDropper) world.getTileEntity(x, y, z));
            }
        }
        return null;
    }

    public void registerRenderers() {
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(world.getTileEntity(x,y,z)!=null){
            switch(ID){
                case 0: return new GuiBlockDispenser(player.inventory, (TileEntityBlockDispenser) world.getTileEntity(x, y, z));
                case 1: return new GuiTransportDropper(player.inventory, (TileEntityTransportDropper) world.getTileEntity(x, y, z));
            }
        }
        return null;
    }
}
