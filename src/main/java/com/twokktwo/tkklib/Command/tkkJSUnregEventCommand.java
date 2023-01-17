package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.TkkJsEventManager;
import com.twokktwo.tkklib.network.networkManager;
import com.twokktwo.tkklib.network.unregEventPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class tkkJSUnregEventCommand extends CommandBase {
    @Override
    public String getName() {
        return "tkkJSUnregEvent";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.tkkJSUnregEvent.usage";
        /*
         * tkkJs <id> <server/client> [player]
         * */
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0){
            throw new  CommandException("command.tkkJSUnregEvent.usage");
        }
        if(args.length<2){
            throw new  CommandException("command.tkkJSUnregEvent.error.lack");
        }
        if(args[1].equals("server")){
            boolean temp= TkkJsEventManager.unregListenerList(args[0]);
            notifyCommandListener(sender,this,"command.tkkJSUnregEvent.error.message",temp);
        }else if(args[1].equals("client")){
            String name="";
            if(args.length>2){name=args[2];}
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(name);
            if(entityplayermp==null){
                networkManager.sendPacketToAll(new unregEventPacket(args[0]));
            }else {
                networkManager.sendPacketToPlayer(new unregEventPacket(args[0]), entityplayermp);
            }
        }else {
            throw new CommandException("command.tkkJSUnregEvent.error.type");
        }

    }
    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }
}
