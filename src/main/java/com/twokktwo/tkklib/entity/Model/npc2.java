// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports
package com.twokktwo.tkklib.entity.Model;

import com.twokktwo.tkklib.js.jsStorageTool;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;




public class npc2 extends ModelBase {
	private final ModelRenderer RightLegwear;
	private final ModelRenderer LeftLegwear;
	private final ModelRenderer RightArmwear;
	private final ModelRenderer LeftArmwear;
	private final ModelRenderer Head;
	private final ModelRenderer Body;

	public npc2() {
		textureWidth = 64;
		textureHeight = 64;

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 12.0F, 0.0F);
		Body.cubeList.add(new ModelBox(Body, 0, 16, -4.0F, -12.0F, -2.0F, 8, 12, 4, 0.0F, false));

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.cubeList.add(new ModelBox(Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

		RightLegwear = new ModelRenderer(this);
		RightLegwear.setRotationPoint(-2.0F, 12.0F, 0.0F);
		RightLegwear.cubeList.add(new ModelBox(RightLegwear, 24, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

		LeftLegwear = new ModelRenderer(this);
		LeftLegwear.setRotationPoint(2.0F, 12.0F, 0.0F);
		LeftLegwear.cubeList.add(new ModelBox(LeftLegwear, 0, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

		RightArmwear = new ModelRenderer(this);
		RightArmwear.setRotationPoint(-4.0F, 1.0F, 0.0F);
		RightArmwear.cubeList.add(new ModelBox(RightArmwear, 32, 0, -4.0F, -1.0F, -2.0F, 4, 12, 4, 0.0F, false));

		LeftArmwear = new ModelRenderer(this);
		LeftArmwear.setRotationPoint(0.0F, 24.0F, 0.0F);
		LeftArmwear.cubeList.add(new ModelBox(LeftArmwear, 16, 32, 4.0F, -24.0F, -2.0F, 4, 12, 4, 0.0F, false));


	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f,f1,f2,f3,f4,f5,entity);
		RightLegwear.render(f5);
		LeftLegwear.render(f5);
		RightArmwear.render(f5);
		LeftArmwear.render(f5);
		Head.render(f5);
		Body.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		super.setRotationAngles(limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		RightArmwear.rotateAngleX=getData("x");
		RightArmwear.rotateAngleY=getData("y");
		RightArmwear.rotateAngleZ=getData("z");
	}

	public float getData(String name){
		Object temp= jsStorageTool.getTempMap("test").get(name);
		if(temp instanceof String){return Float.parseFloat((String) temp);}
		return 0.0f;
	}

}