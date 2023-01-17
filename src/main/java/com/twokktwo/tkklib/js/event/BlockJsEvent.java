package com.twokktwo.tkklib.js.event;

import com.twokktwo.tkklib.tool.xyz;

public class BlockJsEvent {
    public final xyz pos;//脚本对应的坐标
    public final xyz pieceStart;//脚本对应地图块的起点
    public final xyz pieceOver;//脚本对应地图块的终点
    public BlockJsEvent(xyz pos,xyz start,xyz over){
        this.pos=pos;
        this.pieceStart=start;
        this.pieceOver=over;
    }
}
