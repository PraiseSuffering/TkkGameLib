package com.twokktwo.tkklib.Command;

import com.twokktwo.tkklib.JSPluginManager;
import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.custonActions.Capability.ActionContainer;
import com.twokktwo.tkklib.gui.testGuiTool;
import com.twokktwo.tkklib.js.JsContainer;
import com.twokktwo.tkklib.js.JsTool;
import com.twokktwo.tkklib.js.jsStorageTool;
import com.twokktwo.tkklib.keyBinding.KeyEventLoader;
import com.twokktwo.tkklib.keyBinding.TkkKeyBinding;
import com.twokktwo.tkklib.mapPiece.Piece;
import com.twokktwo.tkklib.network.networkManager;
import com.twokktwo.tkklib.network.regEventPacket;
import com.twokktwo.tkklib.network.testPacker;
import com.twokktwo.tkklib.template.Template;
import com.twokktwo.tkklib.tool.map.mapTool;
import com.twokktwo.tkklib.tool.testTool;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Set;

public class debugCommand extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        World world = sender.getEntityWorld();
        if(params.length==6){
            jsStorageTool.tkkmap.hashMap.clear();
            BlockPos pos1 = new BlockPos((double) parseInt(params[0]),(double) parseInt(params[1]),(double) parseInt(params[2]));
            BlockPos pos2 = new BlockPos((double) parseInt(params[3]),(double) parseInt(params[4]),(double) parseInt(params[5]));
        }
        if(params.length==3){
            switch (params[0]) {
                case "set":
                    jsStorageTool.getTempMap("test").put(params[1],params[2]);
                    sender.sendMessage(new TextComponentString(params[1]+":"+jsStorageTool.getTempMap("test").get(params[1])));
                    break;
                case "get":
                    sender.sendMessage(new TextComponentString(params[1]+":"+jsStorageTool.getTempMap("test").get(params[1])));
                    break;
                default:
                    sender.sendMessage(new TextComponentString("错误的" + params[0]));
            }
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
                    jsStorageTool.tempMap.put("test",temp);
                    long over2=(System.nanoTime()-start1)/1000000000;
                    sender.sendMessage(new TextComponentString("time:"+over2+"s"));
                    break;
                case "mirror":
                    Piece p=(Piece) jsStorageTool.tempMap.get("test");
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
                    break;
                case "key":
                    TkkKeyBinding key=new TkkKeyBinding("testKey","ALL","NONE",21,"testTab");
                    key.setJS(new JsContainer(JsTool.getJsFile("keyTest")));
                    if(!KeyEventLoader.registerKeyBinding("test",key)){
                        KeyEventLoader.unregisterKeyBinding("test");
                        sender.sendMessage(new TextComponentString("unreg"));
                    }else {
                        sender.sendMessage(new TextComponentString("reg"));
                    }
                    break;
                case "iSee":
                    //TkkGameLib.print("I see:"+ ClientTool.getMouseOver(1));
                    break;
                case "getFunction":
                    JsContainer jsF=JsTool.getJS("test");
                    Object tempFN=jsF.getVar("main");
                    TkkGameLib.print("class:"+tempFN.getClass());
                    TkkGameLib.print("is ScriptObjectMirror?:"+(tempFN instanceof ScriptObjectMirror));
                    if (tempFN instanceof ScriptObjectMirror){
                        Set<Map.Entry<String, Object>> tempSet=((ScriptObjectMirror) tempFN).entrySet();
                        for(Map.Entry<String, Object> entry:tempSet){
                            TkkGameLib.print("key= " + entry.getKey() + " and value= " + entry.getValue());
                        }
                    }
                    break;
                case "getPlugin":
                    long start2=System.nanoTime();
                    JsContainer[] plugs= JSPluginManager.INSTANCE.pluginSort(JSPluginManager.INSTANCE.getPluginList());
                    long over3=System.nanoTime()-start2;
                    int i=0;
                    for(JsContainer z:plugs){
                        TkkGameLib.print(i+"|plugin|"+z.run(JSPluginManager.INSTANCE.JS_FUNCTION_NAME_GET_PLUGIN_ID));
                        i++;
                    }
                    TkkGameLib.print("time:"+over3);
                    break;
                case "cap":
                    if(!(sender instanceof EntityPlayerMP)){return;}
                    ActionContainer cap=sender.getCommandSenderEntity().getCapability(ActionCapbility.actionCap,null);
                    //sender.sendMessage(new TextComponentString("cap:"+cap.getJS()));
                    cap.setJS("test/js");
                    cap.setEnable(true);
                    cap.setTempDataString("test");
                    cap.setTempDataInt(0);
                    //TkkGameLib.print("enable:"+cap.getEnable());
                    //TkkGameLib.print(sender.getCommandSenderEntity()+"cap:"+cap);

                    ActionCapbility.upDataCap((EntityPlayerMP)sender,120,false);
                    //ActionCapbility.upDataCap(sender.getCommandSenderEntity(),(EntityPlayerMP) sender.getCommandSenderEntity());
                    break;
                case "debugAdd":
                    float[] frames=new float[]{3f,3f,3f,3f,3f,3f,3f,3f,3f,3f};
                    float speed=0.1f;
                    float fix=0f;
                    int size=(1+frames.length)*frames.length/2;
                    TkkGameLib.print("size:"+size);
                    fix=size*0.1f/frames.length;
                    for (int i1=0;i1<frames.length;i1++){
                        frames[i1]-=fix;
                    }
                    for (int i1=1;i1<frames.length;i1++){
                        frames[i1]=frames[i1-1]+speed;
                    }
                    String a="[";
                    for (int i1=0;i1<frames.length;i1++){
                        a+=","+frames[i1];
                    }
                    a+="]";
                    TkkGameLib.print("frames:"+a);
                    a="[,"+(3f-fix);
                    for (int i1=2;i1<frames.length;i1++){
                        a+=","+(3f-fix+(i1-1)*speed);
                    }
                    a+="]";
                    TkkGameLib.print("Math:"+a);
                    TkkGameLib.print("Math:"+(1f+(2-1)*0.1f));
                    break;
                case "api":
                    testB(((EntityPlayerMP)sender.getCommandSenderEntity()).getHeldItem(EnumHand.MAIN_HAND));
                    break;
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
    @Optional.Method(modid = "armourers_workshop")
    public void testB(ItemStack itemStack){
        SkinDescriptor skinDescriptor = SkinNBTHelper.getSkinDescriptorFromStack(itemStack);
        TkkGameLib.print("skinDescriptor"+skinDescriptor);
        if(skinDescriptor==null){return;}
        TkkGameLib.print("model:"+ ClientSkinCache.INSTANCE.getSkin(skinDescriptor));
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
