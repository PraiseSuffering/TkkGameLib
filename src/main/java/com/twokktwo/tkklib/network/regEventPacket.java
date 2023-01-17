package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.TkkJsEventManager;
import com.twokktwo.tkklib.js.JsTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class regEventPacket implements tkkPacket{
    private static int id;

    public String listenerId;

    public String listenerJs;

    public String eventClass;

    public int priority;

    public regEventPacket(){}//解包用

    public regEventPacket(String id,String event,int priority,String js) {
        this.listenerId=id;
        this.eventClass=event;
        this.listenerJs=js;
        this.priority=priority;
    }

    @Override
    public void register(int id){
        regEventPacket.id=id;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(id);
        networkTool.writeString(out,listenerId);
        networkTool.writeString(out,eventClass);
        networkTool.writeString(out,listenerJs);
        out.writeInt(priority);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        this.listenerId=networkTool.readString(in);
        this.eventClass=networkTool.readString(in);
        this.listenerJs=networkTool.readString(in);
        this.priority=in.readInt();
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        //no
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientHandler(EntityPlayerSP player) {
        TkkGameLib.print("client regEvent event:"+eventClass+",id:"+listenerId+",priority:"+priority+",js:"+listenerJs);
        if(!TkkGameLib.canRunServerJs){
            TkkGameLib.print("need canRunServerJs == true");
            return;
        }
        try {
            Class event=Class.forName(eventClass);
            Event Event=(Event) event.newInstance();
            boolean ok= TkkJsEventManager.regListenerList(listenerId,Event,priority,new JsTool.jsListener(new JsContainer(listenerJs)));
            TkkGameLib.print("client regEvent event ok?:"+ok);
        }catch (Exception e){
            TkkGameLib.print("regEventPacket error:"+e);
        }
    }
}

