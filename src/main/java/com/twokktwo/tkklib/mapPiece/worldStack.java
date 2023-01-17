package com.twokktwo.tkklib.mapPiece;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.BlockJs;
import com.twokktwo.tkklib.tool.Tool;
import com.twokktwo.tkklib.tool.miscTool;
import com.twokktwo.tkklib.tool.xyz;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class worldStack implements Serializable {
    public PieceStack[][] piece;
    public transient xyz pos;
    public int size;//世界边长 单位地图块
    public int pieceSize;//地图块边
    public transient Queue<String> jsA = new LinkedList<>();//待运行的js
    public transient Queue<String> jsB = new LinkedList<>();//待运行的js

    public transient Queue<BlockJs> jsQueue = new LinkedList<>();//待运行js列表

    public transient ArrayList<xyz> posA=new ArrayList<>();//js对应的坐标
    public transient ArrayList<xyz> posB=new ArrayList<>();//js对应的坐标
    public transient String nowJsContinue="null";
    public worldStack(xyz pos,int size,int pieceSize){
        this.pos=pos;
        this.piece=new PieceStack[size][size];
        this.size=size;
        this.pieceSize=pieceSize;
    }
    public boolean inWorld(BlockPos pos){
        xyz AA=this.pos;
        xyz AB=this.pos.add(size*pieceSize,0,0);
        xyz BA=this.pos.add(0,0,size*pieceSize);
        if(!(pos.getX()<=AB.x && pos.getX()>=AA.x)){return false;}
        if(!(pos.getZ()<=BA.z && pos.getZ()>=AA.z)){return false;}
        return true;
    }
    public void putPiece(PieceStack[] date){
        if(date.length!=size*size){return;}
        int z=0;
        for (int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                piece[i][j]=date[z];
                z++;
            }
        }
    }
    public void putMap(PieceStack[] mapPiece) {
        int i=0;//x
        int j=0;//z
        int temp=0;
        for(j=0;j<size;j++){
            if((j&2) == 0){i=1;}else{i=0;}
            while (i<size){
                piece[j][i]=mapPiece[temp];
                temp++;
                i+=2;
            }
        }
    }
    public void putAisle(PieceStack[] aislePiece){
        int i=0;//x
        int j=0;//z
        int temp=0;
        for(j=0;j<size;j++){
            if((j&2) == 0){i=0;}else{i=1;}
            while (i<size){
                piece[j][i]=aislePiece[temp];
                temp++;
                i+=2;
            }
        }

    }

    public void runJs() {
        ScriptEngineManager mgr = new ScriptEngineManager(null);
        ScriptEngine engine;
        Invocable inv;
        BlockJs tempJs;
        for(tempJs=jsQueue.poll();tempJs!=null;tempJs=jsQueue.poll()) {
            try {



            } catch (Exception e) {
                TkkGameLib.print("worldStack.runJs() Error:" + miscTool.getError(e) + " js: " + tempJs.js);
            }
        }
    }

    public int[] getPiece(BlockPos pos){
        int[] rt;
        rt = new int[2];
        rt[0]=(pos.getX()-(int)this.pos.x)/pieceSize;
        rt[1]=(pos.getZ()-(int)this.pos.z)/pieceSize;
        return rt;
    }
    public BlockPos getPieceStart(int x,int z){
        return new BlockPos(((int)pos.x)+x*pieceSize,0,((int)pos.z)+z*pieceSize);
    }
    public void updatePiece(World world){
        for(int i=0;i<size;i++){
            for(int k=0;k<size;k++){
                piece[i][k].setPos(this.getPieceStart(i,k));
                piece[i][k].fill(world, Tool.randomNumber(0,3));
            }
        }
    }




















}
