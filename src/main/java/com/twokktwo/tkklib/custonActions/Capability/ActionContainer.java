package com.twokktwo.tkklib.custonActions.Capability;

import net.minecraft.nbt.NBTTagCompound;

public interface ActionContainer {
     void setEnable(boolean bool);
     boolean getEnable();
     void setReplace(boolean bool);
     boolean getReplace();
     void setJS(String js);
     void putJS(String js);
     String getJS();

     void setTempDataInt(int time);
     int getTempDataInt();

    void setTempDataString(String temp);
    String getTempDataString();

    NBTTagCompound getNBT();
    void setNBT(NBTTagCompound nbt);

    Object runJs(String functionName,Object... args);

}
