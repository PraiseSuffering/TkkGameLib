package com.twokktwo.tkklib.proxy;

import com.twokktwo.tkklib.createTab.tabLoader;
import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.entity.EntityLoader;
import com.twokktwo.tkklib.event.EventLoader;
import com.twokktwo.tkklib.network.networkMain;
import com.twokktwo.tkklib.timeFunction.TimeFunctionMain;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        new tabLoader(event);
        new EventLoader(event);
        networkMain.register();
        new EntityLoader();
        ActionCapbility.reg();
        TimeFunctionMain.reg(event);
        //MinecraftForge.EVENT_BUS.register(EntityLoader.class);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
