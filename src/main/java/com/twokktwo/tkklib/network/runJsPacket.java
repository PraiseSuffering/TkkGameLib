package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class runJsPacket implements tkkPacket{
    private static int id;

    public String functionName;

    public String Js;

    public runJsPacket(){}//解包用

    public runJsPacket(String functionName, String Js) {
        this.functionName=functionName;
        this.Js=Js;
    }

    @Override
    public void register(int id){
        runJsPacket.id=id;
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
        if(Js.length()<=3){
            TkkGameLib.print("error js file:"+ Js);
            return;
        }
        JsContainer jsc=new JsContainer(JsTool.getJsFile(Js.substring(0,Js.length()-3)));
        jsc.run(functionName,new evalJsPacket.evalEvent(player,false));
        if(jsc.errored){player.sendMessage(new TextComponentString("runJS "+functionName+"  error:"+jsc.print));}
        if(jsc.print!=null && jsc.print.length()!=0){
            player.sendMessage(new TextComponentString(jsc.print));
        }
    }
}
