package com.twokktwo.tkklib.timeFunction;

import com.twokktwo.tkklib.js.JsContainer;
import net.minecraft.nbt.NBTTagCompound;

public class TimeFunction{
    public int life;
    public boolean save=false;
    public String jsCode="";


    public JsContainer js;
    public TimeFunction(){}
    public TimeFunction(String jsCode){
        this.jsCode=jsCode;
        js=new JsContainer(jsCode);
    }
    public TimeFunction(NBTTagCompound nbt){
        readNBT(nbt);
    }
    public boolean isSave(){
        return save;
    }
    public Object call(String functionName,Object... args){
        if(js!=null && !js.errored){
            return js.run(functionName,args);
        }
        return null;
    }
    public void readNBT(NBTTagCompound nbt){
        save=nbt.getBoolean("save");
        life=nbt.getInteger("life");
        jsCode=nbt.getString("jsCode");
        js=new JsContainer(jsCode);
        call("readNBT",this,nbt);
    }
    public NBTTagCompound writeNBT(NBTTagCompound nbt){
        nbt.setBoolean("save",save);
        nbt.setInteger("life",life);
        nbt.setString("jsCode",jsCode);
        call("writeNBT",this,nbt);
        return nbt;
    }


}
