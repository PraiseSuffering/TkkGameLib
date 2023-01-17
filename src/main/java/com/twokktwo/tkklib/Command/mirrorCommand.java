package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class mirrorCommand extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        if(params!=null && params.length==7){
            TkkGameLib.tkkmap.hashMap.clear();
            World world = sender.getEntityWorld();
            BlockPos pos1 = new BlockPos((double) parseInt(params[0]),(double) parseInt(params[1]),(double) parseInt(params[2]));
            BlockPos pos2 = new BlockPos((double) parseInt(params[3]),(double) parseInt(params[4]),(double) parseInt(params[5]));
            Mirror mirror = (params[6]=="LR")?Mirror.LEFT_RIGHT:Mirror.FRONT_BACK;
            TkkGameLib.tkkTool.mirrorMapPiece(world,pos1,pos2,mirror);
        }else{
            sender.sendMessage(new TextComponentString("command.mirror.error"));
        }
    }

    @Override
    public String getName() {
        return "mirror";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.mirror.usage";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

}