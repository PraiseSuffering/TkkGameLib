package com.twokktwo.tkklib.entity.Render;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.entity.EntityTest;
import com.twokktwo.tkklib.entity.Model.A;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class testBRender extends RenderLiving<EntityTest> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TkkGameLib.MODID + ":" + "texture/aa.png");

    public testBRender(RenderManager renderManagerIn) {
        super(renderManagerIn, new A(), 0.5F);
    }
    protected ResourceLocation getEntityTexture(EntityTest entity) {
        return TEXTURE;
    }
    public boolean canRenderName(EntityTest entity){return true;}
    public void doRender(EntityTest entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (!this.renderOutlines)
        {
            this.renderLeash(entity, x, y, z, entityYaw, partialTicks);
        }
    }


}
