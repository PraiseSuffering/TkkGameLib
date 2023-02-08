package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.tool.tkkFastMap;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.HashMap;

public class jsStorageTool {

    //System storage (don't use it!)
    public static tkkSerializationMap systemDate;
    //==============================
    public static HashMap tempMap = new HashMap();
    public static tkkFastMap tkkmap;//old
    public static tkkSerializationMap dateMap;//old
    public static HashMap<String,tkkSerializationMap> map = new HashMap<String,tkkSerializationMap>();
    public static HashMap<String,HashMap> temp = new HashMap<String,HashMap>();

    public static void init(FMLPreInitializationEvent event)
    {
        //位置配置完毕
        systemDate=new tkkSerializationMap("systemDate");
        systemDate.getHashMap().putIfAbsent("autoSave",new String[]{"dateMap"});
        systemDate.getHashMap().putIfAbsent("tempMap",new String[]{"temp"});
        //获取systemDate完成
        String[] mapArray=(String[]) systemDate.getHashMap().get("autoSave");
        for(String mapName:mapArray){
            map.put(mapName,new tkkSerializationMap(mapName));
        }
        String[] mapArray2=(String[]) systemDate.getHashMap().get("tempMap");
        for(String mapName:mapArray2){
            temp.put(mapName,new HashMap());
        }
        //map数组配置完毕
        tkkmap=new tkkFastMap("default");//old
        dateMap=new tkkSerializationMap("dateMap");//old

    }


    public static boolean hasTempMap(String name){
        String[] a=(String[])systemDate.getHashMap().get("tempMap");
        return Arrays.asList(a).contains(name);
    }
    public static boolean hasAutoSaveMap(String name){
        String[] a=(String[]) systemDate.getHashMap().get("autoSave");
        return Arrays.asList(a).contains(name);
    }
    public static boolean addTempMap(String name){
        if(hasTempMap(name)){
            return false;
        }else {
            String[] old=(String[]) systemDate.getHashMap().get("tempMap");
            String[] newArray=new String[old.length+1];
            System.arraycopy(old, 0, newArray, 0, old.length);
            newArray[newArray.length-1]=name;
            systemDate.getHashMap().put("tempMap",newArray);
            temp.put(name,new HashMap());
            return true;
        }
    }
    public static boolean addAutoSaveMap(String name){
        if(hasAutoSaveMap(name)){
            return false;
        }else {
            String[] old=(String[]) systemDate.getHashMap().get("autoSave");
            String[] newArray=new String[old.length+1];
            System.arraycopy(old, 0, newArray, 0, old.length);
            newArray[newArray.length-1]=name;
            systemDate.getHashMap().put("autoSave",newArray);
            map.put(name,new tkkSerializationMap(name));
            return true;
        }
    }
    public static tkkSerializationMap getAutoSaveMap(String name){
        if(!hasAutoSaveMap(name)){
            addAutoSaveMap(name);
        }
        return map.get(name);
    }
    public static HashMap getTempMap(String name){
        if(!hasTempMap(name)){
            addTempMap(name);
        }
        return temp.get(name);
    }
}
