package com.twokktwo.tkklib.tool;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.UserListOps;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class miscTool {
    public static String getError(Exception e){
        StringWriter temp = new StringWriter();
        e.printStackTrace(new PrintWriter(temp,true));
        return temp.toString();
    }

    public static ScriptEngine getJsInv(String js)throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager(null);
        ScriptEngine engine;
        Invocable inv;

        engine = mgr.getEngineByName("js");
        engine.eval(js);
        return engine;
    }

    public static int getPermissionLevel(EntityPlayerMP player){
        GameProfile gp=player.getServerWorld().getMinecraftServer().getPlayerProfileCache().getGameProfileForUsername(player.getName());
        UserListOps ops=player.getServerWorld().getMinecraftServer().getPlayerList().getOppedPlayers();
        return ops.getPermissionLevel(gp);
    }

}
