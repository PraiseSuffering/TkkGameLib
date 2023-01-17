package com.twokktwo.tkklib.mapPiece.template;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class tkkEasyTemplate implements Serializable {
    public transient Template template;
    public String str;
    public tkkEasyTemplate(String ame){
        template=new Template(ame);
        str=template.writeToNBT(new NBTTagCompound()).toString();
    }
    public tkkEasyTemplate(){
        template=new Template();
        str=template.writeToNBT(new NBTTagCompound()).toString();
    }
    public void update(){
        str=template.writeToNBT(new NBTTagCompound()).toString();
    }
    public void reload() {
        try {
            Template temp=new Template();
            temp.read(JsonToNBT.getTagFromJson(str));
            template = temp;
        }catch (Exception e){
            TkkGameLib.logger.log(Level.ERROR,e);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        this.update();
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
        this.reload();
    }

}
