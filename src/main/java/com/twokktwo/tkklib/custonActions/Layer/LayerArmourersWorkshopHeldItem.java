package com.twokktwo.tkklib.custonActions.Layer;


import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.custonActions.Model.CustomAction;
import moe.plushie.armourers_workshop.api.common.skin.data.ISkinDescriptor;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.client.model.skin.ModelSkinBow;
import moe.plushie.armourers_workshop.client.render.SkinItemRenderHelper;
import moe.plushie.armourers_workshop.client.render.SkinModelRenderHelper;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinCache;
import moe.plushie.armourers_workshop.common.addons.ModAddonManager;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinCapability;
import moe.plushie.armourers_workshop.common.capability.entityskin.IEntitySkinCapability;
import moe.plushie.armourers_workshop.common.capability.wardrobe.ExtraColours;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class LayerArmourersWorkshopHeldItem implements LayerRenderer<EntityLivingBase> {
    public final RenderLivingBase<?> livingEntityRenderer;

    public LayerArmourersWorkshopHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        if (entitylivingbaseIn.getCapability(ActionCapbility.actionCap, null).getEnable()) {
            entitylivingbaseIn.getCapability(ActionCapbility.actionCap, null).runJs("LayerHeldItemDoRenderLayer", entitylivingbaseIn.getCapability(ActionCapbility.actionCap, null), this, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            return;
        }
        renderItem(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public void renderItem(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            IEntitySkinCapability skinCapability = EntitySkinCapability.get(entitylivingbaseIn);
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild) {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.75F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT,skinCapability);
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT,skinCapability);
            GlStateManager.popMatrix();
        }
    }

    public void renderHeldItem(EntityLivingBase p_188358_1_, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide, IEntitySkinCapability skinCapability) {
        if (!p_188358_2_.isEmpty()) {
            GlStateManager.pushMatrix();

            this.translateToHand(handSide);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate(0,0.25f,-0.4f);
            ISkinType[] skinTypes = new ISkinType[]{SkinTypeRegistry.skinSword, SkinTypeRegistry.skinShield, SkinTypeRegistry.skinBow, SkinTypeRegistry.skinPickaxe, SkinTypeRegistry.skinAxe, SkinTypeRegistry.skinShovel, SkinTypeRegistry.skinHoe, SkinTypeRegistry.skinItem};
            boolean didRender = false;

            for(int i = 0; i < ModAddonManager.ItemOverrideType.values().length; ++i) {
                ModAddonManager.ItemOverrideType overrideType = ModAddonManager.ItemOverrideType.values()[i];
                if (ModAddonManager.isOverrideItem(overrideType, p_188358_2_.getItem())) {
                    ISkinDescriptor descriptor = SkinNBTHelper.getSkinDescriptorFromStack(p_188358_2_);
                    if (descriptor == null) {
                        descriptor = skinCapability.getSkinDescriptor(skinTypes[i], 0);
                    }

                    if (descriptor != null) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableCull();
                        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                        GlStateManager.translate(0.0F, 0.125F, 0.125F);
                        if (flag) {
                            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
                            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
                        }

                        if (overrideType != ModAddonManager.ItemOverrideType.BOW) {
                            SkinItemRenderHelper.renderSkinWithoutHelper((ISkinDescriptor)descriptor, false);
                        } else {
                            Skin skin = ClientSkinCache.INSTANCE.getSkin((ISkinDescriptor)descriptor);
                            if (skin != null) {
                                int useCount = p_188358_1_.getItemInUseCount();
                                ModelSkinBow model = SkinModelRenderHelper.INSTANCE.modelBow;
                                model.frame = this.getAnimationFrame(p_188358_1_.getItemInUseMaxCount());
                                model.render(p_188358_1_, skin, false, ((ISkinDescriptor)descriptor).getSkinDye(), (ExtraColours)null, false, 0.0D, false);
                            }
                        }

                        if (flag) {
                            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
                        }

                        GlStateManager.disableCull();
                        GlStateManager.popMatrix();
                        didRender = true;
                        break;
                    }
                }
            }

            if (!didRender) {
                Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
            }

            GlStateManager.popMatrix();
        }
    }

    public void translateToHand(EnumHandSide p_191361_1_) {
        ((CustomAction) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
    private int getAnimationFrame(int useCount) {
        if (useCount >= 18) {
            return 2;
        } else {
            return useCount > 13 ? 1 : 0;
        }
    }
}
