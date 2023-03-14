// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports
package com.twokktwo.tkklib.custonActions.Model;

import com.twokktwo.tkklib.custonActions.Capability.ActionCapbility;
import com.twokktwo.tkklib.custonActions.custom.CustomModelBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class CustomAction extends CustomModelBase {
	public final ModelRenderer body;
	public final ModelRenderer RightLeg;
	public final ModelRenderer RightCalf;
	public final ModelRenderer LeftLeg;
	public final ModelRenderer LeftCalf;
	public final ModelRenderer LeftArm;
	public final ModelRenderer LeftForearm;
	public final ModelRenderer RightArm;
	public final ModelRenderer RightForearm;
	public final ModelRenderer Head;

	public ModelBiped.ArmPose leftArmPose;
	public ModelBiped.ArmPose rightArmPose;
	public boolean isSneak;

	public float partialTick;

	public CustomAction() {
		textureWidth = 64;
		textureHeight = 64;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.cubeList.add(new ModelBox(Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(-5.0F, 1.0F, -1.0F);
		RightArm.cubeList.add(new ModelBox(RightArm, 16, 16, -3.0F, -1.0F, -1.0F, 4, 6, 4, 0.0F, false));

		RightForearm = new ModelRenderer(this);
		RightForearm.setRotationPoint(-1.0F, 5.0F, 3.0F);
		RightArm.addChild(RightForearm);
		RightForearm.cubeList.add(new ModelBox(RightForearm, 0, 16, -2.0F, 0.0F, -4.0F, 4, 6, 4, 0.0F, false));

		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(5.0F, 1.0F, -1.0F);
		LeftArm.cubeList.add(new ModelBox(LeftArm, 0, 26, -1.0F, -1.0F, -1.0F, 4, 6, 4, 0.0F, false));

		LeftForearm = new ModelRenderer(this);
		LeftForearm.setRotationPoint(1.0F, 5.0F, 3.0F);
		LeftArm.addChild(LeftForearm);
		LeftForearm.cubeList.add(new ModelBox(LeftForearm, 32, 16, -2.0F, 0.0F, -4.0F, 4, 6, 4, 0.0F, false));

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(2.0F, 10.0F, 0.0F);
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 32, 26, -2.0F, 2.0F, -2.0F, 4, 6, 4, 0.0F, false));

		LeftCalf = new ModelRenderer(this);
		LeftCalf.setRotationPoint(0.0F, 8.0F, -2.0F);
		LeftLeg.addChild(LeftCalf);
		LeftCalf.cubeList.add(new ModelBox(LeftCalf, 16, 26, -2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F, false));

		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-2.0F, 10.0F, 0.0F);
		RightLeg.cubeList.add(new ModelBox(RightLeg, 16, 36, -2.0F, 2.0F, -2.0F, 4, 6, 4, 0.0F, false));

		RightCalf = new ModelRenderer(this);
		RightCalf.setRotationPoint(0.0F, 8.0F, -2.0F);
		RightLeg.addChild(RightCalf);
		RightCalf.cubeList.add(new ModelBox(RightCalf, 0, 36, -2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F, false));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 32, 0, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));


	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if(entity.getCapability(ActionCapbility.actionCap,null).getEnable()){
			entity.getCapability(ActionCapbility.actionCap,null).runJs("CustomActionRender",entity.getCapability(ActionCapbility.actionCap,null),this,entity,f,f1,f2,f3,f4,f5);
			return;
		}

		setRotationAngles(f,f1,f2,f3,f4,f5,entity);
		Head.render(f5);
		RightArm.render(f5);
		LeftArm.render(f5);
		LeftLeg.render(f5);
		RightLeg.render(f5);
		body.render(f5);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		updateStatus(entityIn);
		if(entityIn.getCapability(ActionCapbility.actionCap,null).getEnable()){
			entityIn.getCapability(ActionCapbility.actionCap,null).runJs("CustomActionSetRotationAngles",entityIn.getCapability(ActionCapbility.actionCap,null),this,entityIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor);
			return;
		}
		runningAction(actionType.INIT,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.HEAD_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.ARM_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.LEG_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.RIDING,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.LEFT_ARM_POSE,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.RIGHT_ARM_POSE,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.SWING_ARM,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.SNEAK,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.NORMAL_OSCILLATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		runningAction(actionType.BOW_AND_ARROW,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
	}

	public void updateStatus(Entity entityIn){

		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack mainHand;
		ItemStack offHand;
		if(entityIn instanceof EntityLivingBase){
			EntityLivingBase entity=(EntityLivingBase) entityIn;
			mainHand=entity.getHeldItem(EnumHand.MAIN_HAND);
			offHand=entity.getHeldItem(EnumHand.OFF_HAND);
			if(!mainHand.isEmpty()){
				rightArmPose = ModelBiped.ArmPose.ITEM;
				if (entity.getItemInUseCount() > 0)
				{
					EnumAction enumaction = mainHand.getItemUseAction();

					if (enumaction == EnumAction.BLOCK)
					{
						rightArmPose = ModelBiped.ArmPose.BLOCK;
					}
					else if (enumaction == EnumAction.BOW)
					{
						rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
			if(!offHand.isEmpty()){
				leftArmPose = ModelBiped.ArmPose.ITEM;
				if (entity.getItemInUseCount() > 0)
				{
					EnumAction enumaction = offHand.getItemUseAction();

					if (enumaction == EnumAction.BLOCK)
					{
						leftArmPose = ModelBiped.ArmPose.BLOCK;
					}
					else if (enumaction == EnumAction.BOW)
					{
						leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
		}
		if(entityIn instanceof EntityLiving){
			EntityLiving entity=(EntityLiving) entityIn;
			mainHand=entity.getHeldItem(EnumHand.MAIN_HAND);
			offHand=entity.getHeldItem(EnumHand.OFF_HAND);
			if(!mainHand.isEmpty()){
				rightArmPose = ModelBiped.ArmPose.ITEM;
				if (entity.getItemInUseCount() > 0)
				{
					EnumAction enumaction = mainHand.getItemUseAction();

					if (enumaction == EnumAction.BLOCK)
					{
						rightArmPose = ModelBiped.ArmPose.BLOCK;
					}
					else if (enumaction == EnumAction.BOW)
					{
						rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
			if(!offHand.isEmpty()){
				leftArmPose = ModelBiped.ArmPose.ITEM;
				if (entity.getItemInUseCount() > 0)
				{
					EnumAction enumaction = offHand.getItemUseAction();

					if (enumaction == EnumAction.BLOCK)
					{
						leftArmPose = ModelBiped.ArmPose.BLOCK;
					}
					else if (enumaction == EnumAction.BOW)
					{
						leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
		}
		if(entityIn.isSneaking()){isSneak=true;}else{isSneak=false;}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime){
		partialTick=partialTickTime;
	}
	public void setModelAttributes(ModelBase model)
	{
		super.setModelAttributes(model);

		if (model instanceof ModelBiped || model instanceof CustomAction)
		{
			ModelBiped modelbiped = (ModelBiped)model;
			this.leftArmPose = modelbiped.leftArmPose;
			this.rightArmPose = modelbiped.rightArmPose;
			this.isSneak = modelbiped.isSneak;
		}
	}

	protected ModelRenderer getArmForSide(EnumHandSide side)
	{
		return side == EnumHandSide.LEFT ? this.LeftArm : this.RightArm;
	}

	protected EnumHandSide getMainHand(Entity entityIn)
	{
		if (entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
			EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
			return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
		}
		else
		{
			return EnumHandSide.RIGHT;
		}
	}
	public void postRenderArm(float scale, EnumHandSide side)
	{
		ModelRenderer arm=side == EnumHandSide.LEFT ? this.LeftArm : this.RightArm;
		ModelRenderer forearm=side == EnumHandSide.LEFT ? this.LeftForearm : this.RightForearm;
		//float offset=side == EnumHandSide.LEFT ? 3.5f : 3.5f;
		float offset=arm.rotationPointX/1.3f;
		this.postRender(arm,arm.rotateAngleX,arm.rotateAngleY,arm.rotateAngleZ,arm.rotationPointX,arm.rotationPointY,arm.rotationPointZ,scale);
		this.postRender(forearm,forearm.rotateAngleX,forearm.rotateAngleY,forearm.rotateAngleZ,forearm.rotationPointX,forearm.rotationPointY,forearm.rotationPointZ,scale);
	}
	public void setVisible(boolean visible)
	{
		this.body.showModel = visible;
		this.RightLeg.showModel = visible;
		this.RightCalf.showModel = visible;
		this.LeftLeg.showModel = visible;
		this.LeftCalf.showModel = visible;
		this.LeftArm.showModel = visible;
		this.LeftForearm.showModel = visible;
		this.RightArm.showModel = visible;
		this.RightForearm.showModel = visible;
		this.Head.showModel = visible;
	}

	public void runningAction(actionType type,float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		boolean flag;
		float f;
		switch (type){
			case INIT:
				body.setRotationPoint(0.0F, 0.0F, 0.0F);
				setRotationAngle(body,0,0,0);
				RightLeg.setRotationPoint(-2.0F, 10.0F, 0.0F);
				setRotationAngle(RightLeg,0,0,0);
				RightCalf.setRotationPoint(0.0F, 8.0F, -2.0F);
				setRotationAngle(RightCalf,0,0,0);
				LeftLeg.setRotationPoint(2.0F, 10.0F, 0.0F);
				setRotationAngle(LeftLeg,0,0,0);
				LeftCalf.setRotationPoint(0.0F, 8.0F, -2.0F);
				setRotationAngle(LeftCalf,0,0,0);
				LeftArm.setRotationPoint(5.0F, 1.0F, -1.0F);
				setRotationAngle(LeftArm,5.0f,0,-1.0f);
				LeftForearm.setRotationPoint(1.0F, 5.0F, 3.0F);
				setRotationAngle(LeftForearm,0,0,0);
				RightArm.setRotationPoint(-5.0F, 1.0F, -1.0F);
				setRotationAngle(RightArm,-5.0f,0,-1.0f);
				RightForearm.setRotationPoint(-1.0F, 5.0F, 3.0F);
				setRotationAngle(RightForearm,0,0,0);
				Head.setRotationPoint(0.0F, 0.0F, 0.0F);
				setRotationAngle(Head,0,0,0);
				break;
			case HEAD_ROTATION:
				flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;

				this.Head.rotateAngleY = netHeadYaw * 0.017453292F;

				if (flag){//如果是鞘翅飞行
					this.Head.rotateAngleX = -((float)Math.PI / 4F);
				}else{//否则
					this.Head.rotateAngleX = headPitch * 0.017453292F;
				}
				break;
			case ARM_ROTATION:
				flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
				f = 1.0F;

				if (flag){
					f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
					f = f / 0.2F;
					f = f * f * f;
				}

				if (f < 1.0F){
					f = 1.0F;
				}

				//手臂摆动
				this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
				this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
				this.RightArm.rotateAngleZ = 0.0F;
				this.LeftArm.rotateAngleZ = 0.0F;
				break;
			case LEG_ROTATION:
				flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
				f = 1.0F;

				if (flag){
					f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
					f = f / 0.2F;
					f = f * f * f;
				}

				if (f < 1.0F){
					f = 1.0F;
				}
				this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
				this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
				this.RightLeg.rotateAngleY = 0.0F;
				this.LeftLeg.rotateAngleY = 0.0F;
				this.RightLeg.rotateAngleZ = 0.0F;
				this.LeftLeg.rotateAngleZ = 0.0F;
				break;
			case RIDING:
				//坐 姿势
				if (this.isRiding)
				{
					this.body.rotationPointY=0.0f;
					this.RightArm.rotateAngleX += -((float)Math.PI / 5F);
					this.LeftArm.rotateAngleX += -((float)Math.PI / 5F);
					this.RightLeg.rotateAngleX = -1.4137167F;
					this.RightLeg.rotateAngleY = ((float)Math.PI / 10F);
					this.RightLeg.rotateAngleZ = 0.07853982F;
					this.LeftLeg.rotateAngleX = -1.4137167F;
					this.LeftLeg.rotateAngleY = -((float)Math.PI / 10F);
					this.LeftLeg.rotateAngleZ = -0.07853982F;
				}else{
					this.body.rotationPointY=0.0f;
				}
				break;
			case LEFT_ARM_POSE:
				switch (this.leftArmPose)
				{
					case EMPTY:
						this.LeftArm.rotateAngleY = 0.0F;
						break;
					case BLOCK:
						this.LeftArm.rotateAngleX = this.LeftArm.rotateAngleX * 0.5F - 0.9424779F;
						this.LeftArm.rotateAngleY = 0.5235988F;
						break;
					case ITEM:
						this.LeftArm.rotateAngleX = this.LeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
						this.LeftArm.rotateAngleY = 0.0F;
				}
				break;
			case RIGHT_ARM_POSE:
				switch (this.rightArmPose)
				{
					case EMPTY:
						this.RightArm.rotateAngleY = 0.0F;
						break;
					case BLOCK:
						this.RightArm.rotateAngleX = this.RightArm.rotateAngleX * 0.5F - 0.9424779F;
						this.RightArm.rotateAngleY = -0.5235988F;
						break;
					case ITEM:
						this.RightArm.rotateAngleX = this.RightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
						this.RightArm.rotateAngleY = 0.0F;
				}
				break;
			case SWING_ARM:
				if (this.swingProgress > 0.0F)
				{
					EnumHandSide enumhandside = this.getMainHand(entityIn);
					ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
					float f1 = this.swingProgress;
					this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float)Math.PI * 2F)) * 0.2F;

					if (enumhandside == EnumHandSide.LEFT)
					{
						this.body.rotateAngleY *= -1.0F;
					}

					this.RightArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
					this.RightArm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
					this.LeftArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
					this.LeftArm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
					this.RightArm.rotateAngleY += this.body.rotateAngleY;
					this.LeftArm.rotateAngleY += this.body.rotateAngleY;
					this.LeftArm.rotateAngleX += this.body.rotateAngleY;
					f1 = 1.0F - this.swingProgress;
					f1 = f1 * f1;
					f1 = f1 * f1;
					f1 = 1.0F - f1;
					float f2 = MathHelper.sin(f1 * (float)Math.PI);
					float f3 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.Head.rotateAngleX - 0.7F) * 0.75F;
					modelrenderer.rotateAngleX = (float)((double)modelrenderer.rotateAngleX - ((double)f2 * 1.2D + (double)f3));
					modelrenderer.rotateAngleY += this.body.rotateAngleY * 2.0F;
					modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
				}
				break;
			case SNEAK:
				if (this.isSneak){
					this.body.rotateAngleX = 0.36F;
					this.RightLeg.rotationPointZ = 4.0F;
					this.LeftLeg.rotationPointZ = 4.0F;
					this.RightLeg.rotationPointY = 9.0F;
					this.LeftLeg.rotationPointY = 9.0F;
					this.Head.rotationPointY = 1.0F;
				}else{
					this.body.rotateAngleX = 0.0F;
					this.RightLeg.rotationPointZ = 0.0F;
					this.LeftLeg.rotationPointZ = 0.0F;
					this.LeftLeg.rotationPointY = 0.0F;
					this.RightLeg.rotationPointY = 10.0F;
					this.LeftLeg.rotationPointY = 10.0F;
					this.Head.rotationPointY = 0.0F;
				}
				break;
			case NORMAL_OSCILLATION:
				this.RightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
				this.LeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
				this.RightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
				this.LeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
				break;
			case BOW_AND_ARROW:
				if (this.rightArmPose == ModelBiped.ArmPose.BOW_AND_ARROW)
				{
					this.RightArm.rotateAngleY = -0.1F + this.Head.rotateAngleY;
					this.LeftArm.rotateAngleY = 0.1F + this.Head.rotateAngleY + 0.4F;
					this.RightArm.rotateAngleX = -((float)Math.PI / 2F) + this.Head.rotateAngleX;
					this.LeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.Head.rotateAngleX;
				}
				else if (this.leftArmPose == ModelBiped.ArmPose.BOW_AND_ARROW)
				{
					this.RightArm.rotateAngleY = -0.1F + this.Head.rotateAngleY - 0.4F;
					this.LeftArm.rotateAngleY = 0.1F + this.Head.rotateAngleY;
					this.RightArm.rotateAngleX = -((float)Math.PI / 2F) + this.Head.rotateAngleX;
					this.LeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.Head.rotateAngleX;
				}
				break;

		}
	}

	public enum actionType{
		//坐标旋转初始化
		INIT,
		//头部旋转，系统会自动将旋转到一定程度再旋转身体，这个只是头部旋转至准星
		HEAD_ROTATION,
		//手臂
		ARM_ROTATION,
		//腿
		LEG_ROTATION,
		//骑乘
		RIDING,
		//左手动作，是指你手里拿个方块物品啥的抬起来一点
		LEFT_ARM_POSE,
		//右手动作
		RIGHT_ARM_POSE,
		//挥动手臂，攻击放置方块什么的
		SWING_ARM,
		//潜行
		SNEAK,
		//自然晃动，就是你手臂自己前后晃的那个
		NORMAL_OSCILLATION,
		//弓动作
		BOW_AND_ARROW
	}
}