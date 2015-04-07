package com.mods.kina.RedstoneExtension.power;

import com.mods.kina.RedstoneExtension.api.implementations.IEnergyReceiver;
import com.mods.kina.RedstoneExtension.api.implementations.IEnergySender;
import com.mods.kina.RedstoneExtension.base.BlockPowerWireBase;
import net.minecraft.dispenser.IBlockSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class PowerNet{
    private EnumVoltage maxVoltage;
    private VoltageAndCountContainer providingPower=new VoltageAndCountContainer(maxVoltage,0);
    private ArrayList<IEnergyReceiver> receivers=new ArrayList<IEnergyReceiver>();
    private ArrayList<IEnergySender>   senders  =new ArrayList<IEnergySender>();
    private IBlockSource coreBlock;

    public PowerNet(EnumVoltage maxVoltage,IBlockSource coreSRC){
        this.maxVoltage=maxVoltage;
        coreBlock=coreSRC;
    }

    /*public boolean setMaxVoltage(EnumVoltage maxVoltage){
        if(this.maxVoltage.ordinal()>maxVoltage.ordinal()){
            this.maxVoltage = maxVoltage;
            return true;
        }
        else return false;
    }*/

    public void addReceiver(IEnergyReceiver receiver){
        receivers.add(receiver);
    }

    public void removeReceiver(IEnergyReceiver receiver){
        receivers.remove(receiver);
    }

    public void addSender(IEnergySender sender){
        senders.add(sender);
    }

    public void removeSender(IEnergySender sender){
        senders.remove(sender);
    }

    public static boolean isNeighborBlockConnectingNet(IBlockSource src){
        for(ForgeDirection direction : ForgeDirection.values()){
            if(src.getWorld().getBlock(src.getXInt()+direction.offsetX,src.getYInt()+direction.offsetY,src.getZInt()+direction.offsetZ)instanceof BlockPowerWireBase)return true;

        }
        return false;
    }
}
