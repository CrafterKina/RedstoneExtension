package com.mods.kina.RedstoneExtension.renderer;

import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityTransportDropperRender extends TileEntitySpecialRenderer{
    public static TileEntityTransportDropperRender render;
    private EntityItem itemEntity;

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8){
        TileEntityTransportDropper dropper = (TileEntityTransportDropper) var1;
        ItemStack item = dropper.getStackInSlot(0);
        if(item == null){
            //System.out.print(false);
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F, (float) var6 + 0.5F);
        if(itemEntity == null){
            itemEntity = new EntityItem(null, var2, var4, var6, item);
            itemEntity.getEntityItem().stackSize = 1;
        }else if((itemEntity.getEntityItem() != null) && (!itemEntity.getEntityItem().isItemEqual(item))){
            itemEntity.setEntityItemStack(item.copy());
            itemEntity.getEntityItem().stackSize = 1;
        }
        if(itemEntity.getEntityItem() != null){
            long time = dropper.getWorldObj().getWorldTime();
            if(GameRegistry.findBlock(Item.itemRegistry.getNameForObject(item.getItem()).split(":")[0], Item.itemRegistry.getNameForObject(item.getItem()).split(":")[1]) != null){
                GL11.glTranslatef(0.0F, -0.15f + 0.05F * (float) Math.sin(time / 8.0D), 0.0F);
                GL11.glScaled(1.75, 1.75, 1.75);
            }else{
                GL11.glTranslatef(0.0F, -0.2F + 0.05F * (float) Math.sin(time / 8.0D), 0.0F);
            }
            RenderManager.instance.renderEntityWithPosYaw(itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        }
        GL11.glPopMatrix();
    }
}
