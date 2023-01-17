package com.twokktwo.tkklib.brokenWorld;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.world.WorldServer;

public class BrokenWorld {
    private static BrokenWorld instance;

    /*
    * 数据[世界id][y][x][z]=区块
    * */

    public BrokenWorld(){

    }
    public static BrokenWorld Instance(){
        if(instance==null){
            instance=new BrokenWorld();
        }
        return instance;
    }

    public int getWorldId(WorldServer world){
        return world.provider.getDimension();
    }
    public WorldServer getWorld(int id)throws Exception{
        for (WorldServer world : TkkGameLib.server.worlds) {
            if (world.provider.getDimension() == id)
                return world;
        }
        throw new Exception("Unknown dimension id:"+id);
    }




}
