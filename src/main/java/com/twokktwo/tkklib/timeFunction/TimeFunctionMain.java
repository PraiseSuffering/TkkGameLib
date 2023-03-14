package com.twokktwo.tkklib.timeFunction;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Vector;

public class TimeFunctionMain {

    public static Vector<TimeFunction> SERVER_TIME_FUNCTION_LIST=new Vector<>();
    public static Vector<TimeFunction> CLIENT_TIME_FUNCTION_LIST=new Vector<>();

    public static void reg(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(TimeFunctionMain.class);
    }
    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public static void serverTick(TickEvent.ClientTickEvent e){
        Iterator<TimeFunction> iterator = SERVER_TIME_FUNCTION_LIST.iterator();
        while (iterator.hasNext()) {
            TimeFunction fn = iterator.next();
            fn.life--;
            fn.call("tick",fn);
            if (fn.life<=0) {
                iterator.remove();
            }
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e){
        Iterator<TimeFunction> iterator = CLIENT_TIME_FUNCTION_LIST.iterator();
        while (iterator.hasNext()) {
            TimeFunction fn = iterator.next();
            fn.life--;
            fn.call("tick",fn);
            if (fn.life<=0) {
                iterator.remove();
            }
        }
    }


    public static TimeFunction addServerTimeFunction(int life,boolean save,String jsCode){
        TimeFunction fn=new TimeFunction(jsCode);
        fn.life=life;
        fn.save=save;
        addServerTimeFunction(fn);
        return fn;
    }
    public static TimeFunction addClientTimeFunction(int life,boolean save,String jsCode){
        TimeFunction fn=new TimeFunction(jsCode);
        fn.life=life;
        fn.save=save;
        addClientTimeFunction(fn);
        return fn;
    }
    public static void addServerTimeFunction(TimeFunction fn){
        SERVER_TIME_FUNCTION_LIST.add(fn);
    }
    public static void addClientTimeFunction(TimeFunction fn){
        CLIENT_TIME_FUNCTION_LIST.add(fn);
    }
}
