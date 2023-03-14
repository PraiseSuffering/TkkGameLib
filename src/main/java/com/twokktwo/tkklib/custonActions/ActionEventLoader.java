package com.twokktwo.tkklib.custonActions;

import com.twokktwo.tkklib.custonActions.Model.CustomAction;
import com.twokktwo.tkklib.custonActions.Render.ActionRender;
import com.twokktwo.tkklib.custonActions.Render.FirstPersonActionRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActionEventLoader {
    public static ActionRender ACTION_RENDER;
    public static FirstPersonActionRender FIRST_PERSON_ACTION_RENDER;
    public static Minecraft mc;
    public static boolean enable=true;
    public static boolean FIRST_PERSON_ENABLE=true;
    public ActionEventLoader(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
    }
    public ActionEventLoader(FMLInitializationEvent event){
        ACTION_RENDER=new ActionRender(Minecraft.getMinecraft().getRenderManager());
        FIRST_PERSON_ACTION_RENDER=new FirstPersonActionRender(Minecraft.getMinecraft().getRenderManager());
        mc=Minecraft.getMinecraft();
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderLivingEvent(RenderLivingEvent.Pre<? extends EntityLivingBase> event){
        if(!enable){return;}
        EntityLivingBase livingentity = event.getEntity();
        RenderLivingBase renderLiving = event.getRenderer();
        boolean doRender=false;
        if(renderLiving instanceof ActionRender || renderLiving instanceof FirstPersonActionRender){return;}
        if(livingentity instanceof AbstractClientPlayer){
            doRender=true;
        }
        if(doRender){
            event.setCanceled(true);
            ACTION_RENDER.setRenderOutlines(false);
            ACTION_RENDER.doRender(livingentity,event.getX(),event.getY(),event.getZ(),event.getEntity().rotationYaw,event.getPartialRenderTick());
            ACTION_RENDER.doRenderShadowAndFire(livingentity, event.getX(),event.getY(),event.getZ(),event.getEntity().rotationYaw,event.getPartialRenderTick());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderLivingEvent(RenderSpecificHandEvent event){
         if(Minecraft.getMinecraft().getRenderManager().renderViewEntity!=null && FIRST_PERSON_ENABLE){
            event.setCanceled(true);
             //TkkGameLib.print("posX"+Minecraft.getMinecraft().player.posX);
            //TkkGameLib.print(Minecraft.getMinecraft().player.getDistanceSq(new BlockPos(0,0,0))+"");
            AbstractClientPlayer abstractclientplayer = this.mc.player;

            ((CustomAction)FIRST_PERSON_ACTION_RENDER.getMainModel()).Head.showModel=false;
            FIRST_PERSON_ACTION_RENDER.setRenderOutlines(false);
            FIRST_PERSON_ACTION_RENDER.doRender(abstractclientplayer,0,-abstractclientplayer.getEyeHeight(),0,abstractclientplayer.rotationYaw,event.getPartialTicks());
            FIRST_PERSON_ACTION_RENDER.doRenderShadowAndFire(abstractclientplayer,0,-abstractclientplayer.getEyeHeight(),0,abstractclientplayer.rotationYaw,event.getPartialTicks());
         }
    }

}
