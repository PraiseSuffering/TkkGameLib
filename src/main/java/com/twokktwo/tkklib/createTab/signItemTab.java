package com.twokktwo.tkklib.createTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class signItemTab extends CreativeTabs {
    public signItemTab(){
        super("signItem");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemById(1));
    }
}