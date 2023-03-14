package com.twokktwo.tkklib.custonActions.Capability;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.network.ActionCapbilityUpdataPacker;
import com.twokktwo.tkklib.network.networkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.concurrent.Callable;

public class ActionCapbility {
    @CapabilityInject(ActionContainer.class)
    public static Capability<ActionContainer> actionCap;
    public static void reg(){
        CapabilityManager.INSTANCE.register(ActionContainer.class,new Storage(),new Factory());
        MinecraftForge.EVENT_BUS.register(ActionCapbility.class);
    }
    public static void upDataCap(Entity entity,int range,boolean updateJS){
        World world=entity.getEntityWorld();
        List<EntityPlayerMP> playerMPS=entity.getEntityWorld().getEntitiesWithinAABB(EntityPlayerMP.class,entity.getEntityBoundingBox().grow(range,range,range));
        if(playerMPS.isEmpty()){return;}
        ActionCapbilityUpdataPacker packer=new ActionCapbilityUpdataPacker((ActionCap) entity.getCapability(actionCap,null),entity.getEntityId());
        packer.putJS=updateJS;
        for (EntityPlayerMP player:playerMPS) {
            networkManager.sendPacketToPlayer(packer,player);
        }
    }
    public static void upDataCap(Entity entity,EntityPlayerMP playerMP,boolean updateJS){
        ActionCapbilityUpdataPacker packer=new ActionCapbilityUpdataPacker((ActionCap) entity.getCapability(actionCap,null),entity.getEntityId());
        packer.putJS=updateJS;
        networkManager.sendPacketToPlayer(packer,playerMP);
    }
    private static class Storage implements Capability.IStorage<ActionContainer> {
        @Override
        public NBTBase writeNBT(Capability<ActionContainer> cap, ActionContainer instance, EnumFacing side) {
            NBTTagCompound temp=new NBTTagCompound();
            temp.setBoolean("enable",instance.getEnable());
            temp.setBoolean("replace",instance.getReplace());
            temp.setInteger("int",instance.getTempDataInt());
            temp.setString("String",instance.getTempDataString());
            temp.setString("JS",instance.getJS());
            temp.setTag("nbt",instance.getNBT());
            return temp;
        }
        @Override
        public void readNBT(Capability<ActionContainer> cap, ActionContainer instance, EnumFacing side, NBTBase data) {
            if (data instanceof NBTTagCompound) {
                instance.setEnable(((NBTTagCompound) data).getBoolean("enable"));
                instance.setReplace(((NBTTagCompound) data).getBoolean("replace"));
                instance.setJS(((NBTTagCompound) data).getString("JS"));
                instance.setTempDataString(((NBTTagCompound) data).getString("String"));
                instance.setTempDataInt(((NBTTagCompound) data).getInteger("int"));
                instance.setNBT(((NBTTagCompound) data).getCompoundTag("nbt"));
            }
        }
    }
    private static class Factory implements Callable<ActionContainer> {

        @Override
        public ActionContainer call() throws Exception {
            return new ActionCap();
        }
    }
    public static class ActionCap implements ActionContainer{
        public boolean enable=false;
        public boolean replace=false;
        public String JS="null";
        public int intTempData=0;
        public String stringTempData="null";
        public NBTTagCompound nbt=new NBTTagCompound();
        public JsContainer jsContainer;
        public ActionCap(){
        }
        @Override
        public void setEnable(boolean bool) {
            this.enable=bool;
        }

        @Override
        public boolean getEnable() {
            return this.enable;
        }
        @Override
        public void setReplace(boolean bool) {
            this.replace=bool;
        }

        @Override
        public boolean getReplace() {
            return this.replace;
        }

        @Override
        public void setJS(String js) {
            if(js.equals(this.JS) && jsContainer!=null){return;}
            this.JS=js;
            jsContainer=new JsContainer(JsTool.getJsFile(js));
        }
        @Override
        public void putJS(String js) {
            this.JS=js;
            jsContainer=new JsContainer(JsTool.getJsFile(js));
        }

        @Override
        public String getJS() {
            return this.JS;
        }

        @Override
        public void setTempDataInt(int time) {
            this.intTempData=time;
        }

        @Override
        public int getTempDataInt() {
            return this.intTempData;
        }

        @Override
        public void setTempDataString(String temp) {
            this.stringTempData=temp;
        }

        @Override
        public String getTempDataString() {
            return this.stringTempData;
        }
        @Override
        public Object runJs(String functionName,Object... args){
            if(jsContainer==null){return null;}
            if(jsContainer.errored){TkkGameLib.print("[error] ActionCapbility:"+jsContainer.print);}
            return jsContainer.run(functionName,args);
        }
        public NBTTagCompound getNBT(){return nbt;}
        public void setNBT(NBTTagCompound nbt){this.nbt=nbt;}

    }
    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private ActionContainer cap = new ActionCap();
        private Capability.IStorage<ActionContainer> storage = actionCap.getStorage();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing)
        {
            return actionCap.equals(capability);
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing)
        {
            if (actionCap.equals(capability))
            {
                @SuppressWarnings("unchecked")
                T result = (T) cap;
                return result;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("actionData", storage.writeNBT(actionCap, cap, null));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            NBTTagCompound list = (NBTTagCompound) compound.getTag("actionData");
            storage.readNBT(actionCap, cap, null, list);
        }
    }
    @SubscribeEvent
    public static void addCap(AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(new ResourceLocation(TkkGameLib.MODID, "action_data"), new Provider());
    }
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if(event.getTarget().hasCapability(actionCap,null)){
            if(event.getTarget().getCapability(actionCap,null).getJS().equals("")){return;}
            upDataCap(event.getTarget(),(EntityPlayerMP) event.getEntityPlayer(),false);
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.hasCapability(actionCap, null)) {
            upDataCap(event.player,(EntityPlayerMP) event.player,false);
        }
    }
}
