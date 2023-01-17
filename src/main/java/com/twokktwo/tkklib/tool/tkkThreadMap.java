package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class tkkThreadMap {
    private ConcurrentHashMap hashMap;
    public File file;
    public String name="NoName";

    public tkkThreadMap() {
        try {

            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new ConcurrentHashMap();
        }catch (Exception e){
            FMLLog.log.log(Level.WARN,"tkklib.tool.map.tkkFastMap():"+e);}
    }

    public tkkThreadMap(String saveName) {
        try {
            file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/fastMap/");
            if (!file.exists()) {
                file.mkdirs();
            }
            hashMap = new ConcurrentHashMap();
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
            FMLLog.log.log(Level.WARN,"tkklib.tool.map.save("+name+") : "+e);
            return false;
        }
    }

    public boolean read(){
        try {
            File files = new File(file.getCanonicalPath() +"/"+ name + ".tkk");
            String text = null;
            if (files.exists()) {
                FileInputStream fis = new FileInputStream(files);
                ObjectInputStream ois = new ObjectInputStream(fis);
                hashMap = (ConcurrentHashMap) ois.readObject();
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

    public void set(Serializable key,Serializable value){
        hashMap.put(key,value);
    }

    public Object replace(Serializable key,Serializable value){return hashMap.replace(key,value);}

    public Object putIfAbsent(Serializable key,Serializable value){return hashMap.putIfAbsent(key,value);}

    public Object get(Object key){return hashMap.get(key);}

    public boolean hasKey(Object key){return hashMap.containsKey(key);}

    public boolean hasValue(Serializable value){return hashMap.containsValue(value);}

    public Object remove(Object key){return hashMap.remove(key);}

    public Object remove(Object key,Serializable value){return hashMap.remove(key,value);}

    public Collection values(){return hashMap.values();}

    public int size(){return hashMap.size();}

    public Set keySet(){return hashMap.keySet();}

    public Set entrySet(){return hashMap.entrySet();}

    public void clear(){hashMap.clear();}

    public ConcurrentHashMap getHashMap(){
        return hashMap;
    }

    public String toString(){
        return "tkkSerializationMap{File:"+file.toString()+",Name:"+name+",hashMap:"+hashMap.toString()+"}";
    }
}
