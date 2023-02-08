package com.twokktwo.tkklib.keyBinding;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.js.JsContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class TkkKeyBinding {
    public KeyBinding keyBinding;
    public JsContainer js;
    public boolean lastKeyDown=false;
    public TkkKeyBinding(String description, net.minecraftforge.client.settings.IKeyConflictContext keyConflictContext, net.minecraftforge.client.settings.KeyModifier keyModifier, int keyCode, String category){
        keyBinding=new KeyBinding(description,keyConflictContext,keyModifier,keyCode,category);
    }

    public TkkKeyBinding(String name,String usableRange,String AttachedKey,int keyCode,String tab){
        IKeyConflictContext tempA;
        KeyModifier tempB;
        switch (usableRange){
            case "GUI":
                tempA=KeyConflictContext.GUI;
                break;
            case "INGAME":
                tempA=KeyConflictContext.IN_GAME;
                break;
            case "ALL":
                tempA=KeyConflictContext.UNIVERSAL;
                break;
            default:
                TkkGameLib.print("TkkKeyBinding usableRange error type:"+usableRange);
                tempA=KeyConflictContext.UNIVERSAL;
        }
        switch (AttachedKey){
            case "CTRL":
                tempB=KeyModifier.CONTROL;
                break;
            case "ALT":
                tempB=KeyModifier.ALT;
                break;
            case "SHIFT":
                tempB=KeyModifier.SHIFT;
                break;
            case "NONE":
                tempB=KeyModifier.NONE;
                break;
            default:
                TkkGameLib.print("TkkKeyBinding AttachedKey error type:"+AttachedKey);
                tempB=KeyModifier.NONE;
        }
        keyBinding=new KeyBinding(name,tempA,tempB,keyCode,tab);
    }

    public void setJS(JsContainer js){
        this.js=js;
    }

    public boolean isPressed(){return keyBinding.isPressed();}

    public boolean isKeyDown(){
        return keyBinding.isKeyDown();
    }

    public int getKeyCode()
    {
        return keyBinding.getKeyCode();
    }

    public void setKeyCode(int keyCode)
    {
        keyBinding.setKeyCode(keyCode);
    }

    public void jsPressStart(){
        if(js!=null){
            js.run("Start");
            if(js.errored){
                TkkGameLib.print("keyError:"+ js.print);
            }
        }
    }

    public void jsPressTick(){
        if(js!=null){
            js.run("Tick");
            if(js.errored){
                TkkGameLib.print("keyError:"+ js.print);
            }
        }
    }

    public void jsPressOver(){
        if(js!=null){
            js.run("Over");
            if(js.errored){
                TkkGameLib.print("keyError:"+ js.print);
            }
        }
    }

}
