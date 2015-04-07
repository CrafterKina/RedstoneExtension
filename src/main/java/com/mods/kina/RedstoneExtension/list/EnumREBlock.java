package com.mods.kina.RedstoneExtension.list;

import com.mods.kina.RedstoneExtension.blocks.*;
import net.minecraft.block.Block;

public enum EnumREBlock{
    blockBlockDispenser(new BlockBlockDispenser()),
    blockMoonlightDetector(new BlockMoonlightDetector()),
    blockTransportDropper(new BlockTransportDropper()),
    blockDownwardPressurePlate(new BlockDownwardPressurePlate()),
    blockRotaryMachine(new BlockRotaryMachine()),;

    Block block;
    EnumREBlock(Block block){
        this.block=block;
    }

    public Block getBlock(){
        return block;
    }
}
