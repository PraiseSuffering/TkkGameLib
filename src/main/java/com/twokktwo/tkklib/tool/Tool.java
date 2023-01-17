package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.*;

public class Tool {
    //swapLocations方法来自https://github.com/WayofTime/BloodMagic/blob/1.12/src/main/java/WayofTime/bloodmagic/util/Utils.java
    //swapLocations方法作者WayofTime
    //swapLocations方法经过了修改
    //pasteMapPiece也是
    public Tool() {
    }
    public void mirrorMapPiece(World world, BlockPos A, BlockPos B, Mirror mirror){
        int[] y = {Math.max(A.getY(),B.getY()),Math.min(A.getY(),B.getY())};
        int[] x = {Math.max(A.getX(),B.getX()),Math.min(A.getX(),B.getX())};
        int[] z = {Math.max(A.getZ(),B.getZ()),Math.min(A.getZ(),B.getZ())};
        //计算立方体的xyz长
        int XLength = (int) Math.sqrt(Math.pow((double) x[0] - x[1], 2));
        int YLength = (int) Math.sqrt(Math.pow((double) y[0] - y[1], 2));
        int ZLength = (int) Math.sqrt(Math.pow((double) z[0] - z[1], 2));
        //缓存方块
        HashMap blockTemp = new HashMap();
        int count=0;
        BlockPos tempPos;
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    tempPos=new BlockPos(x[1]+tempA,y[1]+tempC,z[1]+tempB);
                    blockTemp.put(tempPos.getX()+"|"+tempPos.getY()+"|"+tempPos.getZ(),world.getBlockState(tempPos));
                    TileEntity tileEntity=world.getTileEntity(tempPos);
                    if(tileEntity!=null){
                        blockTemp.put("tile|"+tempPos.getX()+"|"+tempPos.getY()+"|"+tempPos.getZ(),tileEntity.writeToNBT(new NBTTagCompound()));
                        world.removeTileEntity(tempPos);
                        TkkGameLib.logger.log(Level.DEBUG,tileEntity.writeToNBT(new NBTTagCompound()));
                    }
                }
            }
        }
        //设置运算
        float[] rotationYawSkew={0,0};
        int startX=0,startZ=0;
        int countX=-1,countZ=-1;
        int startY=y[0],countY=-1;
        int forX=0,forZ=0,forY=y[1];
        int mirrorX=0,mirrorZ=0,mirrorY=y[1];
        switch (mirror){
            case FRONT_BACK:
                startX=x[1];
                startZ=z[0];
                mirrorX=x[0];
                mirrorZ=z[1];
                countX=1;
                countZ=-1;
                forX=x[0];
                forZ=z[1];
                rotationYawSkew[0]=0;
                rotationYawSkew[1]=-180;
                break;
            case LEFT_RIGHT:
                startX=x[0];
                startZ=z[1];
                mirrorX=x[1];
                mirrorZ=z[0];
                countX=-1;
                countZ=1;
                forX=x[1];
                forZ=z[0];
                rotationYawSkew[0]=-90;
                rotationYawSkew[1]=-270;
                break;
            case NONE:
                return;
        }
        //方块
        boolean runX=true,runY=true,runZ=true;
        int nowX=startX,nowY=startY,nowZ=startZ;
        int tempA=XLength,tempB=ZLength,tempC=YLength;
        IBlockState oldBlock;
        BlockPos old,now;
        while (runX){
            tempB=ZLength;
            nowZ=startZ;
            runZ=true;
            while (runZ){
                tempC=YLength;
                nowY=startY;
                runY=true;
                while (runY){
                    old=new BlockPos(x[1]+tempA,y[1]+tempC,z[1]+tempB);
                    now=new BlockPos(nowX,nowY,nowZ);
                    IBlockState state = (IBlockState) blockTemp.get(old.getX()+"|"+old.getY()+"|"+old.getZ());
                    //TkkGameLib.logger.log(Level.DEBUG,old.getX()+"|"+old.getY()+"|"+old.getZ());
                    tkkSetBlockState(world,now,state.withMirror(mirror),3);
                    if(blockTemp.containsKey("tile|"+old.getX()+"|"+old.getY()+"|"+old.getZ())){
                        TileEntity temptile = TileEntity.create(world,(NBTTagCompound) blockTemp.get("tile|"+old.getX()+"|"+old.getY()+"|"+old.getZ()));
                        world.setTileEntity(now,temptile);
                        temptile.mirror(mirror);
                        temptile.setPos(now);
                    }
                    if(nowY==forY){runY=false;}
                    tempC--;
                    nowY+=countY;
                }
                if(nowZ==forZ){runZ=false;}
                tempB--;
                nowZ+=countZ;
            }
            if(nowX==forX){runX=false;}
            tempA--;
            nowX+=countX;
        }
        TkkGameLib.logger.log(Level.DEBUG,blockTemp);
        //更新
        runX=true;
        runY=true;
        runZ=true;
        nowX=startX;
        nowY=startY;
        nowZ=startZ;
        tempA=XLength;
        tempB=ZLength;
        tempC=YLength;
        while (runX){
            tempB=ZLength;
            nowZ=startZ;
            runZ=true;
            while (runZ){
                tempC=YLength;
                nowY=startY;
                runY=true;
                while (runY){
                    old=new BlockPos(x[1]+tempA,y[1]+tempC,z[1]+tempB);
                    now=new BlockPos(nowX,nowY,nowZ);
                    this.updateBlock(world,now,(IBlockState) blockTemp.get(old.getX()+"|"+old.getY()+"|"+old.getZ()));
                    if(nowY==forY){runY=false;}
                    tempC--;
                    nowY+=countY;
                }
                if(nowZ==forZ){runZ=false;}
                tempB--;
                nowZ+=countZ;
            }
            if(nowX==forX){runX=false;}
            tempA--;
            nowX+=countX;
        }
        //实体
        List<Entity> entityList;
        AxisAlignedBB originalArea = new AxisAlignedBB(new BlockPos(x[0]+1,y[0]+1,z[0]+1),new BlockPos(x[1],y[1],z[1]));
        entityList = world.getEntitiesWithinAABB(Entity.class, originalArea);

        if (!entityList.isEmpty()) {
            for (Entity nowEntity : entityList) {
                double[] entityPos = {nowEntity.posX,nowEntity.posY,nowEntity.posZ};
                double[] skew = {Math.sqrt(Math.pow(entityPos[0] - x[1], 2)),Math.sqrt(Math.pow(entityPos[1] - y[1], 2)),Math.sqrt(Math.pow(entityPos[2] - z[1], 2))};
                if(countX==1){skew[0]-=1;}
                if(countY==1){skew[1]-=1;}
                if(countZ==1){skew[2]-=1;}
                //int[] skew = {(int) entityPos[0]-x[1],(int)entityPos[1]-y[1],(int)entityPos[2]-z[1]};
                float yaw=nowEntity.rotationYaw;
                float newYaw=rotationYawSkew[1]-yaw-rotationYawSkew[0];
                moveEntity(world,nowEntity,mirrorX+skew[0]*countX*-1,mirrorY+skew[1]*countY*-1,mirrorZ+skew[2]*countZ*-1,newYaw,nowEntity.rotationPitch);
                nowEntity.setVelocity(nowEntity.motionX*countX*-1,nowEntity.motionY*countY*-1,nowEntity.motionZ*countZ*-1);
                //TkkGameLib.logger.log(Level.DEBUG,"skew:["+skew[0]+","+skew[1]+","+skew[2]+"] mirror:["+mirrorX+","+mirrorY+","+mirrorZ+"] entity:["+entityPos[0]+","+entityPos[1]+","+entityPos[2]+"] countY:"+countY+" countX:"+countX+" countZ:"+countZ);
                //nowEntity.setRenderYawOffset();

            }
        }

    }

    public boolean pasteMapPiece(World initialWorld, BlockPos AApos, BlockPos ABpos, World finalWorld, BlockPos BApos, BlockPos BBpos, boolean doKillEntity) {
        int[] AA={AApos.getX(),AApos.getY(),AApos.getZ()};
        int[] AB={ABpos.getX(),ABpos.getY(),ABpos.getZ()};
        int[] BA={BApos.getX(),BApos.getY(),BApos.getZ()};
        int[] BB={BBpos.getX(),BBpos.getY(),BBpos.getZ()};
        return pasteMapPiece(initialWorld,AA,AB,finalWorld,BA,BB,doKillEntity);
    }

    public boolean pasteMapPiece(World initialWorld, int[] AA, int[] AB, World finalWorld, int[] BA, int[] BB, boolean doKillEntity) {
        //判断A范围与B范围大小是否一致
        int AXSize = AA[0] - AB[0];
        int BXSize = BA[0] - BB[0];
        int AYSize = AA[1] - AB[1];
        int BYSize = BA[1] - BB[1];
        int AZSize = AA[2] - AB[2];
        int BZSize = BA[2] - BB[2];
        if (Math.abs(AXSize) != Math.abs(BXSize) || Math.abs(AYSize) != Math.abs(BYSize) || Math.abs(AZSize) != Math.abs(BZSize)) {
            return false;
        }
        //寻找立方体准确顶点A为XYZ最小 B为XYZ最大
        int[] realAA = {(AA[0] > AB[0]) ? AB[0] : AA[0], (AA[1] > AB[1]) ? AB[1] : AA[1], (AA[2] > AB[2]) ? AB[2] : AA[2]};
        int[] realAB = {(AA[0] < AB[0]) ? AB[0] : AA[0], (AA[1] < AB[1]) ? AB[1] : AA[1], (AA[2] < AB[2]) ? AB[2] : AA[2]};
        int[] realBA = {(BA[0] > BB[0]) ? BB[0] : BA[0], (BA[1] > BB[1]) ? BB[1] : BA[1], (BA[2] > BB[2]) ? BB[2] : BA[2]};
        int[] realBB = {(BA[0] < BB[0]) ? BB[0] : BA[0], (BA[1] < BB[1]) ? BB[1] : BA[1], (BA[2] < BB[2]) ? BB[2] : BA[2]};

        //计算立方体的xyz长
        int XLength = (int) Math.sqrt(Math.pow((double) realAA[0] - realAB[0], 2));
        int YLength = (int) Math.sqrt(Math.pow((double) realAA[1] - realAB[1], 2));
        int ZLength = (int) Math.sqrt(Math.pow((double) realAA[2] - realAB[2], 2));
        ArrayList oldBlockState=new ArrayList();

        long timeRun=System.nanoTime();
        //开始转换
        int count=0;
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    //this.pasteLocations(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                    oldBlockState.add(this.pasteLocations(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB)));
                    count++;
                }
            }
        }
        long timeOver=System.nanoTime()-timeRun;
        TkkGameLib.logger.log(Level.DEBUG,"1:"+timeOver);
        //更新方块状态
        timeRun=System.nanoTime();
        //count=0;
        //for (int tempA = XLength; tempA >= 0; tempA--) {//X
        //    for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
       //         for (int tempC = YLength; tempC >= 0; tempC--) {//Y
        //            this.updateBlock(finalWorld,new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB),(IBlockState)oldBlockState.get(count));
        //            //this.updateBlock(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
        //        }
        //    }
        //}
        timeOver=System.nanoTime()-timeRun;
        TkkGameLib.logger.log(Level.DEBUG,"2:"+timeOver);
        //修正光照
        /*
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    //this.updateLight(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        */
        //实体传送
        timeRun=System.nanoTime();
        List<Entity> originalWorldEntities;
        List<Entity> focusWorldEntities;
        AxisAlignedBB originalArea = new AxisAlignedBB(realAA[0], realAA[1], realAA[2], realAB[0], realAB[1], realAB[2]);
        originalWorldEntities = initialWorld.getEntitiesWithinAABB(Entity.class, originalArea);
        AxisAlignedBB focusArea = new AxisAlignedBB(realBA[0], realBA[1], realBA[2], realBB[0], realBB[1], realBB[2]);
        focusWorldEntities = finalWorld.getEntitiesWithinAABB(Entity.class, focusArea);

        //删除实体？
        if (!focusWorldEntities.isEmpty() && doKillEntity) {
            for (Entity entity : focusWorldEntities) {
                if(entity instanceof  EntityPlayer){continue;}
                entity.setDead();
                //moveEntity(finalWorld, entity, entity.posX - realBA[0] + realAA[0], entity.posY - realBA[1] + realAA[1], entity.posZ - realBA[2] + realAA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - focusPos.getX() + pos.getX(), entity.posY - focusPos.getY() + pos.getY(), entity.posZ - focusPos.getZ() + pos.getZ()), entity, bindingOwnerID, true));
            }
        }
        if (!originalWorldEntities.isEmpty()) {
            for (Entity entity : originalWorldEntities) {
                pasteEntity(finalWorld, entity, entity.posX - realAA[0] + realBA[0], entity.posY - realAA[1] + realBA[1], entity.posZ - realAA[2] + realBA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, bindingOwnerID, true));
            }
        }
        timeOver=System.nanoTime()-timeRun;
        TkkGameLib.logger.log(Level.DEBUG,"3:"+timeOver);


        return true;
    }


    public boolean moveMapPieceOnlyEntity(World initialWorld, BlockPos AApos, BlockPos ABpos, World finalWorld, BlockPos BApos, BlockPos BBpos){
        int[] AA={AApos.getX(),AApos.getY(),AApos.getZ()};
        int[] AB={ABpos.getX(),ABpos.getY(),ABpos.getZ()};
        int[] BA={BApos.getX(),BApos.getY(),BApos.getZ()};
        int[] BB={BBpos.getX(),BBpos.getY(),BBpos.getZ()};
        //判断A范围与B范围大小是否一致
        int AXSize = AA[0] - AB[0];
        int BXSize = BA[0] - BB[0];
        int AYSize = AA[1] - AB[1];
        int BYSize = BA[1] - BB[1];
        int AZSize = AA[2] - AB[2];
        int BZSize = BA[2] - BB[2];
        if (Math.abs(AXSize) != Math.abs(BXSize) || Math.abs(AYSize) != Math.abs(BYSize) || Math.abs(AZSize) != Math.abs(BZSize)) {
            return false;
        }
        //寻找立方体准确顶点A为XYZ最小 B为XYZ最大
        int[] realAA = {(AA[0] > AB[0]) ? AB[0] : AA[0], (AA[1] > AB[1]) ? AB[1] : AA[1], (AA[2] > AB[2]) ? AB[2] : AA[2]};
        int[] realAB = {(AA[0] < AB[0]) ? AB[0] : AA[0], (AA[1] < AB[1]) ? AB[1] : AA[1], (AA[2] < AB[2]) ? AB[2] : AA[2]};
        int[] realBA = {(BA[0] > BB[0]) ? BB[0] : BA[0], (BA[1] > BB[1]) ? BB[1] : BA[1], (BA[2] > BB[2]) ? BB[2] : BA[2]};
        int[] realBB = {(BA[0] < BB[0]) ? BB[0] : BA[0], (BA[1] < BB[1]) ? BB[1] : BA[1], (BA[2] < BB[2]) ? BB[2] : BA[2]};
        //实体传送
        List<Entity> originalWorldEntities;
        List<Entity> focusWorldEntities;
        AxisAlignedBB originalArea = new AxisAlignedBB(realAA[0], realAA[1], realAA[2], realAB[0]+1, realAB[1]+1, realAB[2]+1);
        originalWorldEntities = initialWorld.getEntitiesWithinAABB(Entity.class, originalArea);
        AxisAlignedBB focusArea = new AxisAlignedBB(realBA[0], realBA[1], realBA[2], realBB[0], realBB[1], realBB[2]);
        focusWorldEntities = finalWorld.getEntitiesWithinAABB(Entity.class, focusArea);

        if (!originalWorldEntities.isEmpty()) {
            for (Entity entity : originalWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realAA[0] + realBA[0], entity.posY - realAA[1] + realBA[1], entity.posZ - realAA[2] + realBA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, bindingOwnerID, true));
            }
        }
        if (!focusWorldEntities.isEmpty()) {
            for (Entity entity : focusWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realBA[0] + realAA[0], entity.posY - realBA[1] + realAA[1], entity.posZ - realBA[2] + realAA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - focusPos.getX() + pos.getX(), entity.posY - focusPos.getY() + pos.getY(), entity.posZ - focusPos.getZ() + pos.getZ()), entity, bindingOwnerID, true));
            }
        }
        return true;

    }

    public boolean moveMapPiece(World initialWorld, BlockPos AApos, BlockPos ABpos, World finalWorld, BlockPos BApos, BlockPos BBpos) {
        int[] AA={AApos.getX(),AApos.getY(),AApos.getZ()};
        int[] AB={ABpos.getX(),ABpos.getY(),ABpos.getZ()};
        int[] BA={BApos.getX(),BApos.getY(),BApos.getZ()};
        int[] BB={BBpos.getX(),BBpos.getY(),BBpos.getZ()};
        //判断A范围与B范围大小是否一致
        int AXSize = AA[0] - AB[0];
        int BXSize = BA[0] - BB[0];
        int AYSize = AA[1] - AB[1];
        int BYSize = BA[1] - BB[1];
        int AZSize = AA[2] - AB[2];
        int BZSize = BA[2] - BB[2];
        if (Math.abs(AXSize) != Math.abs(BXSize) || Math.abs(AYSize) != Math.abs(BYSize) || Math.abs(AZSize) != Math.abs(BZSize)) {
            return false;
        }
        //寻找立方体准确顶点A为XYZ最小 B为XYZ最大
        int[] realAA = {(AA[0] > AB[0]) ? AB[0] : AA[0], (AA[1] > AB[1]) ? AB[1] : AA[1], (AA[2] > AB[2]) ? AB[2] : AA[2]};
        int[] realAB = {(AA[0] < AB[0]) ? AB[0] : AA[0], (AA[1] < AB[1]) ? AB[1] : AA[1], (AA[2] < AB[2]) ? AB[2] : AA[2]};
        int[] realBA = {(BA[0] > BB[0]) ? BB[0] : BA[0], (BA[1] > BB[1]) ? BB[1] : BA[1], (BA[2] > BB[2]) ? BB[2] : BA[2]};
        int[] realBB = {(BA[0] < BB[0]) ? BB[0] : BA[0], (BA[1] < BB[1]) ? BB[1] : BA[1], (BA[2] < BB[2]) ? BB[2] : BA[2]};

        //计算立方体的xyz长
        int XLength = (int) Math.sqrt(Math.pow((double) realAA[0] - realAB[0], 2));
        int YLength = (int) Math.sqrt(Math.pow((double) realAA[1] - realAB[1], 2));
        int ZLength = (int) Math.sqrt(Math.pow((double) realAA[2] - realAB[2], 2));

        //开始转换
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    this.swapLocations(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        //更新方块状态
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    this.updateBlock(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        //修正光照
        /*
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    //this.updateLight(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        */
        //实体传送
        List<Entity> originalWorldEntities;
        List<Entity> focusWorldEntities;
        AxisAlignedBB originalArea = new AxisAlignedBB(realAA[0], realAA[1], realAA[2], realAB[0], realAB[1], realAB[2]);
        originalWorldEntities = initialWorld.getEntitiesWithinAABB(Entity.class, originalArea);
        AxisAlignedBB focusArea = new AxisAlignedBB(realBA[0], realBA[1], realBA[2], realBB[0], realBB[1], realBB[2]);
        focusWorldEntities = finalWorld.getEntitiesWithinAABB(Entity.class, focusArea);

        if (!originalWorldEntities.isEmpty()) {
            for (Entity entity : originalWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realAA[0] + realBA[0], entity.posY - realAA[1] + realBA[1], entity.posZ - realAA[2] + realBA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, bindingOwnerID, true));
            }
        }
        if (!focusWorldEntities.isEmpty()) {
            for (Entity entity : focusWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realBA[0] + realAA[0], entity.posY - realBA[1] + realAA[1], entity.posZ - realBA[2] + realAA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - focusPos.getX() + pos.getX(), entity.posY - focusPos.getY() + pos.getY(), entity.posZ - focusPos.getZ() + pos.getZ()), entity, bindingOwnerID, true));
            }
        }


        return true;
    }

    public boolean moveMapPiece(World initialWorld, int[] AA, int[] AB, World finalWorld, int[] BA, int[] BB) {
        //判断A范围与B范围大小是否一致
        int AXSize = AA[0] - AB[0];
        int BXSize = BA[0] - BB[0];
        int AYSize = AA[1] - AB[1];
        int BYSize = BA[1] - BB[1];
        int AZSize = AA[2] - AB[2];
        int BZSize = BA[2] - BB[2];
        if (Math.abs(AXSize) != Math.abs(BXSize) || Math.abs(AYSize) != Math.abs(BYSize) || Math.abs(AZSize) != Math.abs(BZSize)) {
            return false;
        }
        //寻找立方体准确顶点A为XYZ最小 B为XYZ最大
        int[] realAA = {(AA[0] > AB[0]) ? AB[0] : AA[0], (AA[1] > AB[1]) ? AB[1] : AA[1], (AA[2] > AB[2]) ? AB[2] : AA[2]};
        int[] realAB = {(AA[0] < AB[0]) ? AB[0] : AA[0], (AA[1] < AB[1]) ? AB[1] : AA[1], (AA[2] < AB[2]) ? AB[2] : AA[2]};
        int[] realBA = {(BA[0] > BB[0]) ? BB[0] : BA[0], (BA[1] > BB[1]) ? BB[1] : BA[1], (BA[2] > BB[2]) ? BB[2] : BA[2]};
        int[] realBB = {(BA[0] < BB[0]) ? BB[0] : BA[0], (BA[1] < BB[1]) ? BB[1] : BA[1], (BA[2] < BB[2]) ? BB[2] : BA[2]};

        //计算立方体的xyz长
        int XLength = (int) Math.sqrt(Math.pow((double) realAA[0] - realAB[0], 2));
        int YLength = (int) Math.sqrt(Math.pow((double) realAA[1] - realAB[1], 2));
        int ZLength = (int) Math.sqrt(Math.pow((double) realAA[2] - realAB[2], 2));

        //开始转换
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    this.swapLocations(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        //更新方块状态
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    this.updateBlock(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        //修正光照
        /*
        for (int tempA = XLength; tempA >= 0; tempA--) {//X
            for (int tempB = ZLength; tempB >= 0; tempB--) {//Z
                for (int tempC = YLength; tempC >= 0; tempC--) {//Y
                    //this.updateLight(initialWorld, new BlockPos(realAA[0] + tempA, realAA[1] + tempC, realAA[2] + tempB), finalWorld, new BlockPos(realBA[0] + tempA, realBA[1] + tempC, realBA[2] + tempB));
                }
            }
        }
        */
        //实体传送
        List<Entity> originalWorldEntities;
        List<Entity> focusWorldEntities;
        AxisAlignedBB originalArea = new AxisAlignedBB(realAA[0], realAA[1], realAA[2], realAB[0], realAB[1], realAB[2]);
        originalWorldEntities = initialWorld.getEntitiesWithinAABB(Entity.class, originalArea);
        AxisAlignedBB focusArea = new AxisAlignedBB(realBA[0], realBA[1], realBA[2], realBB[0], realBB[1], realBB[2]);
        focusWorldEntities = finalWorld.getEntitiesWithinAABB(Entity.class, focusArea);

        if (!originalWorldEntities.isEmpty()) {
            for (Entity entity : originalWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realAA[0] + realBA[0], entity.posY - realAA[1] + realBA[1], entity.posZ - realAA[2] + realBA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, bindingOwnerID, true));
            }
        }
        if (!focusWorldEntities.isEmpty()) {
            for (Entity entity : focusWorldEntities) {
                moveEntity(finalWorld, entity, entity.posX - realBA[0] + realAA[0], entity.posY - realBA[1] + realAA[1], entity.posZ - realBA[2] + realAA[2]);
                //TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - focusPos.getX() + pos.getX(), entity.posY - focusPos.getY() + pos.getY(), entity.posZ - focusPos.getZ() + pos.getZ()), entity, bindingOwnerID, true));
            }
        }


        return true;
    }

    public boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos) {
        TileEntity initialTile = initialWorld.getTileEntity(initialPos);
        TileEntity finalTile = finalWorld.getTileEntity(finalPos);
        NBTTagCompound initialTag = new NBTTagCompound();
        NBTTagCompound finalTag = new NBTTagCompound();
        if (initialTile != null)
            initialTile.writeToNBT(initialTag);
        if (finalTile != null)
            finalTile.writeToNBT(finalTag);

        IBlockState initialState = initialWorld.getBlockState(initialPos);
        IBlockState finalState = finalWorld.getBlockState(finalPos);

        if ((initialState.getBlock().equals(Blocks.AIR) && finalState.getBlock().equals(Blocks.AIR)) || initialState.getBlock() instanceof BlockPortal || finalState.getBlock() instanceof BlockPortal)
            return false;


        //Finally, we get to do something! (CLEARING TILES)
        if (finalState.getBlock().hasTileEntity(finalState))
            finalWorld.removeTileEntity(finalPos);
        if (initialState.getBlock().hasTileEntity(initialState))
            initialWorld.removeTileEntity(initialPos);

        //TILES CLEARED
        IBlockState initialBlockState = initialWorld.getBlockState(initialPos);
        IBlockState finalBlockState = finalWorld.getBlockState(finalPos);
        tkkSetBlockState(finalWorld, finalPos, initialBlockState, 3);

        if (initialTile != null) {
            TileEntity newTileInitial = TileEntity.create(finalWorld, initialTag);


            finalWorld.setTileEntity(finalPos, newTileInitial);
            newTileInitial.setPos(finalPos);
            newTileInitial.setWorld(finalWorld);
        }

        tkkSetBlockState(initialWorld, initialPos, finalBlockState, 3);

        if (finalTile != null) {
            TileEntity newTileFinal = TileEntity.create(initialWorld, finalTag);

            initialWorld.setTileEntity(initialPos, newTileFinal);
            newTileFinal.setPos(initialPos);
            newTileFinal.setWorld(initialWorld);
        }


        return true;
    }

    public IBlockState pasteLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos) {
        TileEntity initialTile = initialWorld.getTileEntity(initialPos);
        IBlockState finalState = finalWorld.getBlockState(finalPos);
        if (finalState.getBlock().hasTileEntity(finalState))
            finalWorld.removeTileEntity(finalPos);
        //TILES CLEARED
        IBlockState initialBlockState = initialWorld.getBlockState(initialPos);
        IBlockState finalBlockState = finalWorld.getBlockState(finalPos);
        tkkSetBlockState(finalWorld, finalPos, initialBlockState, 3);

        if (initialTile != null) {
            NBTTagCompound initialTag = new NBTTagCompound();
            initialTile.writeToNBT(initialTag);
            TileEntity newTileInitial = TileEntity.create(finalWorld, initialTag);


            finalWorld.setTileEntity(finalPos, newTileInitial);
            newTileInitial.setPos(finalPos);
            newTileInitial.setWorld(finalWorld);
        }


        return finalBlockState;
    }

    public void updateBlock(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos) {
        IBlockState initialState = initialWorld.getBlockState(initialPos);
        IBlockState finalState = finalWorld.getBlockState(finalPos);
        initialWorld.notifyNeighborsOfStateChange(initialPos, finalState.getBlock(), true);
        finalWorld.notifyNeighborsOfStateChange(finalPos, initialState.getBlock(), true);
        Chunk initiaChunk = initialWorld.getChunkFromBlockCoords(initialPos);
        tkkMarkAndNotifyBlock(initialWorld,initialPos,initiaChunk,finalWorld.getBlockState(finalPos),initialWorld.getBlockState(initialPos),3);
        Chunk finalChunk = finalWorld.getChunkFromBlockCoords(finalPos);
        tkkMarkAndNotifyBlock(finalWorld,finalPos,finalChunk,initialWorld.getBlockState(initialPos),finalWorld.getBlockState(finalPos),3);
    }

    public void updateLight(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos) {
        initialWorld.checkLight(initialPos);
        finalWorld.checkLight(finalPos);
    }

    public boolean tkkSetBlockState(World world, BlockPos pos, IBlockState newState, int flags) {
        if (world.isOutsideBuildHeight(pos)) {
            return false;
        } else if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            Chunk chunk = world.getChunkFromBlockCoords(pos);

            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            IBlockState oldState = world.getBlockState(pos);
            int oldLight = oldState.getLightValue(world, pos);
            int oldOpacity = oldState.getLightOpacity(world, pos);

            //IBlockState iblockstate = chunk.setBlockState(pos, newState);
            IBlockState iblockstate = setBlockState(chunk,pos, newState);

            if (iblockstate != null) {
                if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight) {
                    world.profiler.startSection("checkLight");
                    world.checkLight(pos);
                    world.profiler.endSection();
                }
            }
        }
        return true;
    }

    public void tkkMarkAndNotifyBlock(World world, BlockPos pos, @Nullable Chunk chunk, IBlockState iblockstate, IBlockState newState, int flags) {
        Block block = newState.getBlock();
        {
            {
                if ((flags & 2) != 0 && (!world.isRemote || (flags & 4) == 0) && (chunk == null || chunk.isPopulated())) {
                    world.notifyBlockUpdate(pos, iblockstate, newState, flags);
                }

                if (!world.isRemote && (flags & 1) != 0) {
                    //world.notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);

                    if (newState.hasComparatorInputOverride()) {
                        world.updateComparatorOutputLevel(pos, block);
                    }
                } else if (!world.isRemote && (flags & 16) == 0) {
                    //world.updateObservingBlocksAt(pos, block);
                }
            }
        }
    }

    public void moveEntity(World world, Entity entity, double x, double y, double z) {
        if (entity.getEntityWorld().equals(world)) {
            //同世界传送
            if (entity != null) {
                BlockPos targetTeleposer = new BlockPos(x, y, z);
                if (entity.timeUntilPortal <= 0) {
                    entity.timeUntilPortal = 0;
                    if (entity instanceof EntityPlayer) {
                        EntityPlayerMP player = (EntityPlayerMP) entity;


                        player.setPositionAndUpdate(x,y,z);
                        //player.setPosition(x,y,z);
                        player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                        player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                    } else {
                        WorldServer Sworld = (WorldServer) entity.getEntityWorld();
                        entity.setPosition(x,y,z);
                        Sworld.resetUpdateEntityTick();
                    }
                }
            }
        } else {
            //不同世界传送
        }
    }

    public void moveEntity(World world, Entity entity, double x, double y, double z,float yaw,float pitch) {
        if (entity.getEntityWorld().equals(world)) {
            //同世界传送
            if (entity != null) {
                BlockPos targetTeleposer = new BlockPos(x, y, z);
                if (entity.timeUntilPortal <= 0) {
                    entity.timeUntilPortal = 0;
                    if (entity instanceof EntityPlayer) {
                        EntityPlayerMP player = (EntityPlayerMP) entity;


                        //player.setPositionAndUpdate(x,y,z);
                        //player.setPosition(x,y,z);
                        player.connection.setPlayerLocation(x,y,z,yaw,pitch);
                        player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                        player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
                    } else {
                        WorldServer Sworld = (WorldServer) entity.getEntityWorld();
                        //entity.setPosition(x,y,z);
                        entity.setPositionAndRotation(x,y,z,yaw,pitch);
                        Sworld.resetUpdateEntityTick();
                    }
                }
            }
        } else {
            //不同世界传送
        }
    }


    public void pasteEntity(World world, Entity entity, double x, double y, double z) {
        if (entity.getEntityWorld().equals(world)) {
            //同世界传送
            if (entity != null) {
                BlockPos targetTeleposer = new BlockPos(x, y, z);
                if (entity.timeUntilPortal <= 0) {
                    entity.timeUntilPortal = 0;
                    if (entity instanceof EntityPlayer) {
                        return;
                    } else {
                        //WorldServer Sworld = (WorldServer) entity.getEntityWorld();
                        //entity.setPosition(x,y,z);
                        //Sworld.resetUpdateEntityTick();
                        NBTTagCompound nbt = new NBTTagCompound();
                        entity.writeToNBTAtomically(nbt);
                        nbt.setUniqueId("UUID", UUID.randomUUID());
                        Entity entity1 = EntityList.createEntityFromNBT(nbt,world);
                        if(entity1==null){
                            TkkGameLib.logger.log(Level.DEBUG,nbt);
                            return;
                        }
                        entity1.setPosition(x,y,z);
                        world.spawnEntity(entity1);

                    }
                }
            }
        } else {
            //不同世界传送
        }
    }


    @Nullable
    public IBlockState setBlockState(Chunk chunk,BlockPos pos, IBlockState state) {
        IBlockState iblockstate = chunk.getBlockState(pos);
        Block block = state.getBlock();
        Block block1 = iblockstate.getBlock();
        if (iblockstate == state)
        {
            return null;
        }
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[pos.getY() >> 4];
        if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
        {
            if (block == Blocks.AIR)
            {
                return null;
            }

            extendedblockstorage = new ExtendedBlockStorage(pos.getY() >> 4 << 4, chunk.getWorld().provider.hasSkyLight());
            chunk.getBlockStorageArray()[pos.getY() >> 4] = extendedblockstorage;
            //chunk. = j >= i1;
        }
        extendedblockstorage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
        /*
        if (!chunk.getWorld().isRemote)
        {
            if (block1 != block) //Only fire block breaks when the block changes.
                block1.breakBlock(chunk.getWorld(), pos, iblockstate);
            TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
            if (te != null && te.shouldRefresh(chunk.getWorld(), pos, iblockstate, state)) chunk.getWorld().removeTileEntity(pos);
        }
        else if (block1.hasTileEntity(iblockstate))
        {
            TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
            if (te != null && te.shouldRefresh(chunk.getWorld(), pos, iblockstate, state))
                chunk.getWorld().removeTileEntity(pos);
        }
        */
        if (extendedblockstorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15).getBlock() != block)
        {
            return null;
        }
        //else{
        //    if (!chunk.getWorld().isRemote && block1 != block && (!chunk.getWorld().captureBlockSnapshots || block.hasTileEntity(state)))
        //    {
        //        //block.onBlockAdded(chunk.getWorld(), pos, state);
        //    }
        //}

        return iblockstate;
    }

    public static void updateBlock(World world, BlockPos pos,IBlockState old) {
        IBlockState state = world.getBlockState(pos);
        world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
        Chunk chunk = world.getChunkFromBlockCoords(pos);

        Block block = state.getBlock();
        if ((3 & 2) != 0 && (!world.isRemote || (3 & 4) == 0) && (chunk == null || chunk.isPopulated())) {
            world.notifyBlockUpdate(pos, old, state, 3);
        }
        if (!world.isRemote && (3 & 1) != 0) {
            //world.notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);

            if (state.hasComparatorInputOverride()) {
                world.updateComparatorOutputLevel(pos, block);
            }
        }
    }

    public static int randomNumber(int min,int max){
        Random rand=new Random();
        int numberA=max-min+1;
        return rand.nextInt(numberA)+min;
    }



















}