package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;

import java.util.HashMap;
import java.util.Map;

public class CommandLoaderTkkJs {
    public static HashMap<String,ICommand> commandList=new HashMap<>();

    public static boolean hasCommand(String id){return commandList.containsKey(id);}

    public static boolean unregisterCommand(String id){
        if(!hasCommand(id)){return false;}
        if(TkkGameLib.server==null){return false;}
        unregCommand(commandList.get(id));
        commandList.remove(id);
        return true;
    }
    public static boolean registerCommand(String id,ICommand command){
        if(hasCommand(id)){return false;}
        if(TkkGameLib.server==null){return false;}
        regCommand(command);
        commandList.put(id,command);
        return true;
    }
    private static void regCommand(ICommand command){
        if(TkkGameLib.server==null){return;}
        CommandHandler ch = (CommandHandler)TkkGameLib.server.getCommandManager();
        ch.registerCommand(command);
    }
    private static void unregCommand(ICommand command){
        if(TkkGameLib.server==null){return;}
        CommandHandler ch = (CommandHandler)TkkGameLib.server.getCommandManager();
        Map<String,ICommand> commandMap=ch.getCommands();

        commandMap.remove(command.getName());
        for (String s : command.getAliases())
        {
            ICommand icommand = commandMap.get(s);

            if (icommand != null && icommand.getName().equals(s))
            {
                commandMap.remove(s);
            }
        }
        /*
        我不知道为什么，无法获取到commandSet
        try {
            Field[] fs=ch.getClass().getDeclaredFields();
            String debugS="printFS:";
            for(Field f:fs){
                debugS=debugS+","+f.getName();
            }
            TkkGameLib.print(debugS);

            Field chF = ch.getClass().getDeclaredField("commandSet");
            chF.setAccessible(true);
            Set commandSet = (Set) chF.get(ch);
            commandSet.remove(command);
        }catch (Exception e){
            TkkGameLib.print("unregCommand error:"+ miscTool.getError(e));
        }
        */

    }
}
