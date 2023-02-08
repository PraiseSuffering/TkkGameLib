package com.twokktwo.tkklib.keyBinding;

import com.google.common.collect.Maps;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class KeyEventLoader {
    public static Map<String,TkkKeyBinding> map = Maps.newHashMap();

    public KeyEventLoader(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this.getClass());
    }
    @SubscribeEvent
    public static void key(InputEvent.KeyInputEvent e){
        for (TkkKeyBinding key:map.values().toArray(new TkkKeyBinding[0])){
            if(key.isPressed()){
                key.jsPressStart();
            }
        }
    }
    @SubscribeEvent
    public static void testTick(TickEvent.ClientTickEvent e){
        for (TkkKeyBinding key:map.values().toArray(new TkkKeyBinding[0])){
            if(key.isKeyDown()){
                key.lastKeyDown=true;
                key.jsPressTick();
            }else{
                if(key.lastKeyDown) {
                    key.lastKeyDown = false;
                    key.jsPressOver();
                }
            }
        }
    }
    public static boolean registerKeyBinding(String id,TkkKeyBinding key){
        if(map.containsKey(id)){return false;}
        map.put(id,key);
        ClientRegistry.registerKeyBinding(key.keyBinding);
        return true;
    }
    public static boolean unregisterKeyBinding(String id){
        //删不掉，会炸
        return false;
        /*
        if(!map.containsKey(id)){return false;}
        KeyBinding[] temp=Minecraft.getMinecraft().gameSettings.keyBindings;
        int NewSize=0;
        for(int i=0;i<temp.length;i++){
            if(temp[i]==map.get(id).keyBinding){
                TkkGameLib.print("2kk2:need remove");
                continue;
            }
            NewSize++;
        }
        KeyBinding[] NewArray=new KeyBinding[NewSize];

        for(int i=0,x=0;i<temp.length;i++){
            if(temp[i]==map.get(id).keyBinding){
                TkkGameLib.print("2kk2:remove");
                continue;
            }
            NewArray[x]=temp[i];
            x++;
        }
        TkkGameLib.print("2kk2:key 对比"+NewArray.length+","+NewArray);
        Minecraft.getMinecraft().gameSettings.keyBindings = NewArray;
        map.remove(id);
        return true;
        */
    }
}
