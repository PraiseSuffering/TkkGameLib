package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class networkMain {

    public static final FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(TkkGameLib.MODID);

    public static void register(){
        channel.register(networkManager.class);
        PacketManager.register();
    }

}
