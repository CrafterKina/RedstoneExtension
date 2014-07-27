package com.mods.kina.RedstoneExtension.gui;

import com.mods.kina.RedstoneExtension.container.ContainerBlockDispenser;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityBlockDispenser;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBlockDispenser extends GuiContainer{//ok
    private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityBlockDispenser tileDispenser;

    public GuiBlockDispenser(InventoryPlayer par1InventoryPlayer, TileEntityBlockDispenser par2TileEntityDispenser){
        super(new ContainerBlockDispenser(par1InventoryPlayer, par2TileEntityDispenser));
        this.tileDispenser = par2TileEntityDispenser;
    }

    /**
     Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_){
        this.fontRendererObj.drawString("BlockDispenser", this.xSize / 2 - this.fontRendererObj.getStringWidth("BlockDispenser") / 2, 6, 4210752);
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
