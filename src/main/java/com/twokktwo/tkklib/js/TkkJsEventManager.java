package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

import java.util.HashMap;

public class TkkJsEventManager {
    private static final HashMap<String, listener> listenerList = new HashMap<>();

    public static boolean hasListener(String id){
        return listenerList.containsKey(id);
    }

    public static boolean regListenerList(String id, Event event, int priority, IEventListener listener){
        if(hasListener(id)){
            return false;
        }
        try {
            JsTool.registerListener(event,priority,listener);
        }catch (Exception e){
            TkkGameLib.print("JsEventManager.regListenerList error:"+e);
            return false;
        }
        listenerList.put(id,new listener(event,listener));
        return true;
    }

    public static String[] getRegisteredID(){
        return listenerList.keySet().toArray(new String[0]);
    }

    public static boolean unregListenerList(String id){
        if(!hasListener(id)){
            return false;
        }
        try {
            JsTool.unregisterListener(listenerList.get(id).event,listenerList.get(id).listener);
        }catch (Exception e){
            TkkGameLib.print("JsEventManager.unregListenerList error:"+e);
            return false;
        }
        listenerList.remove(id);
        return true;
    }

    public static class listener{
        public Event event;
        public IEventListener listener;
        public listener(Event event,IEventListener listener){
            this.event=event;
            this.listener=listener;
        }
    }
}
