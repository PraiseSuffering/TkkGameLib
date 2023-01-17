package com.twokktwo.tkklib.mapPiece.pieceBank;

import com.twokktwo.tkklib.mapPiece.Piece;
import com.twokktwo.tkklib.mapPiece.PieceStack;
import com.twokktwo.tkklib.tool.arrayTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class piecePool implements Serializable {
    private pieceList list;//地图块数据
    private ArrayList<String> aisle;//通道
    private ArrayList<String> mapPiece;//地图块
    private ArrayList<String> fixedMapPiece;//至少拥有的地图块

    public boolean addAisle(Piece date,String id,int weight,int maxSize,int atLeast){
        if(!list.addPiece(id,date)){return false;}//添加块数据，如果添加失败则返回false
        date.weight=weight;
        date.maxSize=maxSize;
        date.atLeast=atLeast;
        for(int x=0;x<date.weight;x++){
            aisle.add(date.getId());
        }
        return true;
    }
    public boolean addMap(Piece date,String id,int weight,int maxSize,int atLeast){
        if(!list.addPiece(id,date)){return false;}//添加块数据，如果添加失败则返回false
        date.weight=weight;
        date.maxSize=maxSize;
        date.atLeast=atLeast;
        for(int x=0;x<date.weight;x++){
            mapPiece.add(date.getId());
        }
        for(int x=0;x<date.atLeast;x++){
            fixedMapPiece.add(date.getId());
        }
        return true;
    }
    public void delPiece(String id){
        Piece temp=list.getPiece(id);
        list.removePiece(id);
        if(temp==null){return;}
        for(int x=aisle.size()-1;x>=0;x--){
            if(Objects.equals(aisle.get(x), temp.id)){
                aisle.remove(x);
            }
        }
        for(int x=mapPiece.size()-1;x>=0;x--){
            if(Objects.equals(mapPiece.get(x), temp.id)){
                mapPiece.remove(x);
            }
        }
        for(int x=fixedMapPiece.size()-1;x>=0;x--){
            if(fixedMapPiece.get(x).equals(temp.id)){
                fixedMapPiece.remove(x);
            }
        }
    }
    public PieceStack[][] randomTwoArray(int length){
        PieceStack[][] array = new PieceStack[length][length];
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
            maxSize=list.getPiece(id).getMaxSize();
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
            maxSize=list.getPiece(id).getMaxSize();
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
        int a=0;
        boolean placeAisle;
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
                    //通道块
                    array[i][k]=new PieceStack(list.getPiece(aisleList.get(0)));
                    a++;
                    aisleList.remove(0);
                    placeAisle=false;
                }else{
                    //地图块
                    array[i][k]=new PieceStack(list.getPiece(mapList.get(0)));
                    a++;
                    mapList.remove(0);
                    placeAisle=true;
                }
            }
        }
        return array;
    }

    public PieceStack[] randomMap(PieceStack[] have,int length){
        if(have.length>=length*length){return have;}
        ArrayList<PieceStack> listMap=new ArrayList<PieceStack>();//地图
        ArrayList<String> tempMapPiece = (ArrayList<String>) mapPiece.clone();
        ArrayList<String> tempFixedMapPiece = (ArrayList<String>) fixedMapPiece.clone();
        int mapSize=(int) Math.ceil((length*length)/2.0);//地图块数量
        Collections.shuffle(tempMapPiece);//打乱地图块
        int i=0;
        while (listMap.size()<mapSize && i<have.length){//添加保存的地图块
            listMap.add(have[i]);
            i++;
        }
        String temp;
        Piece temp2;
        while (listMap.size()<mapSize && tempFixedMapPiece.size()>0){//增加基础块(至少拥有的地图块
            temp=tempFixedMapPiece.get(tempFixedMapPiece.size()-1);
            temp2=list.getPiece(temp);
            if(temp2.maxSize!=0) {//存在上限
                if (getMapPieceStackSize(listMap, temp)>temp2.maxSize) {//超过了上限
                    delStringForId(tempFixedMapPiece,temp);
                    continue;
                }
            }
            listMap.add(new PieceStack(temp2));
            tempFixedMapPiece.remove(tempFixedMapPiece.size()-1);
        }
        while (listMap.size()<mapSize){//填充随机地图块
            temp=(String) randomGetValue(tempMapPiece);
            temp2=list.getPiece(temp);
            if(temp2.maxSize!=0) {//存在上限
                if (getMapPieceStackSize(listMap, temp)>temp2.maxSize) {//超过了上限
                    delStringForId(tempMapPiece,temp);
                    continue;
                }
            }
            listMap.add(new PieceStack(temp2));
        }
        Collections.shuffle(listMap);
        return (PieceStack[]) listMap.toArray();

    }

    public PieceStack[] randomAisle(int length){
        PieceStack[] r=new PieceStack[(int) Math.floor((length*length)/2.0)];
        for(int i=0;i<r.length;i++){
            r[i]=new PieceStack(this.list.getPiece((String) randomGetValue(this.aisle)));
        }
        return r;

    }
    
    public static int getMapPieceStackSize(ArrayList<PieceStack> array,String id){
        int size=0;
        for(PieceStack i:array){
            if(i.id==id){size++;}
        }
        return size;
    }

    public static void delMapPieceStackForId(ArrayList<PieceStack> array,String id){
        int size=0;
        for(int i=array.size()-1;i>=0;i--){
            if(array.get(i).id==id){
                array.remove(i);
            }
        }
        return;

    }

    public static void delStringForId(ArrayList<String> array,String id){
        int size=0;
        for(int i=array.size()-1;i>=0;i--){
            if(array.get(i).equals(id)){
                array.remove(i);
            }
        }
        return;

    }

    public static Object randomGetValue(ArrayList array){
        Collections.shuffle(array);
        return array.get(0);
    }




}
