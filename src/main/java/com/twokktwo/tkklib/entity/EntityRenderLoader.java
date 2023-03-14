package com.twokktwo.tkklib.entity;

import com.twokktwo.tkklib.entity.Render.testBRender;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityRenderLoader {

    @SubscribeEvent
    public static void bindEntityRenderer(ModelRegistryEvent event) {
        // 自然也可以用 method reference。这里写成 lambda 只是为了更加明显。
        RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, testBRender::new);

    }
    public EntityRenderLoader()
    {
        EntityLoader.registerRenders();
    }
}
