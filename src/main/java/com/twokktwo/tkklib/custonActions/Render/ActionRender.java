package com.twokktwo.tkklib.custonActions.Render;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.custonActions.Layer.LayerActionArmorBase;
import com.twokktwo.tkklib.custonActions.Layer.LayerArmourersWorkshopHeldItem;
import com.twokktwo.tkklib.custonActions.Layer.LayerHeldItem;
import com.twokktwo.tkklib.custonActions.Model.CustomAction;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class ActionRender  extends RenderLivingBase<EntityLivingBase> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(TkkGameLib.MODID + ":" + "texture/action.png");
    private static final CustomAction CUSTOM_ACTION=new CustomAction();

    public ActionRender(RenderManager renderManagerIn) {
        super(renderManagerIn, CUSTOM_ACTION, 0.5F);;
        setRenderOutlines(true);
        if(Loader.isModLoaded("armourers_workshop")){
            TkkGameLib.print("testArmourers");
            this.addLayer(new LayerArmourersWorkshopHeldItem(this));
        }else{
            this.addLayer(new LayerHeldItem(this));
        }
        this.addLayer(new LayerActionArmorBase(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(CUSTOM_ACTION.Head));
        this.addLayer(new LayerElytra(this));
        //this.addLayer(new LayerEntityOnShoulder(renderManager));


    }
    protected ResourceLocation getEntityTexture(EntityLivingBase entity) {
        if (entity.getCapability(ActionCapbility.actionCap, null).getEnable()) {
            return (ResourceLocation) entity.getCapability(ActionCapbility.actionCap, null).runJs("getEntityTexture", entity.getCapability(ActionCapbility.actionCap, null), this,entity);
        }
        return TEXTURE;
    }
    public boolean canRenderName(EntityLivingBase entity){
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }
    public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
