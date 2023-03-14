package com.twokktwo.tkklib.entity.Render;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class testRender extends Render {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TkkGameLib.MODID + ":" + "texture/test.png");

    public testRender(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // 具体的渲染逻辑全部在此发生。
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // 你需要通过这个方法来声明你使用的实体渲染的纹理。
        // 实际上这个方法是必须要实现的，因为这个方法声明的时候有 abstract 修饰。
        // 但这里有一个坑：Minecraft 的渲染系统并不会主动调用这个方法！
        // 你需要在 doRender 里调用 bindEntityTexture（func_180548_c）来切换当前的纹理上下文到你的纹理上。
        return TEXTURE;
    }


}
