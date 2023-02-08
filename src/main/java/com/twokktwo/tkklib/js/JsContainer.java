package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.miscTool;

import javax.script.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class JsContainer{
    public String js="";
    public String print;
    public StringWriter sw = new StringWriter();
    public PrintWriter pw = new PrintWriter(sw);
    public ScriptEngineManager mgr = new ScriptEngineManager(null);
    public ScriptEngine engine = mgr.getEngineByName("ECMAScript");
    public boolean errored=false;

    public JsContainer(String code){
            initJs(code);
    }
    public void initJs(String js){
        this.errored=false;
        print="";
        mgr = new ScriptEngineManager(null);
        engine = mgr.getEngineByName("ECMAScript");

        engine.getContext().setWriter(pw);
        engine.getContext().setErrorWriter(pw);
        try {
            engine.eval(js);
            this.js=js;
        }catch (Exception e){
            this.errored=true;
            appandConsole(e.getMessage());
        }
        appandConsole(sw.getBuffer().toString().trim());
        sw.getBuffer().delete(0,sw.getBuffer().length());
    }
    public void appandConsole(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if(this.print==null || this.print.equals("")){
            this.print=message;
            return;
        }
        this.print = message + "\n" + this.print;
    }
    public void setVar(String name,Object value){
        engine.put(name,value);
    };
    public Object getVar(String name){
        return engine.get(name);
    }
    public Object run(String fn,Object... args) {
        if(errored){return null;}
        Object obj=null;
        try {
            obj=((Invocable) engine).invokeFunction(fn, args);
        }catch (NoSuchMethodException e){
            //TkkGameLib.print("NoSuchMethodException:"+ miscTool.getError(e));
        }catch (ScriptException e){
            this.errored=true;
            appandConsole(e.getMessage());
            TkkGameLib.print("ScriptException:"+miscTool.getError(e));
        }catch (Exception e){
            this.errored=true;
            appandConsole(e.getMessage());
            TkkGameLib.print("Exception:"+miscTool.getError(e));
        }
        appandConsole(sw.getBuffer().toString().trim());
        sw.getBuffer().delete(0,sw.getBuffer().length());
        return obj;
    }


}