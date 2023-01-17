package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.network.evalJsPacket;
import com.twokktwo.tkklib.network.networkManager;
import com.twokktwo.tkklib.network.runJsPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class tkkJSRunFileCommand extends CommandBase {
    @Override
    public String getName() {
        return "tkkJSRunFile";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.tkkJSRunFile.usage";
        /*
        * tkkJs <fileName+.js> <functionName> <client/server> [player]
        * */
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0){
            throw new  CommandException("command.tkkJSRunFile.usage", new Object[] {});
        }
        if(args.length<3){
            throw new  CommandException("command.tkkJSRunFile.error.lack", new Object[] {});
        }
        String code;
        if(args[0].length()<=3){throw new  CommandException("command.tkkJSRunFile.error.codeError", new Object[] {});}
        code= JsTool.getJsFile(args[0].substring(0,args[0].length()-3));
        String name="";
        if(args.length>3){name=args[3];}
        EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(name);
        if(args[2].equals("server")){
            if(entityplayermp==null){entityplayermp=(EntityPlayerMP) sender;}
            JsContainer jsContainer=new JsContainer(code);
            jsContainer.run(args[1],new evalJsPacket.evalEvent(entityplayermp,true));
            notifyCommandListener(sender,this,jsContainer.print);
        }else if(args[2].equals("client")){
            if(entityplayermp==null){
                networkManager.sendPacketToAll(new runJsPacket(args[1],args[0]));
            }else{
                networkManager.sendPacketToPlayer(new runJsPacket(args[1],args[0]),entityplayermp);
            }
        }else{
            throw new  CommandException("command.tkkJSRunFile.error.typeError", new Object[] {});
        }
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }
}
