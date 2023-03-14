package com.twokktwo.tkklib.entity;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLoader {
    private static int nextID = 0;

    @SubscribeEvent
    public static void onEntityRegistation(RegistryEvent.Register<EntityEntry> event) {
        /*event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityTest.class)
                .id(new ResourceLocation(TkkGameLib.MODID, "my_entity"), nextID++)
                .name("MyEntity")
                .tracker(80, 3, false)
                .build()
        );
         */
        }

    public EntityLoader()
    {
        registerEntity(new ResourceLocation(TkkGameLib.MODID, "test_entity"),EntityTest.class,"testEntity",80,5,true);
        //registerEntity(new ResourceLocation(TkkGameLib.MODID, "my_entity"),EntityTest.class, "GoldenChicken", 80, 3, true);
    }

    private static void registerEntity(ResourceLocation RL, Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(RL,entityClass, name, nextID++, TkkGameLib.instance, trackingRange, updateFrequency,
                sendsVelocityUpdates);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders()
    {
        // TODO
    }

    @SideOnly(Side.CLIENT)
    private static <T extends Entity> void registerEntityRender(Class<T> entityClass, Class<? extends Render<T>> render)
    {
        //RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRender<T>(render));
    }
}