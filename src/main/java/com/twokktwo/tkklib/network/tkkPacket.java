package com.twokktwo.tkklib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;

public interface tkkPacket {




    //注册
    //保存id
    void register(int id);

    //写数据
    //此处应在首先写入id
    void writeData(ByteBuf out) throws IOException;

    //读数据
    //此处不需读id
    void readData(ByteBuf in) throws IOException;

    //服务端处理
    void serverHandler(EntityPlayerMP player);

    //客户端处理
    void clientHandler(EntityPlayerSP player);
}