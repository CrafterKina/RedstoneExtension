package com.mods.kina.RedstoneExtension.gui;

import com.mods.kina.RedstoneExtension.container.ContainerTransportDropper;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTransportDropper extends GuiContainer{
    private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("kina:textures/gui/container/transport_dropper.png");
    public TileEntityTransportDropper dropper;

    public GuiTransportDropper(InventoryPlayer par1InventoryPlayer, TileEntityTransportDropper par2TileEntityTDropper){
        super(new ContainerTransportDropper(par1InventoryPlayer, par2TileEntityTDropper));
        dropper = par2TileEntityTDropper;
    }

    /**
     Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_){
        this.fontRendererObj.drawString("TransportDropper", this.xSize / 2 - this.fontRendererObj.getStringWidth("TransportDropper") / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
