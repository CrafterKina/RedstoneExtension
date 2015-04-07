package com.mods.kina.RedstoneExtension.renderer.block;

import com.mods.kina.RedstoneExtension.base.BlockMachineBase;
import com.mods.kina.RedstoneExtension.base.TileEntityMachineBase;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemDye;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class BlockMachineRendererBase implements ISimpleBlockRenderingHandler{
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceXPos(block, 0, 0, 0, block.getIcon(0, 0));
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0, 0, 0, block.getIcon(1, 0));
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, block.getIcon(2, 0));
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(3, 0));
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0, 0, 0, block.getIcon(4, 0));
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(5, 0));

        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
        TileEntityMachineBase entityBase = (TileEntityMachineBase) world.getTileEntity(x, y, z);
        /*int l = block.colorMultiplier(world, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;*/
        boolean partialRenderBounds = Minecraft.getMinecraft().gameSettings.ambientOcclusion >= 2 && (block.getBlockBoundsMinX() > 0.0D || block.getBlockBoundsMaxX() < 1.0D || block.getBlockBoundsMinY() > 0.0D || block.getBlockBoundsMaxY() < 1.0D || block.getBlockBoundsMinZ() > 0.0D || block.getBlockBoundsMaxZ() < 1.0D);
        /*if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }*/
        return Minecraft.isAmbientOcclusionEnabled() && block.getLightValue() == 0 ? (partialRenderBounds ? renderStandardBlockWithAmbientOcclusionPartial((BlockMachineBase) block, entityBase, renderer, x, y, z) : renderStandardBlockWithAmbientOcclusion((BlockMachineBase) block, entityBase, renderer, x, y, z)) : renderStandardBlockWithColorMultiplier((BlockMachineBase) block, entityBase, renderer, x, y, z);
    }

    public boolean renderStandardBlockWithColorMultiplier(BlockMachineBase blockBase, TileEntityMachineBase entityBase, RenderBlocks renderer, int x, int y, int z){
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 0.5F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float[] rgb;

        int l = blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);

        IIcon iicon;

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0)){
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 0);
            renderer.renderFaceYNeg(blockBase, (double) x, (double) y, (double) z, iicon);
            if(/*!renderer.hasOverrideBlockTexture() && entityBase.gates[0] != null*/entityBase.isOverrideIconAvailable(0)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.DOWN);
                tessellator.setColorOpaque_F(f3 * rgb[0], f3 * rgb[1], f3 * rgb[2]);
                renderer.renderFaceYNeg(blockBase, x, y, z, entityBase.getIconSideOverlay(0));
            }
            flag = true;
        }

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1)){
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
            tessellator.setColorOpaque_F(f3, f3, f3);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 1);
            renderer.renderFaceYPos(blockBase, (double) x, (double) y, (double) z, iicon);
            if(/*!renderer.hasOverrideBlockTexture() && entityBase.gates[1] != null*/entityBase.isOverrideIconAvailable(1)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.UP);
                tessellator.setColorOpaque_F(f3 * rgb[0], f3 * rgb[1], f3 * rgb[2]);
                renderer.renderFaceYPos(blockBase, x, y, z,entityBase.getIconSideOverlay(1));
            }
            flag = true;
        }

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)){
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
            tessellator.setColorOpaque_F(f5, f5, f5);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(blockBase, (double) x, (double) y, (double) z, iicon);

            if(/*!renderer.hasOverrideBlockTexture() && entityBase.gates[2] != null*/entityBase.isOverrideIconAvailable(2)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.NORTH);
                tessellator.setColorOpaque_F(f5 * rgb[0], f5 * rgb[1], f5 * rgb[2]);
                renderer.renderFaceZNeg(blockBase, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(2));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)){
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
            tessellator.setColorOpaque_F(f5, f5, f5);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(blockBase, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.gates[3] != null){
                rgb = getRGBFromGate(entityBase, ForgeDirection.SOUTH);
                tessellator.setColorOpaque_F(f5 * rgb[0], f5 * rgb[1], f5 * rgb[2]);
                renderer.renderFaceZPos(blockBase, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(3));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)){
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
            tessellator.setColorOpaque_F(f6, f6, f6);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(blockBase, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.gates[4] != null){
                rgb = getRGBFromGate(entityBase, ForgeDirection.WEST);
                tessellator.setColorOpaque_F(f6 * rgb[0], f6 * rgb[1], f6 * rgb[2]);
                renderer.renderFaceXNeg(blockBase, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(4));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || blockBase.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)){
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : blockBase.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
            tessellator.setColorOpaque_F(f6, f6, f6);
            iicon = renderer.getBlockIcon(blockBase, renderer.blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(blockBase, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.gates[5] != null){
                rgb = getRGBFromGate(entityBase, ForgeDirection.EAST);
                tessellator.setColorOpaque_F(f6 * rgb[0], f6 * rgb[1], f6 * rgb[2]);
                renderer.renderFaceXPos(blockBase, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(5));
            }

            flag = true;
        }

        return flag;
    }

    //Todo Fix
    public boolean renderStandardBlockWithAmbientOcclusion(BlockMachineBase block, TileEntityMachineBase entityBase, RenderBlocks renderer, int x, int y, int z){
        renderer.enableAO = true;
        boolean flag = false;
        float f3;
        float f4;
        float f5;
        float f6;
        //boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
/*
        if (renderer.getBlockIcon(block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (renderer.hasOverrideBlockTexture())
        {
            flag1 = false;
        }*/

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;

        float[] rgb;
        IIcon iicon;

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0)){
            if(renderer.renderMinY <= 0.0D){
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if(renderer.renderMinY <= 0.0D){
                ++y;
            }

            i1 = l;

            if(renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(x, y - 1, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            }

            f7 = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.5F;
            }*/
            /*else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 0));
            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(0)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.DOWN);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(0));
            }
            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1)){
            if(renderer.renderMaxY >= 1.0D){
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if(renderer.renderMaxY >= 1.0D){
                --y;
            }

            i1 = l;

            if(renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(x, y + 1, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            }

            f7 = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            int colorMultiplier = entityBase.blockType.colorMultiplier(entityBase.getWorldObj(), x, y, z);
            float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
            float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
            float b = (float) (colorMultiplier & 255) / 255.0F;
            if(EntityRenderer.anaglyphEnable){
                float t1 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
                float t2 = (r * 30.0F + g * 70.0F) / 100.0F;
                float t3 = (r * 30.0F + g * 70.0F) / 100.0F;
                r = t1;
                g = t2;
                b = t3;
            }
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 1));
            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(1)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.UP);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(1));
            }
            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)){
            if(renderer.renderMinZ <= 0.0D){
                --z;
            }

            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;
            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if(renderer.renderMinZ <= 0.0D){
                ++z;
            }

            i1 = l;

            if(renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(x, y, z - 1).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            }

            f7 = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            //}
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(2)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.NORTH);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(2));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)){
            if(renderer.renderMaxZ >= 1.0D){
                ++z;
            }

            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if(renderer.renderMaxZ >= 1.0D){
                --z;
            }

            i1 = l;

            if(renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(x, y, z + 1).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            }

            f7 = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f6 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f5 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.8F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3));

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(3)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.SOUTH);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(3));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)){
            if(renderer.renderMinX <= 0.0D){
                --x;
            }

            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if(renderer.renderMinX <= 0.0D){
                ++x;
            }

            i1 = l;

            if(renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(x - 1, y, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            }

            f7 = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f3 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(4)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.WEST);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(4));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)){
            if(renderer.renderMaxX >= 1.0D){
                ++x;
            }

            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if(renderer.renderMaxX >= 1.0D){
                --x;
            }

            i1 = l;

            if(renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(x + 1, y, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            }

            f7 = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f5 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f6 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = p_147751_5_ * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = p_147751_6_ * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = p_147751_7_ * 0.6F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(5)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.EAST);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(5));
            }

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }

    public boolean renderStandardBlockWithAmbientOcclusionPartial(BlockMachineBase block, TileEntityMachineBase entityBase, RenderBlocks renderer, int x, int y, int z){
        renderer.enableAO = true;
        boolean flag = false;
        float f3;
        float f4;
        float f5;
        float f6;
        int l = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;

        float[] rgb;

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0)){
            if(renderer.renderMinY <= 0.0D){
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if(renderer.renderMinY <= 0.0D){
                ++y;
            }

            i1 = l;

            if(renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(x, y - 1, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            }

            f7 = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0] * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1] * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2] * 0.5F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 0));
            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(0)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.DOWN);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(0));
            }
            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1)){
            if(renderer.renderMaxY >= 1.0D){
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if(renderer.renderMaxY >= 1.0D){
                --y;
            }

            i1 = l;

            if(renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(x, y + 1, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            }

            f7 = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            int colorMultiplier = entityBase.blockType.colorMultiplier(entityBase.getWorldObj(), x, y, z);
            float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
            float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
            float b = (float) (colorMultiplier & 255) / 255.0F;
            if(EntityRenderer.anaglyphEnable){
                float t1 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
                float t2 = (r * 30.0F + g * 70.0F) / 100.0F;
                float t3 = (r * 30.0F + g * 70.0F) / 100.0F;
                r = t1;
                g = t2;
                b = t3;
            }
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 1));
            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(1)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.UP);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(1));
            }
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        IIcon iicon;

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)){
            if(renderer.renderMinZ <= 0.0D){
                --z;
            }

            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();             flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();             flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();             flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if(renderer.renderMinZ <= 0.0D){
                ++z;
            }

            i1 = l;

            if(renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(x, y, z - 1).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            }

            f7 = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            f3 = (float) ((double) f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double) f9 * renderer.renderMaxY * renderer.renderMinX + (double) f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float) ((double) f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double) f9 * renderer.renderMaxY * renderer.renderMaxX + (double) f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            f5 = (float) ((double) f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double) f9 * renderer.renderMinY * renderer.renderMaxX + (double) f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float) ((double) f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double) f9 * renderer.renderMinY * renderer.renderMinX + (double) f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0] * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1] * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2] * 0.8F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(2)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.NORTH);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(2));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)){
            if(renderer.renderMaxZ >= 1.0D){
                ++z;
            }

            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();             flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();             flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();             flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if(renderer.renderMaxZ >= 1.0D){
                --z;
            }

            i1 = l;

            if(renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(x, y, z + 1).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            }

            f7 = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f3 = (float) ((double) f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double) f9 * renderer.renderMaxY * renderer.renderMinX + (double) f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float) ((double) f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double) f9 * renderer.renderMinY * renderer.renderMinX + (double) f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            f5 = (float) ((double) f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double) f9 * renderer.renderMinY * renderer.renderMaxX + (double) f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float) ((double) f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double) f9 * renderer.renderMaxY * renderer.renderMaxX + (double) f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0] * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1] * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2] * 0.8F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(3)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.SOUTH);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(3));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)){
            if(renderer.renderMinX <= 0.0D){
                --x;
            }

            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();             flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();             flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();             flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag4 && !flag3){
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if(!flag5 && !flag3){
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if(!flag4 && !flag2){
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }else{
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if(!flag5 && !flag2){
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }else{
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if(renderer.renderMinX <= 0.0D){
                ++x;
            }

            i1 = l;

            if(renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(x - 1, y, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            }

            f7 = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            f3 = (float) ((double) f9 * renderer.renderMaxY * renderer.renderMaxZ + (double) f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double) f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            f4 = (float) ((double) f9 * renderer.renderMaxY * renderer.renderMinZ + (double) f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double) f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double) f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            f5 = (float) ((double) f9 * renderer.renderMinY * renderer.renderMinZ + (double) f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double) f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            f6 = (float) ((double) f9 * renderer.renderMinY * renderer.renderMaxZ + (double) f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double) f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double) f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0] * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1] * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2] * 0.6F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(4)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.WEST);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(4));
            }

            flag = true;
        }

        if(renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)){
            if(renderer.renderMaxX >= 1.0D){
                ++x;
            }

            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            /*flag2 = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();             flag3 = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();             flag4 = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();             flag5 = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();*/
            flag2 = false;
            flag3 = false;
            flag4 = false;
            flag5 = false;

            if(!flag3 && !flag5){
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if(!flag3 && !flag4){
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if(!flag2 && !flag5){
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }else{
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if(!flag2 && !flag4){
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }else{
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if(renderer.renderMaxX >= 1.0D){
                --x;
            }

            i1 = l;

            if(renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(x + 1, y, z).isOpaqueCube()){
                i1 = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            }

            f7 = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f11 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float) ((double) f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double) f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double) f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double) f11 * renderer.renderMinY * renderer.renderMaxZ);
            f4 = (float) ((double) f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double) f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double) f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double) f11 * renderer.renderMinY * renderer.renderMinZ);
            f5 = (float) ((double) f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double) f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double) f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double) f11 * renderer.renderMaxY * renderer.renderMinZ);
            f6 = (float) ((double) f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double) f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double) f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double) f11 * renderer.renderMaxY * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

            /*if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0] * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1] * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2] * 0.6F;
            }
            else
            {*/
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            //}

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, iicon);

            if(!renderer.hasOverrideBlockTexture() && entityBase.isOverrideIconAvailable(5)){
                rgb = getRGBFromGate(entityBase, ForgeDirection.EAST);
                renderer.colorRedTopLeft *= rgb[0];
                renderer.colorRedBottomLeft *= rgb[0];
                renderer.colorRedBottomRight *= rgb[0];
                renderer.colorRedTopRight *= rgb[0];
                renderer.colorGreenTopLeft *= rgb[1];
                renderer.colorGreenBottomLeft *= rgb[1];
                renderer.colorGreenBottomRight *= rgb[1];
                renderer.colorGreenTopRight *= rgb[1];
                renderer.colorBlueTopLeft *= rgb[2];
                renderer.colorBlueBottomLeft *= rgb[2];
                renderer.colorBlueBottomRight *= rgb[2];
                renderer.colorBlueTopRight *= rgb[2];
                renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, entityBase.getIconSideOverlay(5));
            }

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }

    private float[] getRGBFromGate(TileEntityMachineBase entityBase, ForgeDirection fDirection){
        int direction = fDirection.ordinal();
        int meta = entityBase.gates[direction]!=null?entityBase.gates[direction].getItemDamage():15;
        int color = ItemDye.field_150922_c[meta];
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        return new float[]{r, g, b};
    }

    private IIcon getItemIcon(String s){
        return ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(s);
    }

    public boolean shouldRender3DInInventory(int modelId){
        return true;
    }

    public int getRenderId(){
        return EnumRenderType.MachineBase.getType();
    }
}
