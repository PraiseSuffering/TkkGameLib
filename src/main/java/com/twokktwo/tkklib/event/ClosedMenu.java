package com.twokktwo.tkklib.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ClosedMenu extends PlayerEvent {
    public final Container container;

    public ClosedMenu(EntityPlayer player, Container container) {
        super(player);
        this.container = container;
    }
}
