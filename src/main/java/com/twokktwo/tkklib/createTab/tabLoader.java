package com.twokktwo.tkklib.createTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class tabLoader {
    public static CreativeTabs sign;

    public tabLoader(FMLPreInitializationEvent event)
    {
        sign = new signItemTab();
    }
}
