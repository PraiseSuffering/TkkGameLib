package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class cvCommand extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        if(params != null && (params.length == 13 || params.length == 12)){
            World world = sender.getEntityWorld();
            int[] AA = {parseInt(params[0]),parseInt(params[1]),parseInt(params[2])};
            int[] AB = {parseInt(params[3]),parseInt(params[4]),parseInt(params[5])};
            int[] BA = {parseInt(params[6]),parseInt(params[7]),parseInt(params[8])};
            int[] BB = {parseInt(params[9]),parseInt(params[10]),parseInt(params[11])};
            boolean doKillEntity;
            if(params.length==12 || params[12] == "true"){
                doKillEntity=true;
            }else{
                doKillEntity=false;
            }
            boolean ok = TkkGameLib.tkkTool.pasteMapPiece(world,AA,AB,world,BA,BB,doKillEntity);
            if(ok){
                sender.sendMessage(new TextComponentString("\u6210\u529f\u4e92\u6362\u5730\u56fe\u5757"));
            }else {
                sender.sendMessage(new TextComponentString("\u4e92\u6362\u5730\u56fe\u5757\u5931\u8d25\uff0c\u53ef\u80fd\u662f\u53c2\u6570\u6709\u95ee\u9898\uff0c\u5171\u670912\u53c2\u6570\uff0caa ab ba bb \u56db\u4e2a\u5750\u6807\u70b9\u3002"));
            }
        }else{
            sender.sendMessage(new TextComponentString("\u53c2\u6570\u6709\u95ee\u9898\uff0c\u5171\u670912\u53c2\u6570\uff0caa ab ba bb \u56db\u4e2a\u5750\u6807\u70b9\u3002"));
        }
    }

    @Override
    public String getName() {
        return "cvMapPiece";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.cvMapPiece.usage";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

}
