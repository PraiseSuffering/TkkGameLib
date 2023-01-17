package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.js.event.BlockJsEvent;
import net.minecraft.util.math.BlockPos;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class BlockJs{
    public BlockPos pos;
    public String js;

    public void runJS(BlockJsEvent event) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager mgr = new ScriptEngineManager(null);
        ScriptEngine engine = mgr.getEngineByName("js");
        engine.eval(js);
        Invocable inv = (Invocable) engine;
        inv.invokeFunction("main",event);
    }





}
