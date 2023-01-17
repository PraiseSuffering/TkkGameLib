package com.twokktwo.tkklib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;


public class testGuiTool {
    public static void test(EntityPlayer p){
        if(FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) {
            FMLCommonHandler.instance().showGuiScreen(new testClientGui());
        }
    }
    public static void testB(EntityPlayer p){
    }
}
