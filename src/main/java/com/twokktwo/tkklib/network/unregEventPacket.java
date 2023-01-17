package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.TkkJsEventManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class unregEventPacket implements tkkPacket{
    private static int id;

    public String listenerId;


    public unregEventPacket(){}//解包用

    public unregEventPacket(String id) {
        this.listenerId=id;
    }

    @Override
    public void register(int id){
        unregEventPacket.id=id;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(id);
        networkTool.writeString(out,listenerId);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        this.listenerId=networkTool.readString(in);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        //no
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientHandler(EntityPlayerSP player) {
        TkkGameLib.print("client unregEvent id:"+listenerId);
        try {
            boolean ok=TkkJsEventManager.unregListenerList(listenerId);
            TkkGameLib.print("client unregEvent event ok?:"+ok);
        }catch (Exception e){
            TkkGameLib.print("unregEventPacket error:"+e);
        }
    }
}

