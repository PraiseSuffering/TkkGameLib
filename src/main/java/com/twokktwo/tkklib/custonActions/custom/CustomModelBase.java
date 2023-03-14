package com.twokktwo.tkklib.custonActions.custom;

import com.twokktwo.tkklib.js.JsContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class CustomModelBase extends ModelBase {
    public void customRender(JsContainer js, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        js.run("ModelBaseRender",this,entity,f,f1,f2,f3,f4,f5);
    }


    public void postRender(ModelRenderer model,float rotateAngleX,float rotateAngleY,float rotateAngleZ,float rotationPointX,float rotationPointY,float rotationPointZ,float scale)
    {
        if (!model.isHidden)
        {
            if (model.showModel)
            {
                //if (!model.compiled)
                //{
                //    model.compileDisplayList(scale);
                //}

                if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F)
                {
                    if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F)
                    {
                        GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
                    }
                }
                else
                {
                    GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

                    if (rotateAngleZ != 0.0F)
                    {
                        GlStateManager.rotate(rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (rotateAngleY != 0.0F)
                    {
                        GlStateManager.rotate(rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (rotateAngleX != 0.0F)
                    {
                        GlStateManager.rotate(rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                    }
                }
            }
        }
    }
}
