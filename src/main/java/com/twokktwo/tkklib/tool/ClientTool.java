package com.twokktwo.tkklib.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
public class ClientTool {
    public static Minecraft mc=Minecraft.getMinecraft();
    private static Entity pointedEntity;

    @Nullable
    @SideOnly(Side.CLIENT)
    //https://github.com/ToroCraft/ToroHealth
    public static RayTraceResult rayTrace(Entity e, double blockReachDistance, float partialTicks) {
        Vec3d vec3d = e.getPositionEyes(partialTicks);
        Vec3d vec3d1 = e.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, true);
    }
    //https://github.com/ToroCraft/ToroHealth
    @SideOnly(Side.CLIENT)
    public static Entity getMouseEntity(double Distance) {
        RayTraceResult objectMouseOver;
        Entity observer = mc.getRenderViewEntity();

        if (observer == null || mc.world == null) {
            return null;
        }

        mc.pointedEntity = null;

        objectMouseOver = rayTrace(observer, Distance, 1);

        Vec3d observerPositionEyes = observer.getPositionEyes(1);

        double distance = Distance;

        if (objectMouseOver != null) {
            distance = objectMouseOver.hitVec.distanceTo(observerPositionEyes);
        }

        Vec3d lookVector = observer.getLook(1);
        Vec3d lookVectorFromEyePosition = observerPositionEyes.addVector(lookVector.x * Distance, lookVector.y * Distance,
                lookVector.z * Distance);
        pointedEntity = null;
        Vec3d hitVector = null;
        List<Entity> list = mc.world.getEntitiesInAABBexcluding(observer,
                observer.getEntityBoundingBox()
                        .expand(lookVector.x * Distance, lookVector.y * Distance, lookVector.z * Distance)
                        .expand(1.0D, 1.0D, 1.0D),
                EntitySelectors.NOT_SPECTATING);
        double d2 = distance;

        for (Entity entity : list) {
            Entity entity1 = (Entity) entity;
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(observerPositionEyes, lookVectorFromEyePosition);

            if (axisalignedbb.contains(observerPositionEyes)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    hitVector = raytraceresult == null ? observerPositionEyes : raytraceresult.hitVec;
                    d2 = 0.0D;
                }
            } else if (raytraceresult != null) {
                double d3 = observerPositionEyes.distanceTo(raytraceresult.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getLowestRidingEntity() == observer.getLowestRidingEntity() && !observer.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                            hitVector = raytraceresult.hitVec;
                        }
                    } else {
                        pointedEntity = entity1;
                        hitVector = raytraceresult.hitVec;
                        d2 = d3;
                    }
                }
            }
        }

        return pointedEntity;
    }
    @SideOnly(Side.CLIENT)
    public static BlockPos getMouseBlock(double Distance){
        EntityPlayerSP player=mc.player;
        WorldClient world=mc.world;

        Vec3d pos=player.getPositionEyes(1);
        Vec3d eye=player.getLookVec();
        Vec3d tarage = pos.add(eye.normalize().scale(3));
        RayTraceResult block = world.rayTraceBlocks(pos, tarage);
        return (block!=null)? block.getBlockPos() : null;
    }

}
