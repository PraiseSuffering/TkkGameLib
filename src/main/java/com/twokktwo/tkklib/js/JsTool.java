package com.twokktwo.tkklib.js;

import com.google.common.collect.Lists;
import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.miscTool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JsTool {
    public static void registerListener(Event event, int priority, JsContainer listener) throws NoSuchFieldException, IllegalAccessException {
        registerListener(event,priority,new jsListener(listener));
    }

    public static void registerListener(Event event, int priority, IEventListener listener) throws IllegalAccessException, NoSuchFieldException {
        EventPriority Priority=EventPriority.values()[priority];
        Field busid = MinecraftForge.EVENT_BUS.getClass().getDeclaredField("busID");
        busid.setAccessible(true);
        int busID=busid.getInt(MinecraftForge.EVENT_BUS);
        event.getListenerList().register(busID,Priority,listener);
    }

    public static void unregisterListener(Event event,IEventListener listener)throws IllegalAccessException, NoSuchFieldException{
        Field busid = MinecraftForge.EVENT_BUS.getClass().getDeclaredField("busID");
        busid.setAccessible(true);
        int busID=busid.getInt(MinecraftForge.EVENT_BUS);
        event.getListenerList().unregister(busID,listener);
    }

    public static String[] getPluginJs(){
        ArrayList<String> returnedVault= Lists.newArrayList();
        try {
            File file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/plugin/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File[] list=file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".js")){return true;}
                    return false;
                }
            });
            for(File JSfile:list){
                InputStreamReader fr = new InputStreamReader(new FileInputStream(JSfile), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);
                StringBuffer jsCode=new StringBuffer();
                String temp;
                while ((temp= br.readLine())!=null){
                    jsCode.append("\n"+temp);
                }
                fr.close();
                br.close();
                returnedVault.add(jsCode.toString());
            }
        }catch (Exception e){
            TkkGameLib.print("getPluginJs error:"+ miscTool.getError(e));
        }
        String[] rt=new String[returnedVault.size()];
        for(int i = 0;i<returnedVault.size();i++){
            rt[i]=returnedVault.get(i);
        }
        return rt;
    }

    public static JsContainer getJS(String fileName){
        return new JsContainer(getJsFile(fileName));
    }

    public static String getJsFile(String filename){
        StringBuffer rt=new StringBuffer();
        try {
            File file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/js/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File files = new File(file.getCanonicalPath() +"/"+ filename + ".js");
            if(!files.exists()) {
                files.createNewFile();
            }
            if (files.exists()) {
                InputStreamReader fr = new InputStreamReader(new FileInputStream(files), StandardCharsets.UTF_8);
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

    public static class jsListener implements IEventListener{
        public JsContainer js;

        public jsListener(JsContainer js){
            this.js=js;
        }
        @Override
        public void invoke(Event event) {
            js.run("main",event);
            if(js.errored){
                TkkGameLib.print(js.print);
            }
        }
    }
}
