package com.twokktwo.tkklib.event;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLoader {

    public EventLoader(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void saveDate(WorldEvent.Save event){
        //TkkGameLib.logger.log(Level.INFO,"TkkGameLib:save dateMap!");
        TkkGameLib.dateMap.save();
        for(String mapName:TkkGameLib.map.keySet()){
            ((tkkSerializationMap)TkkGameLib.map.get(mapName)).save();
            //TkkGameLib.logger.log(Level.INFO,"TkkGameLib:save "+mapName);
        }
    }
}
