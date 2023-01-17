package com.twokktwo.tkklib.tool;

import java.util.ArrayList;

public class arrayTool {

    public static int getObjectSize(ArrayList array,Object target){
        int size=0;
        for(Object i:array){
            if(i==target){size++;}
        }
        return size;
    }

    public static int delObject(ArrayList array,Object target){
        int size=0;
        boolean temp=true;
        while (temp){
            temp=array.remove(target);
            if(temp){size++;}
        }
        return size;
    }




}
