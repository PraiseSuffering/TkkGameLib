package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

import java.io.*;
import java.lang.reflect.Field;

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
