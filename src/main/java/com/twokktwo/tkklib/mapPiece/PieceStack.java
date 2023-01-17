package com.twokktwo.tkklib.mapPiece;

import com.twokktwo.tkklib.mapPiece.pieceDate.mapPieceDate;
import com.twokktwo.tkklib.mapPiece.template.tkkEasyTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PieceStack implements Serializable {
    public tkkEasyTemplate block;//方块数据
    public mapPieceDate date;
    public String id;
    public transient BlockPos pos=BlockPos.ORIGIN;
    public PieceStack(Piece piece){
        this.block=piece.block;
        this.date=piece.getDate();
        this.id= piece.id;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(pos.getX());
        out.writeObject(pos.getY());
        out.writeObject(pos.getZ());

    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
        pos=new BlockPos((int)ois.readObject(),(int)ois.readObject(),(int)ois.readObject());
    }

    public void setPos(BlockPos newPos){this.pos=newPos;}

    public BlockPos getPos(){return this.pos;}

    public void fill(World world){
        this.block.template.addBlock(world,pos);
    }
    public void fill(World world,int mirror){
        this.block.template.addBlock(world,pos,mirror);
    }
    public void takeBlock(World world){
        this.block.template.takeBlock(world,this.pos,this.block.template.size);
    }








}
