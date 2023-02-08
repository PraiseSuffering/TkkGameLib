package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.js.jsStorageTool;
import com.twokktwo.tkklib.mapPiece.template.Template;
import com.twokktwo.tkklib.mapPiece.template.tkkEasyTemplate;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class templateCommand  extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        World world = sender.getEntityWorld();
        if(params.length>0){
            long time=System.nanoTime();
            BlockPos AA = new BlockPos(parseInt(params[0]),parseInt(params[1]),parseInt(params[2]));
            BlockPos size = new BlockPos(parseInt(params[3]),parseInt(params[4]),parseInt(params[5]));
            Template temp=new Template();
            tkkEasyTemplate temp2 = new tkkEasyTemplate();
            temp2.template.takeBlock(world,AA,size);
            temp.takeBlock(world,AA,size);
            jsStorageTool.tempMap.put("template",temp2);
            jsStorageTool.tkkmap.hashMap.put("template",temp2);
            jsStorageTool.tkkmap.save();
            jsStorageTool.tkkmap.read();
            long overtime=System.nanoTime()-time;
            sender.sendMessage(new TextComponentString("get elapsed time:"+overtime+"ns"));
        }else{
            long time = System.nanoTime();
            //Template temp = (Template) TkkGameLib.tempMap.get("template");
            tkkEasyTemplate temp = (tkkEasyTemplate) jsStorageTool.tkkmap.hashMap.get("template");
            temp.template.addBlock(world,sender.getPosition());
            long overtime=System.nanoTime()-time;
            sender.sendMessage(new TextComponentString("fill elapsed time:"+overtime+"ns"));

        }
    }

    @Override
    public String getName() {
        return "tkkTemplate";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.tkkTemplate.usage";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

}
