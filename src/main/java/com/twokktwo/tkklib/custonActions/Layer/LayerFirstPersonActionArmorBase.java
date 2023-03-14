package com.twokktwo.tkklib.custonActions.Layer;

import com.google.common.collect.Maps;
import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.custonActions.Model.ArmorModel;
import com.twokktwo.tkklib.custonActions.Model.CustomAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class LayerFirstPersonActionArmorBase implements LayerRenderer<EntityLivingBase>
{
    public static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public ModelBiped modelLeggings;
    public ModelBiped modelArmor;
    public final RenderLivingBase<?> renderer;
    public float alpha = 1.0F;
    public float colorR = 1.0F;
    public float colorG = 1.0F;
    public float colorB = 1.0F;
    public boolean skipRenderGlint;
    public ArmorModel actionLeg;
    public ArmorModel actionArm;
    public static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();
    public static final Map<ModelBiped,ArmorModel> JOINT_HAS_BEEN_ADDED= Maps.<ModelBiped, ArmorModel>newHashMap();

    public LayerFirstPersonActionArmorBase(RenderLivingBase<?> rendererIn)
    {
        this.renderer = rendererIn;
        this.initArmor();
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(entitylivingbaseIn.getCapability(ActionCapbility.actionCap,null).getEnable()){
            entitylivingbaseIn  .getCapability(ActionCapbility.actionCap,null).runJs("LayerActionArmorBaseDoRenderLayer",entitylivingbaseIn.getCapability(ActionCapbility.actionCap,null),this,entitylivingbaseIn,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale);
            return;
        }
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }

    public void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

        if (itemstack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();

            if (itemarmor.getEquipmentSlot() == slotIn)
            {
                ModelBiped t = this.getModelFromSlot(slotIn);
                ArmorModel t2 = JOINT_HAS_BEEN_ADDED.get(t);
                t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
                t.setModelAttributes(this.renderer.getMainModel());
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
                t2.synchronousAllData((CustomAction) this.renderer.getMainModel());
                this.setModelSlotVisible(t2, slotIn);
                boolean flag = this.isLegSlot(slotIn);
                this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
                {
                    if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
                    {
                        int i = itemarmor.getColor(itemstack);
                        float f = (float)(i >> 16 & 255) / 255.0F;
                        float f1 = (float)(i >> 8 & 255) / 255.0F;
                        float f2 = (float)(i & 255) / 255.0F;
                        GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                        //t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        t2.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
                    }
                    { // Non-colored
                        GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                        t2.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        //t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                    } // Default
                    if (!this.skipRenderGlint && itemstack.hasEffect())
                    {
                        renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                    }
                }
            }
        }
    }

    public ModelBiped getModelFromSlot(EntityEquipmentSlot slotIn)
    {
        return (ModelBiped)(this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor);
    }

    public boolean isLegSlot(EntityEquipmentSlot slotIn)
    {
        return slotIn == EntityEquipmentSlot.LEGS;
    }

    public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_)
    {
        float f = (float)p_188364_1_.ticksExisted + p_188364_5_;
        p_188364_0_.bindTexture(ENCHANTED_ITEM_GLINT_RES);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            float f2 = 0.76F;
            GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            model.render(p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
    }

    @Deprecated //Use the more sensitive version getArmorResource below
    public ResourceLocation getArmorResource(ItemArmor armor, boolean p_177181_2_)
    {
        return this.getArmorResource(armor, p_177181_2_, (String)null);
    }

    @Deprecated //Use the more sensitive version getArmorResource below
    public ResourceLocation getArmorResource(ItemArmor armor, boolean p_177178_2_, String p_177178_3_)
    {
        String s = String.format("textures/models/armor/%s_layer_%d%s.png", armor.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, p_177178_3_ == null ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s);
            ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }

        return resourcelocation;
    }

    public void initArmor(){
        this.modelLeggings = new ModelBiped(0.5F);
        this.modelArmor = new ModelBiped(1.0F);
        this.actionLeg=new ArmorModel(modelLeggings,0.7f);
        this.actionArm=new ArmorModel(modelArmor,1.0f);
        JOINT_HAS_BEEN_ADDED.put(modelArmor,actionArm);
        JOINT_HAS_BEEN_ADDED.put(modelLeggings,actionLeg);

    };

    public void setModelSlotVisible(ArmorModel p_188359_1_, EntityEquipmentSlot slotIn) {

        this.setModelVisible(p_188359_1_);

        switch (slotIn)
        {
            case HEAD:
                //p_188359_1_.Head.showModel = true;
                break;
            case CHEST:
                p_188359_1_.body.showModel = true;
                p_188359_1_.RightArm.showModel = true;
                p_188359_1_.LeftArm.showModel = true;
                p_188359_1_.RightForearm.showModel = true;
                p_188359_1_.LeftForearm.showModel = true;
                break;
            case LEGS:
                p_188359_1_.body.showModel = true;
                p_188359_1_.RightLeg.showModel = true;
                p_188359_1_.LeftLeg.showModel = true;
                p_188359_1_.LeftCalf.showModel = true;
                p_188359_1_.RightCalf.showModel = true;
                break;
            case FEET:
                p_188359_1_.RightLeg.showModel = true;
                p_188359_1_.LeftLeg.showModel = true;
                p_188359_1_.LeftCalf.showModel = true;
                p_188359_1_.RightCalf.showModel = true;

        }
    };
    public void setModelVisible(ArmorModel model)
    {
        model.setVisible(false);
    }
    /*=================================== FORGE START =========================================*/

    /**
     * Hook to allow item-sensitive armor model. for LayerBipedArmor.
     */
    public ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model)
    {
        ModelBiped temp = net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
        //return model;
        if(!JOINT_HAS_BEEN_ADDED.containsKey(temp)){
            JOINT_HAS_BEEN_ADDED.put(temp,new ArmorModel(temp));
        }
        return temp;
    }

    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack ItemStack for the armor
     * @param slot Slot ID that the item is in
     * @param type Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type)
    {
        ItemArmor item = (ItemArmor)stack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (isLegSlot(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
    /*=================================== FORGE END ===========================================*/
}
