package com.twokktwo.tkklib.world;

import com.twokktwo.tkklib.tool.configTool;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WorldManager {
    public static int WORLD_ID_ORIGIN=2222;
    public static int NOW_WORLD_ID;
    public static DimensionType dimType;
    public static void preInit(FMLPreInitializationEvent e){
        WORLD_ID_ORIGIN= configTool.getConfigInt("WORLD_ID_ORIGIN",2222);
        NOW_WORLD_ID=WORLD_ID_ORIGIN;
        dimType=DimensionType.register("tkk", "_tkk", WORLD_ID_ORIGIN, WorldProvider.class, false);
    }
    public static void save(){

    }
    public static DimensionType registerDIM(){
        int id=NOW_WORLD_ID;
        DimensionType dimType = DimensionType.register("broken_"+id, "_broken"+id, id, WorldProvider.class, false);
        DimensionManager.registerDimension(id, dimType);
        NOW_WORLD_ID++;
        return dimType;
    }

}
