package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.inventory.menuChest;
import com.twokktwo.tkklib.inventory.menuInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketOpenWindow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class tkkFastChestMenu implements Serializable {
    public transient menuInventory inventory;

    public tkkFastChestMenu(menuInventory inv){
        inventory=inv;
    }
    public tkkFastChestMenu(int size, String Name, boolean hasName, String id){
        if(Name==null){hasName=false;}else{hasName=true;}
        inventory=new menuInventory(Name,hasName,size);
        inventory.id=id;
    }
    public void setItem(int slot, ItemStack item){
        inventory.setInventorySlotContents(slot,item);
    }
    public ItemStack delItem(int slot){
        return inventory.removeStackFromSlot(slot);
    }
    public ItemStack getItem(int slot){
        return inventory.getStackInSlot(slot);
    }
    public int getSize(){
        return inventory.getSizeInventory();
    }
    public void openForPlayer(EntityPlayerMP player){
        menuChest chest = new menuChest(player.inventory,this.inventory,player);
        this.displayGUIChest(player,chest);

    }
    public void displayGUIChest(EntityPlayerMP playerMP,menuChest chest) {
        if (playerMP.openContainer != playerMP.inventoryContainer){
            playerMP.closeScreen();
        }

        playerMP.getNextWindowId();
        playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", chest.lowerChestInventory.getDisplayName(), chest.lowerChestInventory.getSizeInventory()));
        playerMP.openContainer = chest;
        playerMP.openContainer.windowId = playerMP.currentWindowId;
        playerMP.openContainer.addListener(playerMP);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
    }
    public tkkFastChestMenu clone(){
        return new tkkFastChestMenu(new menuInventory(this.inventory.writeToNBT(new NBTTagCompound())));
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(inventory.writeToNBT(new NBTTagCompound()).toString());
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, NBTException {
        String strNBT=(String) ois.readObject();
        inventory= new menuInventory(JsonToNBT.getTagFromJson(strNBT));
        ois.defaultReadObject();
    }


}
