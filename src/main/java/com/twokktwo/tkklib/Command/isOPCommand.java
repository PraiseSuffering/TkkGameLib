package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.tool.miscTool;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class isOPCommand extends CommandBase {
    @Override
    public String getName() {
        return "tkkIsOP";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tkkIsOP [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int is;
        if(args.length!=0){
            if(server.getPlayerList().getPlayerByUsername(args[0])!=null){
                is=miscTool.getPermissionLevel(server.getPlayerList().getPlayerByUsername(args[0]));
            }else{
                sender.sendMessage(new TextComponentString("Player "+args[0]+" not found "));
                return;
            }
        }else {
            is=miscTool.getPermissionLevel((EntityPlayerMP) sender);
        }
        sender.sendMessage(new TextComponentString("getPermissionLevel： "+is));
    }
    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }
}
