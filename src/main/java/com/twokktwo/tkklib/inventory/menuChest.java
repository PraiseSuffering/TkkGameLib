package com.twokktwo.tkklib.inventory;


import com.google.common.collect.Sets;
import com.twokktwo.tkklib.event.ClickMenu;
import com.twokktwo.tkklib.event.CloseMenu;
import com.twokktwo.tkklib.event.ClosedMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class menuChest extends Container implements Serializable {

    public IInventory lowerChestInventory;
    public int numRows;
    public int tkkDragEvent;
    public int tkkDragModde = -1;
    public final Set tkkDragSlots = new HashSet();
    private int dragEvent;
    private int dragMode = -1;
    private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();

    public menuChest(IInventory p_i1806_1_, IInventory p_i1806_2_,EntityPlayer player){
        this.lowerChestInventory = p_i1806_2_;
        this.numRows = p_i1806_2_.getSizeInventory() / 9;
        p_i1806_2_.openInventory(player);
        int i = (this.numRows - 4) * 18;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(p_i1806_2_, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(p_i1806_1_, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(p_i1806_1_, j, 8 + j * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        return this.lowerChestInventory.isUsableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_){

        ClosedMenu event = new ClosedMenu(p_75134_1_,this);
        MinecraftForge.EVENT_BUS.post(event);
        if(lowerChestInventory instanceof menuInventory) {
            if (((menuInventory) lowerChestInventory).cloneJSOpen) {
                try {
                    ((menuInventory) lowerChestInventory).runJs(new menuInventory.CloseEvent((menuInventory)lowerChestInventory,p_75134_1_,false));
                } catch (Exception e) {
                    p_75134_1_.sendMessage(new TextComponentString("\u00a7crun 'CloseEvent' throws:\u00a72" + e));
                }
            }
        }


        InventoryPlayer inventoryplayer = p_75134_1_.inventory;

        if (inventoryplayer.getItemStack() != null)
        {
            p_75134_1_.dropItem(inventoryplayer.getItemStack(), false);
            inventoryplayer.setItemStack(ItemStack.EMPTY);
        }

        CloseMenu event1 = new CloseMenu(p_75134_1_,this);
        MinecraftForge.EVENT_BUS.post(event1);
        if(lowerChestInventory instanceof menuInventory) {
            if (((menuInventory) lowerChestInventory).cloneJSOpen) {
                try {
                    ((menuInventory) lowerChestInventory).runJs(new menuInventory.CloseEvent((menuInventory)lowerChestInventory,p_75134_1_,true));
                } catch (Exception e) {
                    p_75134_1_.sendMessage(new TextComponentString("\u00a7crun 'CloseEvent' throws:\u00a72" + e));
                }
            }
        }

        this.lowerChestInventory.closeInventory(p_75134_1_);

    }

    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ClickMenu event = new ClickMenu(player,this,slotId,dragType,clickTypeIn);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()){
            if (player instanceof EntityPlayerMP){
                ((EntityPlayerMP)player).sendContainerToPlayer(this);
            }
            return ItemStack.EMPTY;
        }
        if(lowerChestInventory instanceof menuInventory) {
            if (((menuInventory) lowerChestInventory).clickJSOpen) {
                ItemStack RI=ItemStack.EMPTY;
                menuInventory.clickSlotEvent eventJs=new menuInventory.clickSlotEvent((menuInventory) this.lowerChestInventory, slotId, dragType, clickTypeIn, player,RI);
                try {
                    ((menuInventory) lowerChestInventory).runJs(eventJs);
                } catch (Exception e) {
                    player.sendMessage(new TextComponentString("\u00a7crun 'clickEvent' throws:\u00a72" + e));
                }
                RI=eventJs.returnItem;

                if (eventJs.Canceled) {
                    if (player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) player).sendContainerToPlayer(this);
                    }
                    return RI;
                }
            }
        }
        //return null;
        return super.slotClick(slotId,dragType,clickTypeIn,player);
    }




}
