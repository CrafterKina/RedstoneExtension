package com.mods.kina.RedstoneExtension.renderer;

import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BlockTransportDropperRender implements ISimpleBlockRenderingHandler{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer){
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();

        renderDropper(renderer, 0, 0, 0, block, tessellator, 0);

        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getLightValue(world, x, y, z));

        float f = 1.0F;
        int color = block.colorMultiplier(world, x, y, z);
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        if(EntityRenderer.anaglyphEnable){
            float blightness = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float yow = (r * 30.0F + g * 70.0F) / 100.0F;
            float p = (r * 30.0F + b * 70.0F) / 100.0F;
            r = blightness;
            g = yow;
            b = p;
        }
        tessellator.setNormal(f * r, f * g, f * b);

        renderDropper(renderer, x, y, z, block, tessellator, renderer.blockAccess.getBlockMetadata(x, y, z));

        return true;
    }

    private void renderDropper(RenderBlocks renderblocks, int i, int j, int k, Block block, Tessellator tessellator, int meta){
        IIcon glassTex = renderblocks.getBlockIconFromSide(Blocks.glass, 0);
        IIcon baseTex = renderblocks.getBlockIconFromSideAndMetadata(Blocks.dispenser, 0, 0);

        float f0 = 0.0625F;//16*16_1

        //renderblocks.setRenderBounds(f0, f0 * 2.0F, f0, 1.0F - f0, 1.0F - f0, 1.0F - f0);
        //double[] a={block.getBlockBoundsMinX(),block.getBlockBoundsMinY(),block.getBlockBoundsMinZ(),block.getBlockBoundsMaxX(),block.getBlockBoundsMaxY(),block.getBlockBoundsMaxZ()};
        switch(getFace(meta)){
            case 4:
                renderblocks.setRenderBounds(f0 * 2, 0, 0, 1, 1, 1);
            case 0:
                renderblocks.setRenderBounds(0, f0 * 2, 0, 1, 1, 1);
            case 2:
                renderblocks.setRenderBounds(0, 0, f0 * 2, 1, 1, 1);
            case 5:
                renderblocks.setRenderBounds(0, 0, 0, f0 * 14, 1, 1);
            case 1:
                renderblocks.setRenderBounds(0, 0, 0, 1, f0 * 14, 1);
            case 3:
                renderblocks.setRenderBounds(0, 0, 0, 1, 1, f0 * 14);
        }
        renderAllFace(renderblocks, i, j, k, block, tessellator, glassTex, true);

        //renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, f0 * 2.0F, 1.0F);
        switch(getFace(meta)){
            case 4:
                renderblocks.setRenderBounds(0, 0, 0, f0 * 2, 1, 1);
            case 0:
                renderblocks.setRenderBounds(0, 0, 0, 1, f0 * 2, 1);
            case 2:
                renderblocks.setRenderBounds(0, 0, 0, 1, 1, f0 * 2);
            case 5:
                renderblocks.setRenderBounds(f0 * 14, 1, 1, 1, 0, 0);
            case 1:
                renderblocks.setRenderBounds(1, f0 * 14, 1, 0, 1, 0);
            case 3:
                renderblocks.setRenderBounds(1, 1, f0 * 14, 0, 0, 1);
        }
        renderAllFace(renderblocks, i, j, k, block, tessellator, baseTex, true);

        /*if(getFace(meta)>=3)
            block.setBlockBounds(1,1,1,1,1,1);
        else*/
        block.setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    private void renderAllFace(RenderBlocks renderblocks, int i, int j, int k, Block block, Tessellator tessellator, IIcon Tex, boolean b){
        if(b){
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderblocks.renderFaceXPos(block, i, j, k, Tex);
        }
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, i, j, k, Tex);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceYPos(block, i, j, k, Tex);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceYNeg(block, i, j, k, Tex);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZPos(block, i, j, k, Tex);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZNeg(block, i, j, k, Tex);
    }

    public static int getFace(int meta){
        int k = meta & 7;
        for(int side = 0; side < 6; side++){
            if(side == k) return side;
        }
        return -1;
    }

    public boolean shouldRender3DInInventory(int modelId){
        return true;
    }

    public int getRenderId(){
        return RedstoneExtensionCore.RenderType;
    }
}
