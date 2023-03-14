package com.twokktwo.tkklib.network;

import com.google.common.collect.Lists;
import com.twokktwo.tkklib.TkkGameLib;

import java.util.List;

public class PacketManager {
    public static List<Class> packetList= Lists.newArrayList();

    public static int regPacket(tkkPacket packet){
        int id=packetList.size();
        packetList.add(packet.getClass());
        return id;
    }

    public static void regPacketEasy(tkkPacket c){
        c.register(regPacket(c));
    }

    public static void register(){
        regPacketEasy(new testPacker());
        regPacketEasy(new regEventPacket());
        regPacketEasy(new evalJsPacket());
        regPacketEasy(new unregEventPacket());
        regPacketEasy(new runJsPacket());
        regPacketEasy(new ActionCapbilityUpdataPacker());
    }

    public static Class getPacketClass(int id) {
        if (id >= packetList.size()) {
            TkkGameLib.print("ERROR PACKER:"+id);
            return null;
        }
        return packetList.get(id);
    }

}
