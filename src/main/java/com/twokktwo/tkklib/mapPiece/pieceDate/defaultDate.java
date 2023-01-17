package com.twokktwo.tkklib.mapPiece.pieceDate;

import com.twokktwo.tkklib.js.JsRunContainer;

import java.io.Serializable;
import java.util.HashMap;

public class defaultDate implements mapPieceDate,Serializable{
    public HashMap<Serializable,Serializable> date = new HashMap<Serializable,Serializable>();
    public HashMap temp=new HashMap();
    public boolean haveJs=false;
    public JsRunContainer js=null;
    public boolean savePiece=false;
    public defaultDate(){}


    @Override
    public boolean needSave() {
        return savePiece;
    }

    @Override
    public boolean haveJs() {
        return haveJs;
    }

    @Override
    public JsRunContainer getJs() {
        return js;
    }

    @Override
    public boolean runJs() {
        try {
            js.runJs();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HashMap<Serializable, Serializable> getMap() {
        return date;
    }

    @Override
    public HashMap getTempMap() {
        return temp;
    }
}
