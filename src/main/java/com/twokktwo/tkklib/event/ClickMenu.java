package com.twokktwo.tkklib.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClickMenu extends PlayerEvent {
    public final Container container;

    public final int slot;

    public final int dragType;

    public final ClickType clickTypeIn;

    public ClickMenu(EntityPlayer player, Container container, int slot, int dragType, ClickType clickTypeIn) {
        super(player);
        this.container = container;
        this.slot = slot;
        this.dragType = dragType;
        this.clickTypeIn = clickTypeIn;
    }
}
