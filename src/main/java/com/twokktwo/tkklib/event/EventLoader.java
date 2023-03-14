package com.twokktwo.tkklib.event;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.jsStorageTool;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;

public class EventLoader {
    public long lastSaveTime=0;
    public static int clientTime=0;
    public EventLoader(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void saveDate(WorldEvent.Save event){
        long nowTime=System.nanoTime();
        if(!(nowTime-lastSaveTime>500000000)){
            return;
        }else{
            lastSaveTime=nowTime;
        }
        TkkGameLib.logger.log(Level.INFO,"TkkGameLib:save dateMap!");
        jsStorageTool.dateMap.save();
        for(String mapName:jsStorageTool.map.keySet()){
            ((tkkSerializationMap)jsStorageTool.map.get(mapName)).save();
            TkkGameLib.logger.log(Level.INFO,"TkkGameLib:save "+mapName);
        }
    }
    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e){
        clientTime++;
    }
}
