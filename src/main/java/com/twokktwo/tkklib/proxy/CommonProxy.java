package com.twokktwo.tkklib.proxy;

import com.twokktwo.tkklib.createTab.tabLoader;
import com.twokktwo.tkklib.event.EventLoader;
import com.twokktwo.tkklib.network.networkMain;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        new tabLoader(event);
        new EventLoader(event);
        networkMain.register();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
