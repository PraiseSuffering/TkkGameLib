package com.twokktwo.tkklib.proxy;

import com.twokktwo.tkklib.custonActions.ActionEventLoader;
import com.twokktwo.tkklib.entity.EntityRenderLoader;
import com.twokktwo.tkklib.keyBinding.KeyEventLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new KeyEventLoader(event);
        MinecraftForge.EVENT_BUS.register(EntityRenderLoader.class);
        new ActionEventLoader(event);
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        new ActionEventLoader(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
