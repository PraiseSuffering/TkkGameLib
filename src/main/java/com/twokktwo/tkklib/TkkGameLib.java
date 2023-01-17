package com.twokktwo.tkklib;

import com.twokktwo.tkklib.Command.CommandLoader;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.proxy.ClientProxy;
import com.twokktwo.tkklib.proxy.CommonProxy;
import com.twokktwo.tkklib.tool.Tool;
import com.twokktwo.tkklib.tool.configTool;
import com.twokktwo.tkklib.tool.map.mapTool;
import com.twokktwo.tkklib.tool.tkkFastMap;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

@Mod(modid = TkkGameLib.MODID, name = TkkGameLib.NAME, version = TkkGameLib.VERSION)
public class TkkGameLib
{
    public static final String MODID = "tkklib";
    public static final String NAME = "TkkGameLib";
    public static final String VERSION = "1.6.1";

    public static File MOD_DIR;

    public static MinecraftServer server;

    //System storage (don't use it!)
    public static tkkSerializationMap systemDate;
    //==============================
    public static HashMap tempMap = new HashMap();
    public static tkkFastMap tkkmap;//old
    public static tkkSerializationMap dateMap;//old
    public static HashMap<String,tkkSerializationMap> map = new HashMap<String,tkkSerializationMap>();
    public static HashMap<String,HashMap> temp = new HashMap<String,HashMap>();
    public static boolean canRunServerJs;

    public static JsContainer js;

    public static Tool tkkTool=new Tool();

    public static Logger logger;

    @SidedProxy(clientSide = "com.twokktwo.tkklib.proxy.ClientProxy",
            serverSide = "com.twokktwo.tkklib.proxy.CommonProxy",
            modId = MODID
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MOD_DIR = new File(event.getModConfigurationDirectory().getParentFile(),NAME);
        if (!MOD_DIR.exists()) {
            MOD_DIR.mkdir();
        }
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
        new mapTool();
        proxy.preInit(event);
        canRunServerJs= configTool.getConfigBool("canRunServerJs");
        TkkGameLib.logger.log(Level.DEBUG,"TkkGameLib has loaded successfully!");
        TkkGameLib.logger.log(Level.INFO,"TkkGameLib:Initialize the MainJs.js");
        js=new JsContainer(getMainJs());
        if(proxy instanceof ClientProxy){
            TkkGameLib.logger.log(Level.INFO,"MainJs.js run function setIsClient(true)");
            js.run("setIsClient",true);
        }else{
            TkkGameLib.logger.log(Level.INFO,"MainJs.js run function setIsClient(true)");
            js.run("setIsClient",false);
        }
        TkkGameLib.logger.log(Level.INFO,"MainJs.js run function FMLPreInitializationEvent");
        js.run("FMLPreInitializationEvent",event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        TkkGameLib.logger.log(Level.INFO,"MainJs.js run function FMLInitializationEvent");
        js.run("FMLInitializationEvent",event);

    }
    @EventHandler
    public void init(FMLServerStartingEvent event)
    {
        new CommandLoader(event);
        TkkGameLib.logger.log(Level.INFO,"MainJs.js run function FMLServerStartingEvent");
        js.run("FMLServerStartingEvent",event);
    }
    @EventHandler
    public void setAboutToStart(FMLServerAboutToStartEvent event) {
        this.server=event.getServer();
        TkkGameLib.logger.log(Level.INFO,"MainJs.js run function FMLServerAboutToStartEvent");
        js.run("FMLServerAboutToStartEvent",event);
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
    public static void print(String str){
        logger.log(Level.INFO,str);
    }
    private static String getMainJs(){
        StringBuffer rt=new StringBuffer();
        try {
            File file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/Main/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File files = new File(file.getCanonicalPath() +"/MainJs.js");
            if(!files.exists()) {
                files.createNewFile();
                FileWriter fw=new FileWriter(files);
                fw.write(getDefaultMainJs());
                fw.close();
            }
            if (files.exists()) {
                FileReader fr = new FileReader(files);
                BufferedReader br = new BufferedReader(fr);
                String temp;
                while ((temp= br.readLine())!=null){
                    rt.append("\n"+temp);
                }
                fr.close();
                br.close();
            }
        } catch (FileNotFoundException e) {
            TkkGameLib.print("getJsFile() error:"+e);
        } catch (IOException e) {
            TkkGameLib.print("getJsFile() error:"+e);
        }
        return rt.toString();
    }
    private static String getDefaultMainJs(){
        StringBuffer sb=new StringBuffer();
        sb.append("var tkkLib=Java.type(\"com.twokktwo.tkklib.TkkGameLib\")");
        sb.append("\nvar isClient=false;");
        sb.append("\nfunction setIsClient(boolean){");
        sb.append("\n\tisClient=boolean");
        sb.append("\n\ttkkLib.print(\"isClient?\"+isClient)");
        sb.append("\n}");
        sb.append("\nfunction FMLPreInitializationEvent(event){tkkLib.print(\"Js FMLPreInitializationEvent run!\")}");
        sb.append("\nfunction FMLInitializationEvent(event){tkkLib.print(\"Js FMLInitializationEvent run!\")}");
        sb.append("\nfunction FMLServerStartingEvent(event){tkkLib.print(\"Js FMLServerStartingEvent run!\")}");
        sb.append("\nfunction FMLServerAboutToStartEvent(event){tkkLib.print(\"Js FMLServerAboutToStartEvent run!\")}");
        return sb.toString();
    }
}
