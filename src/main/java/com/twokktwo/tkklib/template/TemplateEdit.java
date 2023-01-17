package com.twokktwo.tkklib.template;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class TemplateEdit {
    private final HashMap<String,Integer> index;
    public final List<com.twokktwo.tkklib.template.BlockDate> list;
    public BlockPos size=BlockPos.ORIGIN;
    public int minX=0,minY=0,minZ=0;
    public int maxX=0,maxY=0,maxZ=0;
    public TemplateEdit(){
        list=Lists.<com.twokktwo.tkklib.template.BlockDate>newArrayList();
        index=new HashMap<>();
    }

    public Template build(){
        Template temp = new Template();
        //=============
        updateAllBoundary();
        modifiedBoundary();
        updateSize();
        //=============
        temp.blocks.clear();
        temp.blocks.addAll(list);
        temp.size=this.size;
        return temp;
    }

    public @Nullable BlockDate getBlockDate(int x,int y,int z){
        String key=x+","+y+","+z;
        if(index.containsKey(key)){
            return list.get(index.get(key));
        }
        return null;
    }

    public @Nullable BlockDate setBlockDate(int x,int y,int z,BlockDate blockdate){
        String key=x+","+y+","+z;
        BlockDate date=null;
        if(index.containsKey(key)){
            //该坐标已经有方块了
            date = list.get(index.get(key));
            list.set(index.get(key),blockdate);
        }else{
            //该坐标从未设置
            index.put(key,list.size());
            list.add(blockdate);
        }
        return date;
    }

    public @Nullable BlockDate delBlockDate(int x,int y,int z){
        String key=x+","+y+","+z;
        BlockDate date=null;
        if(index.containsKey(key)){
            date =  list.get(index.get(key));
            list.remove(index.remove(key));
        }
        return date;
    }


    //test function
    //更新边界，在设置方块时调用
    public void updateBoundary(int x,int y,int z){
        minX=Math.min(minX,x);
        minY=Math.min(minY,y);
        minZ=Math.min(minZ,z);
        maxX=Math.max(maxX,x);
        maxY=Math.max(maxY,y);
        maxZ=Math.max(maxZ,z);
    }
    //更新边界，计算所有方块
    public void updateAllBoundary(){
        minX=0;
        minY=0;
        minZ=0;
        maxX=0;
        maxY=0;
        maxZ=0;
        for(BlockDate b:list){
            updateBoundary(b.pos.getX(),b.pos.getY(),b.pos.getZ());
        }
    }

    //重绘索引
    public void updateIndex(){
        index.clear();
        BlockDate b;
        for(int i=0;i<list.size();i++){
            b=list.get(i);
            index.put(b.pos.getX()+","+b.pos.getY()+","+b.pos.getZ(),i);
        }
    }

    //Move all blocks until the coordinates are in the positive direction
    //以上翻译结果来自有道神经网络翻译（YNMT）· 通用领域
    //最好在运行前使用一次updateAllBoundary，否则可能出问题
    //移动所有方块坐标，以便计算体积
    public void modifiedBoundary(){
        int tempX=maxX,tempY=maxY,tempZ=maxZ;
        int moveX=0,moveY=0,moveZ=0;
        if(minX<0){
            moveX=Math.abs(minX);
            tempX+=moveX;
        }
        if(minY<0){
            moveY=Math.abs(minY);
            tempY+=moveY;
        }
        if(minZ<0){
            moveZ=Math.abs(minZ);
            tempZ+=moveZ;
        }
        BlockDate b;
        for(int i=0;i<list.size();i++){
            b=list.get(i);
            list.set(i,new BlockDate(b.pos.add(moveX,moveY,moveZ),b.blockState,b.tileEntity));
        }
        updateIndex();
        minX=0;
        minY=0;
        minZ=0;
        maxX=tempX;
        maxY=tempY;
        maxZ=tempZ;
    }

    //根据边界设置体积
    public void updateSize(){
        if(minX<0 || minY<0 || minZ<0){
            modifiedBoundary();
        }
        size=new BlockPos(maxX,maxY,maxZ);
    }


}
