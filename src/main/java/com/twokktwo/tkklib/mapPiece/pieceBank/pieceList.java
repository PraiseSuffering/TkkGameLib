package com.twokktwo.tkklib.mapPiece.pieceBank;

import com.twokktwo.tkklib.mapPiece.Piece;

import java.io.Serializable;
import java.util.HashMap;

public class pieceList implements Serializable {
    private HashMap<String,Piece> mapList;
    public Piece getPiece(String name){return mapList.get(name);}
    public boolean addPiece(String name,Piece piece){
        if(mapList.containsKey(name)){return false;}
        mapList.put(name,piece);
        return true;
    }
    public void removePiece(String name){
        mapList.remove(name);
    }
    public void putPiece(String name,Piece piece) {
        mapList.put(name, piece);
    }

}
