package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.HashMap;

public class tkkFastMap {
    public HashMap hashMap;
    public File file;
    public String name="NoName";

    public tkkFastMap() {
        try {

            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new HashMap();
        }catch (Exception e){FMLLog.log.log(Level.WARN,"tkklib.tool.map.tkkFastMap():"+e);}
    }

    public tkkFastMap(String saveName) {
        try {
            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new HashMap();
            this.name=saveName;
            this.read();
        }catch (Exception e){FMLLog.log.log(Level.WARN,"tkklib.tool.map.tkkFastMap("+saveName+"):"+e);}
    }


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

    public HashMap getHashMap(){
        return hashMap;
    }

    public void setHashMap(HashMap map){
        this.hashMap=map;
    }

    public String toString(){
        if(file==null || name==null || hashMap==null){return "tkkFastMap{?}";}
        return "tkkFastMap{File:"+file.toString()+",Name:"+name+",hashMap:"+hashMap.toString()+"}";
    }

}
