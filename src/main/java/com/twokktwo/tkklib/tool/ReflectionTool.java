package com.twokktwo.tkklib.tool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionTool {
    private static final HashMap<Class, HashMap<String,Method>> METHOD_LIST=new HashMap<>();
    private static final HashMap<Class, HashMap<String,Field>> FIELD_LIST=new HashMap<>();

    public static Method getMethodForList(Class c,String method) throws NoSuchMethodException {
        METHOD_LIST.putIfAbsent(c,new HashMap<>());
        if(!METHOD_LIST.get(c).containsKey(method)){
            Method temp=c.getDeclaredMethod(method);
            temp.setAccessible(true);
            METHOD_LIST.get(c).put(method,temp);
        }
        return METHOD_LIST.get(c).get(method);
    }
    public static Field getFieldForList(Class c,String field) throws NoSuchFieldException {
        FIELD_LIST.putIfAbsent(c,new HashMap<>());
        if(!FIELD_LIST.get(c).containsKey(field)){
            Field temp=c.getDeclaredField(field);
            temp.setAccessible(true);
            FIELD_LIST.get(c).put(field,temp);
        }
        return FIELD_LIST.get(c).get(field);
    }
    public static Object invokeMethod(Object obj,String name,Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getMethodForList(obj.getClass(),name).invoke(obj,args);
    }
    public static Object getField(Object obj,String name) throws NoSuchFieldException, IllegalAccessException {
        return getFieldForList(obj.getClass(),name).get(obj);
    }
    public static void setField(Object obj,String name,Object value) throws NoSuchFieldException, IllegalAccessException {
        getFieldForList(obj.getClass(),name).set(obj,value);
    }


}
