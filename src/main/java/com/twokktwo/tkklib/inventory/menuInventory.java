package com.twokktwo.tkklib.inventory;

import com.google.common.collect.Lists;
import com.twokktwo.tkklib.js.JsContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


public class menuInventory implements IInventory {
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;
    public String id;
    public boolean JSOpen;
    public JsContainer JS=new JsContainer("");

    public String print="";


    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        NBTTagList items=new NBTTagList();
        for(ItemStack item:this.inventoryContents){
            items.appendTag(item.writeToNBT(new NBTTagCompound()));
        }
        nbt.setTag("items",items);
        nbt.setTag("inventoryTitle",new NBTTagString(inventoryTitle));
        nbt.setTag("slotsCount",new NBTTagInt(slotsCount));
        nbt.setTag("hasCustomName",new NBTTagInt((hasCustomName)?1:0));
        nbt.setTag("id",new NBTTagString(id));
        nbt.setTag("JSOpen",new NBTTagInt((JSOpen)?1:0));
        nbt.setTag("JS",new NBTTagString(JS.js));

        return nbt;
    }
    public menuInventory(NBTTagCompound nbt){
        this.inventoryTitle=nbt.getString("inventoryTitle");
        this.slotsCount=nbt.getInteger("slotsCount");
        this.hasCustomName=(nbt.getInteger("hasCustomName")==1);
        this.id=nbt.getString("id");

        this.JSOpen=(nbt.getInteger("JSOpen")==1);
        this.JS=new JsContainer(nbt.getString("JS"));


        this.inventoryContents=NonNullList.<ItemStack>withSize(this.slotsCount, ItemStack.EMPTY);
        NBTTagList items=(NBTTagList)nbt.getTag("items");
        int i=0;
        for(NBTBase item:items){
            this.inventoryContents.set(i,new ItemStack((NBTTagCompound) item));
            i++;
        }
    }

    public menuInventory(String title, boolean customName, int slotCount)
    {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = NonNullList.<ItemStack>withSize(slotCount, ItemStack.EMPTY);
    }

    @SideOnly(Side.CLIENT)
    public menuInventory(ITextComponent title, int slotCount)
    {
        this(title.getUnformattedText(), true, slotCount);
    }

    public void runJs(clickSlotEvent event) throws Exception {
        JS.run("clickEvent", event);
        if(JS.errored){
            JS.errored=false;
            throw new Exception(JS.print);
        }
    }
    public void runJs(CloseEvent event) throws Exception {
        JS.run("CloseEvent", event);
        if(JS.errored){
            JS.errored=false;
            throw new Exception(JS.print);
        }
    }

    public void addInventoryChangeListener(IInventoryChangedListener listener)
    {
        if (this.changeListeners == null)
        {
            this.changeListeners = Lists.<IInventoryChangedListener>newArrayList();
        }

        this.changeListeners.add(listener);
    }

    public void removeInventoryChangeListener(IInventoryChangedListener listener)
    {
        this.changeListeners.remove(listener);
    }

    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.inventoryContents.size() ? (ItemStack)this.inventoryContents.get(index) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < this.slotsCount; ++i)
        {
            ItemStack itemstack1 = this.getStackInSlot(i);

            if (itemstack1.isEmpty())
            {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.grow(k);
                    itemstack.shrink(k);

                    if (itemstack.isEmpty())
                    {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
        {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack itemstack = this.inventoryContents.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventoryContents.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public int getSizeInventory()
    {
        return this.slotsCount;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.inventoryContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public String getName()
    {
        return this.inventoryTitle;
    }

    public boolean hasCustomName()
    {
        return this.hasCustomName;
    }

    public void setCustomName(String inventoryTitleIn)
    {
        this.hasCustomName = true;
        this.inventoryTitle = inventoryTitleIn;
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void markDirty()
    {
        if (this.changeListeners != null)
        {
            for (int i = 0; i < this.changeListeners.size(); ++i)
            {
                ((IInventoryChangedListener)this.changeListeners.get(i)).onInventoryChanged(this);
            }
        }
        if(JSOpen){
            JS.run("markDirty",this);
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        this.inventoryContents.clear();
    }

    public static class CloseEvent{
        public EntityPlayer player;
        public menuInventory inventory;
        public boolean end;
        public CloseEvent(menuInventory inventory,EntityPlayer player,boolean e){
            this.inventory=inventory;
            this.player=player;
            this.end=e;
        }
        public void sendMessage(String message){this.player.sendMessage(new TextComponentString(message));}
        /*
        function CloseEvent(e){
            e.sendMessage('player:'+e.player)//玩家
            e.sendMessage('inventory:'+e.inventory)//menuInventory
            e.sendMessage('end:'+e.end)//是关闭后还是关闭前(重要！)
            e.sendMessage(e.player.field_71071_by.func_70445_o())//指针物品
        }
        */

    }

    public static class clickSlotEvent{
        public int slotId;
        public int dragType;
        public ClickType clickTypeIn;
        public EntityPlayer player;
        public boolean Canceled;
        public menuInventory inventory;
        public ItemStack returnItem;
        public clickSlotEvent(menuInventory inventory,int slotId,int dragType,ClickType clickTypeIn,EntityPlayer player,ItemStack returnItem){
            this.inventory=inventory;
            this.slotId=slotId;
            this.dragType=dragType;
            this.clickTypeIn=clickTypeIn;
            this.player=player;
            this.Canceled=false;
            this.returnItem=returnItem;
        }
        public void setCanceled(boolean bool){this.Canceled=bool;}
        public void sendMessage(String message){this.player.sendMessage(new TextComponentString(message));}

        /*
        function clickEvent(e){
            e.setCanceled(true);//设置取消
            e.sendMessage('slotId:'+e.slotId)//点击位置(-999为gui外)
            e.sendMessage('dragType:'+e.dragType)//我不知道这是干什么的
            e.sendMessage('clickTypeIn:'+e.clickTypeIn)//点击类型,比如单点 shift点
            e.sendMessage('player:'+e.player)//玩家
            e.sendMessage('inventory:'+e.inventory)//menuInventory
            e.sendMessage(e.player.field_71071_by.func_70445_o())//指针物品
        }
        */

    }

}
