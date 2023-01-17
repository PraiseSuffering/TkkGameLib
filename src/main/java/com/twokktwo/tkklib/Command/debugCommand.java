package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.gui.testGuiTool;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.mapPiece.Piece;
import com.twokktwo.tkklib.network.networkManager;
import com.twokktwo.tkklib.network.regEventPacket;
import com.twokktwo.tkklib.network.testPacker;
import com.twokktwo.tkklib.template.Template;
import com.twokktwo.tkklib.tool.map.mapTool;
import com.twokktwo.tkklib.tool.testTool;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class debugCommand extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        World world = sender.getEntityWorld();
        if(params!=null && params.length==6){
            TkkGameLib.tkkmap.hashMap.clear();
            BlockPos pos1 = new BlockPos((double) parseInt(params[0]),(double) parseInt(params[1]),(double) parseInt(params[2]));
            BlockPos pos2 = new BlockPos((double) parseInt(params[3]),(double) parseInt(params[4]),(double) parseInt(params[5]));
        }
        if(params.length==1){
            Piece temp;
            switch (params[0]){
                case "aisle":
                    temp=new Piece("testAisle",world,sender.getPosition(),new BlockPos(48,64,48));
                    sender.sendMessage(new TextComponentString("id:"+temp.getId()));
                    mapTool.setMapPiece(temp,true);
                    sender.sendMessage(new TextComponentString("获取通道块!"));
                    break;
                case "mapPiece":
                    temp=new Piece("testMapPiece",world,sender.getPosition(),new BlockPos(48,64,48));
                    sender.sendMessage(new TextComponentString("id:"+temp.getId()));
                    mapTool.setMapPiece(temp,false);
                    sender.sendMessage(new TextComponentString("获取地图块!"));
                    break;
                case "fill":
                    long start=System.nanoTime();
                    mapTool.fill(world,48,64,sender.getPosition());
                    long over=(System.nanoTime()-start)/1000000000;
                    sender.sendMessage(new TextComponentString("time:"+over+"s"));
                    break;
                case "get":
                    long start1=System.nanoTime();
                    temp=new Piece("test",world,sender.getPosition(),new BlockPos(48,64,48));
                    TkkGameLib.tempMap.put("test",temp);
                    long over2=(System.nanoTime()-start1)/1000000000;
                    sender.sendMessage(new TextComponentString("time:"+over2+"s"));
                    break;
                case "mirror":
                    Piece p=(Piece) TkkGameLib.tempMap.get("test");
                    p.block.template.addBlock(world,sender.getPosition(),1);//左下
                    p.block.template.addBlock(world,sender.getPosition().add(48,0,0),0);//左上
                    p.block.template.addBlock(world,sender.getPosition().add(0,0,48),3);//右下
                    p.block.template.addBlock(world,sender.getPosition().add(48,0,48),2);//右上
                    break;
                case "js":
                    JsContainer js=new JsContainer("var a='a';function main(){print('|'+a);a='b'}");
                    js.run("main");
                    TkkGameLib.print("1|"+js.print+"|error:"+js.errored);
                    js.run("main");
                    TkkGameLib.print("2|"+js.print+"|error:"+js.errored);
                    break;
                case "gui":
                    testGuiTool.test((EntityPlayer) sender);
                    break;
                case "gen":
                    testTool testTool=new testTool(444444,null);
                    double[] heightmap=testTool.generateHeightmap(8,0,4,0.1f,0.2f);
                    int h=0;
                    Template template = testTool.setBlocksInChunk(20);
                    template.addBlock(sender.getEntityWorld(),sender.getPosition());
                    break;
                case "packet":
                    networkManager.sendPacketToPlayer(new testPacker("server to client message"),(EntityPlayerMP) sender);
                    networkManager.sendPacketToServer(new testPacker("client to server message"));
                    break;
                case "jsEvent":
                    String jsListener="function main(e){var j=Java.type('com.twokktwo.tkklib.TkkGameLib');j.print('client:'+e)}";
                    jsListener=JsTool.getJsFile("text");
                    String jsEvent="net.minecraftforge.event.entity.living.LivingHurtEvent";
                    String jsId="test";
                    int priority=4;
                    networkManager.sendPacketToPlayer(new regEventPacket(jsId,jsEvent,priority,jsListener),(EntityPlayerMP) sender);
                    sender.sendMessage(new TextComponentString("send packet!"));
                    break;
                case "getJS":
                    sender.sendMessage(new TextComponentString("getJS:"+JsTool.getJsFile("text")));
                    return;
                default:
                    mapTool.see();
                    sender.sendMessage(new TextComponentString("错误的"+params[0]));
            }
        }
        if(params.length==0){
            try {
                Object temp=runJs("function test(){return temp};function temp(){}");
                ScriptObjectMirror jsTemp=(ScriptObjectMirror) temp;
                TkkGameLib.print(jsTemp.getClassName());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.cvMapPiece.usage";
    }

    @Override
    public int getRequiredPermissionLevel(){

        return 2;
    }
    public Object runJs(String js) throws ScriptException,NoSuchMethodException {
        ScriptEngineManager mgr = new ScriptEngineManager(null);
        ScriptEngine engine = mgr.getEngineByName("js");
        engine.eval(js);
        Invocable inv = (Invocable) engine;
        Object r=inv.invokeFunction("test");
        return r;
    }

}
