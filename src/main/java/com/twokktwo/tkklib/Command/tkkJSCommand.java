package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.network.evalJsPacket;
import com.twokktwo.tkklib.network.networkManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class tkkJSCommand extends CommandBase {
    @Override
    public String getName() {
        return "tkkJS";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.tkkJS.usage";
        /*
        * tkkJs <fileName+.js / jsCode> <functionName> <client/server> [player]
        * */
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0){
            throw new  CommandException("command.tkkJS.usage", new Object[] {});
        }
        if(args.length<3){
            throw new  CommandException("command.tkkJS.error.lack", new Object[] {});
        }
        String code;
        if(args[0].length()<=3){throw new  CommandException("command.tkkJS.error.codeError", new Object[] {});}
        if(args[0].substring(args[0].length()-3,args[0].length()).equals(".js")){
            code= JsTool.getJsFile(args[0].substring(0,args[0].length()-3));
        }else {
            code=args[0];
        }
        String name="";
        if(args.length>3){name=args[3];}
        EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(name);
        if(args[2].equals("server")){
            if(entityplayermp==null){entityplayermp=(EntityPlayerMP) sender;}
            JsContainer jsContainer=new JsContainer(code);
            jsContainer.run(args[1],new evalJsPacket.evalEvent(entityplayermp,true));
            notifyCommandListener(sender,this,"command.tkkJS.JsServerPrint",jsContainer.print);
        }else if(args[2].equals("client")){
            if(entityplayermp==null){
                networkManager.sendPacketToAll(new evalJsPacket(args[1],code));
            }else{
                networkManager.sendPacketToPlayer(new evalJsPacket(args[1],code),entityplayermp);
            }
        }else{
            throw new  CommandException("command.tkkJS.error.typeError", new Object[] {});
        }
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }
}
