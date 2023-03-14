// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports
package com.twokktwo.tkklib.entity.Model;

import com.twokktwo.tkklib.js.jsStorageTool;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;





public class A extends ModelBase {
	private final ModelRenderer AA;
	private final ModelRenderer BB;
	private final ModelRenderer CC;

	public A() {
		textureWidth = 16;
		textureHeight = 16;

		AA = new ModelRenderer(this);
		AA.setRotationPoint(0.0F, 23.0F, 0.0F);
		AA.cubeList.add(new ModelBox(AA, 6, 2, -1.0F, -3.0F, -1.0F, 2, 2, 2, 2.0F, false));

		BB = new ModelRenderer(this);
		BB.setRotationPoint(0.0F, -6.0F, 0.0F);
		AA.addChild(BB);
		BB.cubeList.add(new ModelBox(BB, 0, 4, -1.0F, -2.0F, -1.0F, 2, 2, 2, 1.0F, false));

		CC = new ModelRenderer(this);
		CC.setRotationPoint(0.0F, -4.0F, 0.0F);
		BB.addChild(CC);
		CC.cubeList.add(new ModelBox(CC, 6, 6, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f,f1,f2,f3,f4,f5,entity);
		AA.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		super.setRotationAngles(limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entityIn);
		setRotationAngle(AA,getData("A",0f),0f,0f);
		setRotationAngle(BB,getData("B",0f),0f,0f);
		setRotationAngle(CC,getData("C",0f),0f,0f);
	}

	public float getData(String name,float def){
		Object temp= jsStorageTool.getTempMap("test").get(name);
		if(temp instanceof String){return Float.parseFloat((String) temp);}
		return 0.0f;
	}
}