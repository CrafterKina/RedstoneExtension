package com.mods.kina.RedstoneExtension.proxy;

import com.mods.kina.RedstoneExtension.RedstoneExtensionCore;
import com.mods.kina.RedstoneExtension.api.helper.RecipeHelper;
import com.mods.kina.RedstoneExtension.api.implementations.tiles.IRotaryMachineParts;
import com.mods.kina.RedstoneExtension.renderer.BlockTransportDropperRender;
import com.mods.kina.RedstoneExtension.renderer.TileEntityTransportDropperRender;
import com.mods.kina.RedstoneExtension.tileentity.TileEntityTransportDropper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import mods.defeatedcrow.api.recipe.IProsessorRecipe;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy{
    public void registerTileEntitySpecialRenderer(){
        RedstoneExtensionCore.RenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RedstoneExtensionCore.RenderType, new BlockTransportDropperRender());
        ClientRegistry.registerTileEntity(TileEntityTransportDropper.class, "transport_dropper", new TileEntityTransportDropperRender());
    }

    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }

    public void loadModRecipe()throws IllegalArgumentException{
        if(Loader.isModLoaded("DCsAppleMilk")){
            for(IProsessorRecipe recipe:RecipeRegisterManager.prosessorRecipe.getRecipes()){
                if(!recipe.isFoodRecipe()){
                    IRotaryMachineParts amt=((IRotaryMachineParts)RedstoneExtensionCore.itemPartAMTProcessor);
                    //if(recipe.getInput().length==1){
                        if (recipe.getInput()[0] instanceof String) {
                            RecipeHelper.addOreRecipeI(amt,(String) recipe.getInput()[0],recipe.getOutput(),20);
                            if(recipe.getSecondary()!=null)
                                RecipeHelper.addOreRareRecipeNoOptionI(amt,(String)recipe.getInput()[0],recipe.getSecondary(),1);
                        } else if (recipe.getInput()[0] instanceof ItemStack) {
                            amt.addRecipe(((ItemStack)recipe.getInput()[0]),recipe.getOutput(),20);
                            if(recipe.getSecondary()!=null)
                                amt.addRareRecipe((ItemStack)recipe.getInput()[0],recipe.getSecondary(),1);
                        } else if (recipe.getInput()[0] instanceof Item) {
                            amt.addRecipe(new ItemStack((Item)recipe.getInput()[0]),recipe.getOutput(),20);
                            if(recipe.getSecondary()!=null)
                                amt.addRareRecipe(new ItemStack((Item)recipe.getInput()[0]),recipe.getSecondary(),1);
                        } else if (recipe.getInput()[0] instanceof Block) {
                            amt.addRecipe(new ItemStack((Block)recipe.getInput()[0]),recipe.getOutput(),20);
                            if(recipe.getSecondary()!=null)
                                amt.addRareRecipe(new ItemStack((Block)recipe.getInput()[0]),recipe.getSecondary(),1);
                        } else {
                            throw new IllegalArgumentException("Unknown Object passed to recipe!");
                        }
                    //}
                }
            }
        }
        if(Loader.isModLoaded("IC2")){

        }
    }
}
