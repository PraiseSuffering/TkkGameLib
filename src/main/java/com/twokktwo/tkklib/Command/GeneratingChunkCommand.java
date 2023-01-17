package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.template.Template;
import com.twokktwo.tkklib.tool.testTool;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class GeneratingChunkCommand extends CommandBase {
    @Override
    public String getName() {
        return "genChunk";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.genChunk.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==7){
            testTool testTool=new testTool(parseLong(args[0]),null);
            //8 0 4 0.1f 0.2f
            testTool.generateHeightmap(parseInt(args[1]),parseInt(args[2]),parseInt(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]));
            //20
            Template template = testTool.setBlocksInChunk(parseInt(args[6]));
            template.addBlock(sender.getEntityWorld(),sender.getPosition());
        }
        if(args.length==8){
            int radius=parseInt(args[7]);
            for (int x=-radius;x<=radius;x++){
                for(int z=-radius;z<=radius;z++){
                    spawnChunk(parseLong(args[0]),parseInt(args[1])+x*4,parseInt(args[2]),parseInt(args[3])+z*4,Float.parseFloat(args[4]),Float.parseFloat(args[5]),parseInt(args[6]),sender.getEntityWorld(),sender.getPosition().add(x*16,0,z*16));
                }
            }
        }
        sender.sendMessage(new TextComponentString("args : long seed, int x, int y, int z,float BaseHeight,float HeightVariation,int seaLevel"));
    }

    private void spawnChunk(long seed, int x, int y, int z, float BaseHeight, float HeightVariation, int seaLevel, World world, BlockPos pos){
        testTool testTool=new testTool(seed,null);
        //8 0 4 0.1f 0.2f
        testTool.generateHeightmap(x,y,z,BaseHeight,HeightVariation);
        //20
        Template template = testTool.setBlocksInChunk(seaLevel);
        template.addBlock(world,pos);

    }
}
