package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.Command.CommandLoaderTkkJs;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.TkkJsEventManager;
import com.twokktwo.tkklib.keyBinding.KeyEventLoader;
import com.twokktwo.tkklib.keyBinding.TkkKeyBinding;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

public class regTool {
    public static boolean regListener(String id, Event event, int priority, IEventListener listener){
        return TkkJsEventManager.regListenerList(id,event,priority,listener);
    }
    public static boolean unregListener(String id){
        return TkkJsEventManager.unregListenerList(id);
    }

    public static boolean regCommand(String id, ICommand command){
        return CommandLoaderTkkJs.registerCommand(id,command);
    }
    public static boolean unregCommand(String id){
        return CommandLoaderTkkJs.unregisterCommand(id);
    }

    public static boolean regKey(String id,JsContainer js,String name,String usableRange,String AttachedKey,int keyCode,String tab){

        TkkKeyBinding key=new TkkKeyBinding(name,usableRange,AttachedKey,keyCode,tab);
        key.setJS(js);
        return KeyEventLoader.registerKeyBinding(id,key);
    }

}
