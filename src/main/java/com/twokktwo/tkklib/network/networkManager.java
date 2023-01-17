package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class networkManager {

    @SubscribeEvent //可加注@SideOnly(Side.CLIENT)
    public static void onClientPacketEvent(FMLNetworkEvent.ClientCustomPacketEvent event) {
        decodeDataClient(event.getPacket().payload(), Minecraft.getMinecraft().player);
    }

    @SubscribeEvent
    public static void onServerPacketEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {
        decodeDataServer(event.getPacket().payload(), ((NetHandlerPlayServer)event.getHandler()).player);
    }


    //可加注@SideOnly(Side.CLIENT)
    @SideOnly(Side.CLIENT)
    private static void decodeDataClient(ByteBuf input, EntityPlayerSP player) {
        // 服务器->客户端的解包逻辑
        // 解包的第一步逻辑应在这里出现，即识别包的种类
        // 具体实现应将InputStream重新用MyPacket包装后解耦实现
        Class packetType=PacketManager.getPacketClass(input.readInt());
        if(packetType==null){ return;}
        try {
            tkkPacket packet=((tkkPacket)packetType.newInstance());
            packet.readData(input);
            packet.clientHandler(player);
        } catch (Exception e) {
            e.printStackTrace();
            TkkGameLib.print("decodeDataClient error: "+e.toString());
        }
    }

    //但这里就不能@SideOnly(Side.SERVER)，因为单机游戏也需要这个。
    //这里的server实质是逻辑服务器端。
    private static void decodeDataServer(ByteBuf input, EntityPlayerMP player) {
        // 客户端->服务器的解包逻辑
        // 解包的第一步逻辑应在这里出现，即识别包的种类
        // 具体实现应将InputStream重新用MyPacket包装后解耦实现
        Class packetType=PacketManager.getPacketClass(input.readInt());
        if(packetType==null){ return;}
        try {
            tkkPacket packet=((tkkPacket)packetType.newInstance());
            packet.readData(input);
            packet.serverHandler(player);
        } catch (Exception e) {
            e.printStackTrace();
            TkkGameLib.print("decodeDataServer error: "+e.toString());
        }
    }





    //从这里开始就是一堆封装一样的玩意...

    //向某个维度发包
    public static void sendPacketToDim(tkkPacket pkt, int dim) {
        networkMain.channel.sendToDimension(createFMLProxyPacket(pkt), dim);
    }

    //向某个维度的某个点发包
    public static void sendPacketAroundPos(tkkPacket pkt, int dim, BlockPos pos,Double range) {
        // TargetPoint的构造器为：
        // 维度id x坐标 y坐标 z坐标 覆盖范围
        // 其中，覆盖范围指接受此更新数据包的坐标的范围
        // 之所以要强调最后一个参数是double是因为Kotlin并不会帮你把2隐式转换为kotlin.Double....
        networkMain.channel.sendToAllAround(createFMLProxyPacket(pkt), new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
    }

    //向某个玩家发包
    public static void sendPacketToPlayer(tkkPacket pkt, EntityPlayerMP player) {
        networkMain.channel.sendTo(createFMLProxyPacket(pkt), player);
    }

    //向所有人发包
    public static void sendPacketToAll(tkkPacket pkt) {
        networkMain.channel.sendToAll(createFMLProxyPacket(pkt));
    }

    //向服务器发包，这个给客户端用
    public static void sendPacketToServer(tkkPacket pkt) {
        networkMain.channel.sendToServer(createFMLProxyPacket(pkt));
    }

    //FMLEventChannel经由这个NetworkHandler暴露出来的方法到此为止

    public static FMLProxyPacket createFMLProxyPacket(tkkPacket pkt) {
        ByteBuf buffer = Unpooled.buffer();
        try {
            pkt.writeData(buffer);
        }catch (Exception e){
            TkkGameLib.print("pkt.writeData(buffer); Error: "+e);
        }
        return new FMLProxyPacket(new PacketBuffer(buffer), TkkGameLib.MODID);
    }
}
