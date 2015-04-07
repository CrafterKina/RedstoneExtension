package com.mods.kina.RedstoneExtension.power;

public class VoltageAndCountContainer{
    private EnumVoltage v;
    private int c;
    public VoltageAndCountContainer(EnumVoltage voltage,int count){
        v=voltage;
        c=count;
    }

    public EnumVoltage getVoltage(){
        return v;
    }

    public int getCount(){
        return c;
    }
}
