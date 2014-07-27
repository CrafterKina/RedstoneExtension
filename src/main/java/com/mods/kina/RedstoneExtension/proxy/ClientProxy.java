package com.mods.kina.RedstoneExtension.proxy;

import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.renderer.BlockTransportDropperRender;
import com.mods.kina.RedstoneExtension.renderer.TileEntityTransportDropperRender;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{
    public void registerTileEntitySpecialRenderer(){
        RedstoneExtensionCore.RenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RedstoneExtensionCore.RenderType, new BlockTransportDropperRender());
        ClientRegistry.registerTileEntity(TileEntityTransportDropper.class, "transport_dropper", new TileEntityTransportDropperRender());
    }

    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
