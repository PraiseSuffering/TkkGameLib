package com.twokktwo.tkklib.network;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.tool.miscTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public class ActionCapbilityUpdataPacker implements tkkPacket{
    private static int id;
    public int entityID;
    public NBTTagCompound cap;
    public boolean putJS=false;
    public ActionCapbilityUpdataPacker(){}
    public ActionCapbilityUpdataPacker(ActionCapbility.ActionCap cap, int entityID){
        this.cap=(NBTTagCompound) ActionCapbility.actionCap.getStorage().writeNBT(ActionCapbility.actionCap,cap,null);
        this.entityID=entityID;
    }
    @Override
    public void register(int id) {
        ActionCapbilityUpdataPacker.id=id;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        out.writeInt(id);
        out.writeInt(entityID);
        out.writeBoolean(putJS);
        networkTool.writeString(out,cap.toString());
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        entityID=in.readInt();
        putJS=in.readBoolean();
        try {
            cap=JsonToNBT.getTagFromJson(networkTool.readString(in));
        } catch (NBTException e) {
            cap=new NBTTagCompound();
            TkkGameLib.print("[error]ActionCapbilityUpdataPacker:"+miscTool.getError(e));
        }
    }

    @Override
    public void serverHandler(EntityPlayerMP player) {

    }

    @Override
    public void clientHandler(EntityPlayerSP player) {
        Entity entity= Minecraft.getMinecraft().world.getEntityByID(entityID);
        if(entity==null){
            return;
        }
        if(putJS){
            entity.getCapability(ActionCapbility.actionCap,null).putJS(cap.getString("JS"));
        }
        ActionCapbility.actionCap.getStorage().readNBT(ActionCapbility.actionCap,entity.getCapability(ActionCapbility.actionCap,null),null,cap);

    }
}
