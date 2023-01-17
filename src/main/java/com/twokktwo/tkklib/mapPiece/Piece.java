package com.twokktwo.tkklib.mapPiece;

import com.twokktwo.tkklib.mapPiece.pieceDate.defaultDate;
import com.twokktwo.tkklib.mapPiece.pieceDate.mapPieceDate;
import com.twokktwo.tkklib.mapPiece.template.tkkEasyTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.Serializable;

public class Piece implements Serializable {
    public tkkEasyTemplate block;//方块数据
    public mapPieceDate date;
    public String id;
    public int weight=1;//权重
    public int maxSize=0;//最多出现几次(0为无限制)
    public int atLeast=0;//最少出现几次
    public Piece(){
    }
    public Piece(tkkEasyTemplate block, String id){
        this.block=block;
        this.id=id;
        this.date=new defaultDate();
    }
    public Piece(tkkEasyTemplate block, String id,mapPieceDate date){
        this.block=block;
        this.id=id;
        this.date=date;
    }
    public Piece(String id, World world, BlockPos start,BlockPos distance){
        this();
        tkkEasyTemplate temp=new tkkEasyTemplate(id);
        temp.template.takeBlock(world,start,distance);
        this.block=temp;
        this.id=id;
        this.date=new defaultDate();
    }
    public void setWeight(int value){this.weight=value;}

    public void setMaxSize(int value){this.maxSize=value;}

    public void setAtLeast(int value){this.atLeast=value;}

    public void setId(String value){this.id=value;}

    public void setDate(mapPieceDate date){this.date=date;}

    public mapPieceDate getDate(){return this.date;}

    public String getId(){return this.id;}

    public int getWeight(){return this.weight;}

    public int getMaxSize(){return this.maxSize;}

    public int getAtLeast(){return this.atLeast;}
}
