/*
package com.mods.kina.RedstoneExtension.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class BlockCrankRender implements ISimpleBlockRenderingHandler{//TODO 真っ赤だなー
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        renderer.renderAllFaces = true;

        renderer.setRenderBounds(0.45D, 0.0D, 0.45D, 0.55D, 0.8D, 0.55D);
        Tessellator tess = Tessellator.instance;

        BlockRenderInfo info = block.getRendererInstance();
        if (info.isValid())
        {
            if (block.hasSubtypes()) {
                block.setRenderStateByMeta(item.func_77960_j());
            }
            renderer.uvRotateBottom = info.getTexture(ForgeDirection.DOWN).setFlip(getOrientation(ForgeDirection.DOWN, ForgeDirection.SOUTH, ForgeDirection.UP));

            renderer.uvRotateTop = info.getTexture(ForgeDirection.UP).setFlip(getOrientation(ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.UP));

            renderer.uvRotateEast = info.getTexture(ForgeDirection.EAST).setFlip(getOrientation(ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.UP));

            renderer.uvRotateWest = info.getTexture(ForgeDirection.WEST).setFlip(getOrientation(ForgeDirection.WEST, ForgeDirection.SOUTH, ForgeDirection.UP));

            renderer.uvRotateNorth = info.getTexture(ForgeDirection.NORTH).setFlip(getOrientation(ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UP));

            renderer.uvRotateSouth = info.getTexture(ForgeDirection.SOUTH).setFlip(getOrientation(ForgeDirection.SOUTH, ForgeDirection.SOUTH, ForgeDirection.UP));
        }

        renderInvBlock(EnumSet.allOf(ForgeDirection.class), block, item, tess, 16777215, renderer);

        if (block.hasSubtypes()) {
            info.setTemporaryRenderIcon(null);
        }
        renderer.field_147865_v = (renderer.field_147875_q = renderer.field_147869_t = renderer.field_147871_s = renderer.field_147867_u = renderer.field_147873_r = 0);


        renderer.func_147782_a(0.5499999999999999D, 0.7D, 0.45D, 0.98D, 0.8D, 0.55D);
        super.renderInventory(blk, is, renderer, type, obj);

        renderer.field_147837_f = false;
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
        return false;
    }

    public boolean shouldRender3DInInventory(int modelId){
        return false;
    }

    public int getRenderId(){
        return 0;
    }
}
*/
