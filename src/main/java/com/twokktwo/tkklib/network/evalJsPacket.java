package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.JsContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class evalJsPacket  implements tkkPacket{
    private static int id;

    public String functionName;

    public String Js;

    public evalJsPacket(){}//解包用

    public evalJsPacket(String functionName,String Js) {
        this.functionName=functionName;
        this.Js=Js;
    }

    @Override
    public void register(int id){
        evalJsPacket.id=id;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(id);
        networkTool.writeString(out,functionName);
        networkTool.writeString(out,Js);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        this.functionName=networkTool.readString(in);
        this.Js=networkTool.readString(in);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        //没想好怎么处理安全问题，先不做
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientHandler(EntityPlayerSP player) {
        TkkGameLib.print("client eval fn:"+functionName+",js:"+Js);
        if(!TkkGameLib.canRunServerJs){
            TkkGameLib.print("need canRunServerJs == true");
            return;
        }
        JsContainer jsc=new JsContainer(Js);
        jsc.run(functionName,new evalEvent(player,false));
        if(jsc.errored){player.sendMessage(new TextComponentString("runJS "+functionName+"  error:"+jsc.print));}
        if(jsc.print!=null && jsc.print.length()!=0){
            player.sendMessage(new TextComponentString("command.tkkJS.JsClientPrint"+jsc.print));
        }
    }

    public static class evalEvent{
        public EntityPlayer player;
        public boolean isServer;
        public evalEvent(EntityPlayer player,boolean isServer){
            this.player=player;
            this.isServer=isServer;
        }
    }
}
