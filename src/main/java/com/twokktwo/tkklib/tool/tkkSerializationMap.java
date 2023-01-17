package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.HashMap;

public class tkkSerializationMap implements Serializable {
    private HashMap<Serializable,Serializable> hashMap;
    public File file;
    public String name="NoName";

    public tkkSerializationMap() {
        try {

            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new HashMap<Serializable,Serializable>();
        }catch (Exception e){
            FMLLog.log.log(Level.WARN,"tkklib.tool.map.tkkFastMap():"+e);}
    }

    public tkkSerializationMap(String saveName) {
        try {
            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new HashMap<Serializable,Serializable>();
            this.name=saveName;
            this.read();
        }catch (Exception e){FMLLog.log.log(Level.WARN,"tkklib.tool.map.tkkFastMap("+saveName+"):"+e);}
    }

    public void setName(String name){this.name=name;}

    public boolean save(){
        try{
            File files = new File(file.getCanonicalPath() +"/"+ name + ".tkk");
            if(!file.exists()){
                file.mkdirs();
            }
            if(!files.exists()) {
                files.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(files);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hashMap);
            oos.close();
            fos.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            FMLLog.log.log(Level.WARN,"tkklib.tool.map.save("+name+") : "+e);
            return false;
        }
    }

    public boolean read(){
        try {
            File files = new File(file.getCanonicalPath() +"/"+ name + ".tkk");
            if (files.exists()) {
                FileInputStream fis = new FileInputStream(files);
                ObjectInputStream ois = new ObjectInputStream(fis);
                hashMap = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            FMLLog.log.log(Level.INFO,"tkklib.tool.map.read("+name+") : "+e);
            return false;
        }

    }

    public void remove(String fileName){
        File files = new File(file + fileName + ".tkk");
        if(files.exists()){
            files.delete();
        }
    }

    public void setFilePath(String path){
        this.file = new File(path);
        if(file.exists()){
            file.mkdirs();
        }
    }

    public HashMap<Serializable,Serializable> getHashMap(){
        return hashMap;
    }

    public String toString(){
        return "tkkSerializationMap{File:"+file.toString()+",Name:"+name+",hashMap:"+hashMap.toString()+"}";
    }
}
