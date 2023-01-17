package com.twokktwo.tkklib.tool.map;


import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.tkkFastMap;
import com.twokktwo.tkklib.tool.tkkSerializationMap;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class mapPieceTool {
    public int mapLength=8;
    public int interval=1;
    public tkkSerializationMap date;
    public tkkFastMap map;
    /*
    * date:{
    *
    *   npc块:{name:NBTTemplate}
    *   遗迹块:{name:{date:NBTTemplate,mapPos:BlockPos}}
    *
    *
    *   地图块数据{
    *     x|z:{破碎值:int,状态:string,类型:string}
    *   }
    *
    *   世界数据{
    *     区域id:{原点:[x,y,z],边长:int,name:String,类型:string,额外数据:{}}
    *   }
    * }
    * */

    public mapPieceTool(){
        this.date=new tkkSerializationMap("MapPieceDate");
        this.map=new tkkFastMap();
        date.getHashMap().putIfAbsent("npcTemplate",new tkkSerializationMap());
        date.getHashMap().putIfAbsent("relicTemplate",new tkkSerializationMap());
        date.getHashMap().putIfAbsent("date",new tkkSerializationMap());
        date.getHashMap().putIfAbsent("world",new tkkSerializationMap());

    }

    public int[] getXZ(int x,int z){
        return new int[] {(x*mapLength+x*interval)*16,(z*mapLength+z*interval)*16};
    }

    public BlockPos getPos(String world, int x, int z){
        tkkSerializationMap Date = (tkkSerializationMap)((tkkSerializationMap)this.date.getHashMap().get("world")).getHashMap().get(world);
        if(Date==null){return null;}
        int[] worldPos=(int[]) Date.getHashMap().get("pos");
        return new BlockPos(worldPos[0]+(x*mapLength+x*interval)*16,0,worldPos[2]+(z*mapLength+z*interval)*16);
    }

    public boolean addMapTemplate(World world, BlockPos A,BlockPos B,boolean isNpcMap,String name){
        //判断大小是否匹配
        if(Math.sqrt(Math.pow(A.getY() - B.getY(), 2)) != 256){return false;}
        if(Math.sqrt(Math.pow(A.getX() - B.getX(), 2)) != mapLength*16){return false;}
        if(Math.sqrt(Math.pow(A.getZ() - B.getZ(), 2)) != mapLength*16){return false;}
        //NBTTagCompound temp=mapTool.getMapPiece(world,A,B);
        tkkSerializationMap map;
        if(isNpcMap){
            //存储到npc模板
            map = (tkkSerializationMap) date.getHashMap().get("npcTemplate");
        }else{
            //存储到遗迹模板
            map = (tkkSerializationMap) date.getHashMap().get("relicTemplate");
        }
        //map.set(name,temp.toString());
        return true;
    }

    public NBTTagCompound getMapTemplate(boolean isNpcMap, String name){
        tkkSerializationMap map;
        if(isNpcMap){
            //npc模板
            map = (tkkSerializationMap) date.getHashMap().get("npcTemplate");
        }else{
            //遗迹模板
            map = (tkkSerializationMap) date.getHashMap().get("relicTemplate");
        }
        if(!map.getHashMap().containsKey(name)){return null;}
        try {
            return JsonToNBT.getTagFromJson((String) map.getHashMap().get(name));
        }catch (Exception e){
            TkkGameLib.logger.log(Level.ERROR,"mapPieceTool.getMapTemplate("+isNpcMap+","+name+")error:"+e);
            return null;
        }

    }

    public String randomNpcMapName(){
        tkkSerializationMap map=(tkkSerializationMap)date.getHashMap().get("npcTemplate");
        int Size=map.getHashMap().size();
        int index=TkkGameLib.tkkTool.randomNumber(0,Size-1);
        int count=0;
        for(Object key : map.getHashMap().keySet()){
            if(count==index){return (String)key;}
            count++;
        }
        return null;
    }

    public String randomRelicMapName(){
        tkkSerializationMap map=(tkkSerializationMap)date.getHashMap().get("relicTemplate");
        int Size=map.getHashMap().size();
        int index=TkkGameLib.tkkTool.randomNumber(0,Size-1);
        int count=0;
        for(Object key : map.getHashMap().keySet()){
            if(count==index){return (String)key;}
            count++;
        }
        return null;
    }

    public NBTTagCompound randomMapPiece(){
        int npcProbability=2;
        int relicProbability=8;
        int random=TkkGameLib.tkkTool.randomNumber(0,npcProbability+relicProbability);
        if(random<=npcProbability){return getMapTemplate(true,randomNpcMapName());}
        if(random<=npcProbability+relicProbability){return getMapTemplate(true,randomNpcMapName());}
        return null;
    }

    public boolean setWorld(BlockPos pos,int length,String name,boolean isNpcMap){
        tkkSerializationMap map=(tkkSerializationMap)date.getHashMap().get("world");
        tkkSerializationMap date=new tkkSerializationMap();
        //此处应有判断重叠,I'm lazyDog
        date.getHashMap().put("pos",new int[] {pos.getX(),pos.getY(),pos.getZ()});
        date.getHashMap().put("length",length);
        date.getHashMap().put("isNpcMap",isNpcMap);
        map.getHashMap().put(name,date);
        return true;
    }

    /*public @Nullable String getWorldForPos(BlockPos pos){
        tkkSerializationMap Date=(tkkSerializationMap) this.date.get("world");
        for(Object key : Date.keySet()){
            tkkSerializationMap worldDate = (tkkSerializationMap) Date.get(key);
            int[] worldPos = (int[]) worldDate.get("pos");
            int worldLength=(int) worldDate.get("length");

        }
    }*/

    public void refreshMapPieceDate(World MCworld,int x,int z,String world){
        BlockPos posA = getPos(world,x,z);
        BlockPos posB = new BlockPos(posA.getX()+mapLength*16,256,posA.getZ()+mapLength*16);




    }

}
