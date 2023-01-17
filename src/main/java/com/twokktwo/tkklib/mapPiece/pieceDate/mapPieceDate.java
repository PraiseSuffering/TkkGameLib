package com.twokktwo.tkklib.mapPiece.pieceDate;

import com.twokktwo.tkklib.js.JsRunContainer;

import java.io.Serializable;
import java.util.HashMap;

public interface mapPieceDate extends Serializable{
    /*
    * 地图块数据不包含方块以及实体
    * 需要包含；
    * 地图块的数据（供脚本使用）
    * 地图块的脚本
    * 地图块是否保存
    *
    * */
    boolean needSave();
    boolean haveJs();
    JsRunContainer getJs();
    boolean runJs();
    HashMap<Serializable,Serializable> getMap();
    HashMap getTempMap();
}
