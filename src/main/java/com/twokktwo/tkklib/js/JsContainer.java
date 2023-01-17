package com.twokktwo.tkklib.js;

import com.twokktwo.tkklib.TkkGameLib;

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
    public void run(String fn,Object... args) {
        if(errored){return;}
        try {
            ((Invocable) engine).invokeFunction(fn, args);
        }catch (NoSuchMethodException e){
            TkkGameLib.print("NoSuchMethodException:"+e);
        }catch (ScriptException e){
            this.errored=true;
            appandConsole(e.getMessage());
            TkkGameLib.print("ScriptException:"+e);
        }catch (Exception e){
            this.errored=true;
            appandConsole(e.getMessage());
            TkkGameLib.print("Exception:"+e);
        }
        appandConsole(sw.getBuffer().toString().trim());
        sw.getBuffer().delete(0,sw.getBuffer().length());
    }


}