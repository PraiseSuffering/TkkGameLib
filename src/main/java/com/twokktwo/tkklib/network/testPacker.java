package com.twokktwo.tkklib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

public class testPacker implements tkkPacket{
    private static int id;

    public String message;

    public testPacker(){}//解包用

    public testPacker(String message){
        this.message=message;
    }

    @Override
    public void register(int id){
        testPacker.id=id;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(id);
        networkTool.writeString(out,message);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        this.message=networkTool.readString(in);
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {
        player.sendMessage(new TextComponentString("server:"+message));
    }

    @Override
    public void clientHandler(EntityPlayerSP player) {
        player.sendMessage(new TextComponentString("client:"+message));
    }
}
