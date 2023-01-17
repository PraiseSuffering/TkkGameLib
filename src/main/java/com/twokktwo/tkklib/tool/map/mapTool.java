package com.twokktwo.tkklib.tool.map;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.arrayTool;
import com.twokktwo.tkklib.mapPiece.Piece;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class mapTool {
    /*\
    *map{
    * block:map<string,Piece>{id:Piece},
    * string[] aisle{},//过道地图块，权重几就存在几次
    * string[] mapPiece{},//地图块，权重几增加几次
    * string[] fixedMapPiece{}//必定存在地图块，存在几个就增加几次
    *
    * }
    *
    * */
    public static tkkSerializationMap map;
    private static HashMap<String,Piece> block;//方块模板
    private static ArrayList<String> aisle;//通道
    private static ArrayList<String> mapPiece;//地图块
    private static ArrayList<String> fixedMapPiece;//至少拥有的地图块
    public static HashMap<String,String> jsA = new HashMap<String,String>();
    public static HashMap<String,String> jsB = new HashMap<String,String>();
    public static boolean jsIsA=true;
    public mapTool(){
        TkkGameLib.addAutoSaveMap("mapPiece");
        map=TkkGameLib.map.get("mapPiece");
        map.getHashMap().putIfAbsent("block",new HashMap<String, Piece>());
        map.getHashMap().putIfAbsent("aisle",new ArrayList<String>());
        map.getHashMap().putIfAbsent("mapPiece",new ArrayList<String>());
        map.getHashMap().putIfAbsent("fixedMapPiece",new ArrayList<String>());
        block=(HashMap<String, Piece>) map.getHashMap().get("block");
        aisle=(ArrayList<String>) map.getHashMap().get("aisle");
        mapPiece=(ArrayList<String>) map.getHashMap().get("mapPiece");
        fixedMapPiece=(ArrayList<String>) map.getHashMap().get("fixedMapPiece");
    }

    public static  boolean addMapPiece(Piece piece, boolean isAisle){
        if(block.containsKey(piece.getId())){return false;}
        block.put(piece.getId(),piece);
        if(isAisle){//是通道地图块
            for(int x=0;x<piece.weight;x++){
                aisle.add(piece.getId());
            }
        }else{
            for(int x=0;x<piece.weight;x++){
                mapPiece.add(piece.getId());
            }
        }
        for(int x=0;x<piece.atLeast;x++){
            fixedMapPiece.add(piece.getId());
        }
        return true;
    }

    public static void delMapPiece(String id,boolean isAisle){
        Piece temp=block.get(id);
        block.remove(id);
        if(temp==null){return;}
        int waitDel=temp.weight;
        if(isAisle){
            for(int x=aisle.size()-1;x>=0;x--){
                if(Objects.equals(aisle.get(x), temp.id)){
                    aisle.remove(x);
                    waitDel-=1;
                    if(waitDel<=0){break;}
                }
            }
        }else {
            for(int x=mapPiece.size()-1;x>=0;x--){
                if(Objects.equals(mapPiece.get(x), temp.id)){
                    mapPiece.remove(x);
                    waitDel-=1;
                    if(waitDel<=0){break;}
                }
            }
        }
        waitDel=temp.atLeast;
        for(int x=fixedMapPiece.size()-1;x>=0;x--){
            if(fixedMapPiece.get(x).equals(temp.id)){
                fixedMapPiece.remove(x);
                waitDel-=1;
                if(waitDel<=0){break;}
            }
        }
    }

    public static void setMapPiece(Piece piece,boolean isAisle){
        delMapPiece(piece.getId(),isAisle);
        addMapPiece(piece,isAisle);
    }

    public static void see(){
        TkkGameLib.logger.log(Level.DEBUG,"block:"+block.toString());
        TkkGameLib.logger.log(Level.DEBUG,"aisle:"+aisle);
        TkkGameLib.logger.log(Level.DEBUG,"mapPiece:"+mapPiece);
        TkkGameLib.logger.log(Level.DEBUG,"fixedMapPiece:"+fixedMapPiece);
    }

    public static void fill(World world, int interval, int length, BlockPos start){
        ArrayList<String> tempAisle = (ArrayList<String>) aisle.clone();
        ArrayList<String> tempMapPiece = (ArrayList<String>) mapPiece.clone();
        ArrayList<String> mapList = (ArrayList<String>) fixedMapPiece.clone();
        ArrayList<String> aisleList = new ArrayList<String>();
        int mapSize=(int) Math.ceil((length*length)/2.0);
        int aisleSize=(int) Math.floor((length*length)/2.0);
        Collections.shuffle(tempAisle);
        Collections.shuffle(tempMapPiece);
        int haveSize,maxSize;
        while (mapList.size()<mapSize){
            String id=tempMapPiece.get(0);
            maxSize=block.get(id).getMaxSize();
            if(maxSize!=0) {
                haveSize = arrayTool.getObjectSize(mapList, id);
                if (haveSize >= maxSize) {
                    arrayTool.delObject(tempMapPiece, id);
                    continue;
                }
            }
            mapList.add(id);
        }
        Collections.shuffle(mapList);
        //已选择需生成的地图
        while (aisleList.size()<aisleSize){
            String id=tempAisle.get(0);
            maxSize=block.get(id).getMaxSize();
            if(maxSize!=0) {
                haveSize = arrayTool.getObjectSize(aisleList, id);
                if (haveSize >= maxSize) {
                    arrayTool.delObject(tempAisle, id);
                    continue;
                }
            }
            aisleList.add(id);
        }
        Collections.shuffle(aisleList);
        //地图块以及通道块选择完毕
        //接下来放置于世界中
        boolean placeAisle;
        BlockPos pos=start;
        for(int i=0;i<length;i++){
            //纵向循环
            if(i%2==0){
                placeAisle=false;
            }else{
                placeAisle=true;
            }
            for(int k=0;k<length;k++){
                //横向循环
                if(placeAisle){
                    //放置通道块
                    block.get(aisleList.get(0)).block.template.addBlock(world,pos.add(interval*k,0,i*interval));
                    aisleList.remove(0);
                    placeAisle=false;
                }else{
                    //放置地图块
                    block.get(mapList.get(0)).block.template.addBlock(world,pos.add(interval*k,0,i*interval));
                    mapList.remove(0);
                    placeAisle=true;
                }
            }

        }

    }

    public static BlockPos getPieceStart(int interval,BlockPos start,BlockPos target){
        BlockPos dist = new BlockPos(start.getX()-target.getX(),target.getY(),start.getZ()-target.getZ());
        int xDistPiece= (int) Math.floor(dist.getX()/interval);
        int zDistPiece= (int) Math.floor(dist.getZ()/interval);
        return new BlockPos(start.getX()+xDistPiece*interval,start.getY(),start.getZ()+zDistPiece*interval);
    }
















}
