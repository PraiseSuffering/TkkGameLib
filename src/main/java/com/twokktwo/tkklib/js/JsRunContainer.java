package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.TkkGameLib;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JsRunContainer {
    public String js;
    public Object value;
    public JsRunContainer(String code, Object value){
        this.js=code;
        this.value=value;
    }
    public void runJs(Object... arg) throws Exception{
        ScriptEngineManager mgr = new ScriptEngineManager(null);
        ScriptEngine engine = mgr.getEngineByName("js");
        engine.eval(this.js);
        Invocable inv = (Invocable) engine;
        inv.invokeFunction("main",arg);
    }
    public void print(String str){
        TkkGameLib.print(str);
    }


}
