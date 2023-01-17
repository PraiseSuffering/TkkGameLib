package com.twokktwo.tkklib.tool;

import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class xyz implements Serializable {
    public double x;
    public double y;
    public double z;
    public xyz(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public xyz(BlockPos pos){
        this.x=pos.getX();
        this.y=pos.getY();
        this.z=pos.getZ();
    }
    public BlockPos getPos(){
        return new BlockPos(x,y,z);
    }
    public xyz add(double x,double y,double z){
        return new xyz(this.x+x,this.y+y,this.z+z);
    }


}
