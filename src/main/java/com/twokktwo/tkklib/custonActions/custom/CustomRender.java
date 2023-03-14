package com.twokktwo.tkklib.custonActions.custom;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public abstract class CustomRender<T extends EntityLivingBase> extends RenderLivingBase<T> {
    public boolean doCustomRender=false;
    public CustomRender(RenderManager renderManagerIn, CustomModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (doCustomRender) {
            doCustomRender(entity, x, y, z, entityYaw, partialTicks);
        } else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    public abstract void doCustomRender(T entity, double x, double y, double z, float entityYaw, float partialTicks);



}
