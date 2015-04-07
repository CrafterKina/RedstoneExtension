package com.mods.kina.RedstoneExtension.proxy;

import com.mods.kina.RedstoneExtension.renderer.block.BlockMachineRendererBase;
import com.mods.kina.RedstoneExtension.renderer.block.BlockTransportDropperRender;
import com.mods.kina.RedstoneExtension.renderer.tileentity.TileEntityTransportDropperRender;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy{
    public void registerTileEntitySpecialRenderer(){
        RenderingRegistry.registerBlockHandler(new BlockMachineRendererBase());
        RenderingRegistry.registerBlockHandler(new BlockTransportDropperRender());
        ClientRegistry.registerTileEntity(TileEntityTransportDropper.class, "transport_dropper", new TileEntityTransportDropperRender());
    }

    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }

    public void loadModRecipe()throws IllegalArgumentException{

    }
}
