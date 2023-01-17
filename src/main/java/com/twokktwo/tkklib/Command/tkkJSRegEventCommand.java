package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.js.TkkJsEventManager;
import com.twokktwo.tkklib.network.networkManager;
import com.twokktwo.tkklib.network.regEventPacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class tkkJSRegEventCommand extends CommandBase {
    @Override
    public String getName() {
        return "tkkJSRegEvent";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.tkkJSRegEvent.usage";
        /*
         * tkkJs <id> <fileName+.js / jsCode> <class> <client/server> [player]
         * */
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0){
            notifyCommandListener(sender,this,"command.tkkJSRegEvent.usage",new Object[0]);
            //sender.sendMessage(new TextComponentString("command.tkkJSRegEvent.usage"));
            return;
        }
        if(args.length<4){
            throw new  CommandException("command.tkkJSRegEvent.error.lack", new Object[0]);
        }
        String code;
        if(args[1].length()<=3){throw new  CommandException("command.tkkJSRegEvent.error.codeError", new Object[0]);}
        if(args[1].substring(args[1].length()-3,args[1].length()).equals(".js")){
            code= JsTool.getJsFile(args[1].substring(0,args[1].length()-3));
        }else {
            code=args[1];
        }
        Class Sevent;
        Event SEvent;
        try {
            Sevent = Class.forName(args[2]);
            SEvent = (Event) Sevent.newInstance();
        }catch (Exception e){
            throw new  CommandException("command.tkkJSRegEvent.error.classError", new Object[0]);
        }
        if(args[3].equals("server")){
            boolean a=TkkJsEventManager.regListenerList(args[0],SEvent,2,new JsTool.jsListener(new JsContainer(code)));
            //sender.sendMessage(new TextComponentString("command.tkkJSRegEvent.message.return"+a));
            notifyCommandListener(sender,this,"command.tkkJSRegEvent.message.return",a);
        }else if(args[3].equals("client")){
            String name="";
            if(args.length>4){name=args[4];}
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(name);
            if(entityplayermp==null){
                networkManager.sendPacketToAll(new regEventPacket(args[0],args[2],2,code));
            }else {
                networkManager.sendPacketToPlayer(new regEventPacket(args[0], args[2], 2, code), entityplayermp);
            }
        }else {
            throw new  CommandException("command.tkkJSRegEvent.error.type", new Object[] {});
        }

    }
    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }
}
