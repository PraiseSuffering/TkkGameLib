package com.twokktwo.tkklib.custonActions.Model;

import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.tool.ReflectionTool;
import com.twokktwo.tkklib.tool.miscTool;
import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ArmorModel extends ModelBiped {
    public ModelBiped source;
    public float size;
    public ModelRenderer RightCalf;
    public ModelRenderer LeftCalf;
    public ModelRenderer LeftForearm;
    public ModelRenderer RightForearm;


    public ModelRenderer body;
    public ModelRenderer RightLeg;
    public ModelRenderer LeftLeg;
    public ModelRenderer LeftArm;
    public ModelRenderer RightArm;
    public ModelRenderer Head;

    public ArmorModel(ModelBiped src,float size){
        this.size=size;
        this.source=src;
        allToActionsModel();
    }
    public ArmorModel(ModelBiped src){
        this(src,1.0f);
    }


    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale){
        body.render(scale);
        RightLeg.render(scale);
        RightArm.render(scale);
        LeftArm.render(scale);
        LeftLeg.render(scale);
        Head.render(scale);
    }

    public void setVisible(boolean b){

        body.showModel=b;
        RightLeg.showModel=b;
        LeftLeg.showModel=b;
        LeftArm.showModel=b;
        RightArm.showModel=b;
        Head.showModel=b;

        RightCalf.showModel=b;
        LeftCalf.showModel=b;
        RightForearm.showModel=b;
        LeftForearm.showModel=b;
    }

    public void allToActionsModel(){
        TkkGameLib.print("source:"+source.getClass()+" | "+source);
        toActionModel("bipedRightArm",source.bipedRightArm);
        toActionModel("bipedLeftArm",source.bipedLeftArm);
        toActionModel("bipedRightLeg",source.bipedRightLeg);
        toActionModel("bipedLeftLeg",source.bipedLeftLeg);
        body=source.bipedBody;
        Head=source.bipedHead;
        RightArm=source.bipedRightArm;
        RightLeg=source.bipedRightLeg;
        LeftArm=source.bipedLeftArm;
        LeftLeg=source.bipedLeftLeg;
    }

    public void toActionModel(String name, ModelRenderer target){
        switch (name){
            case "bipedRightArm":
                target.cubeList.set(0,new ModelBox(target,40, 16,-3.0F, -1.0F, -1.0F, 4, 5, 4, size));
                RightForearm=new ModelRenderer(source,40, 26);
                RightForearm.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, size);
                RightForearm.setRotationPoint(0.0f,6.0f,0.0f);
                target.addChild(RightForearm);
                break;
            case "bipedLeftArm":
                target.cubeList.set(0,new ModelBox(target,40, 16,-1.0F, -1.0F, -1.0F, 4, 5, 4, size));
                LeftForearm=new ModelRenderer(source,40, 26);
                LeftForearm.mirror=true;
                LeftForearm.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, size);
                LeftForearm.setRotationPoint(0.0f,6.0f,0.0f);
                target.addChild(LeftForearm);
                break;
            case "bipedRightLeg":
                target.cubeList.set(0,new ModelBox(target,0,16,-2.0F, 1.0F, -2.0F, 4, 6, 4, size));
                RightCalf=new ModelRenderer(source,0, 22);
                RightCalf.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, size);
                RightCalf.setRotationPoint(0.0f,8.0f,-2.0f);
                target.addChild(RightCalf);
                try {
                    TexturedQuad[] quadList = (TexturedQuad[]) ReflectionTool.getField(RightCalf.cubeList.get(0),"quadList");
                    PositionTextureVertex[] vertexPositions=(PositionTextureVertex[]) ReflectionTool.getField(RightCalf.cubeList.get(0),"vertexPositions");
                    quadList[3] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[2], vertexPositions[3], vertexPositions[7], vertexPositions[6]}, 8, 16, 8 + 4, 16 + 4, RightCalf.textureWidth, RightCalf.textureHeight);
                } catch (Exception e) {
                    TkkGameLib.print(miscTool.getError(e));
                }
                break;
            case "bipedLeftLeg":
                target.cubeList.set(0,new ModelBox(target,0,16,-2.0F, 1.0F, -2.0F, 4, 6, 4, size));
                LeftCalf=new ModelRenderer(source,0, 22);
                LeftCalf.mirror=true;
                LeftCalf.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, size);
                LeftCalf.setRotationPoint(0.0F, 8.0F, -2.0F);
                target.addChild(LeftCalf);
                try {
                    TexturedQuad[] quadList = (TexturedQuad[]) ReflectionTool.getField(LeftCalf.cubeList.get(0),"quadList");
                    PositionTextureVertex[] vertexPositions=(PositionTextureVertex[]) ReflectionTool.getField(LeftCalf.cubeList.get(0),"vertexPositions");
                    quadList[3] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[2], vertexPositions[3], vertexPositions[7], vertexPositions[6]}, 8, 16, 8 + 4, 16 + 4, LeftCalf.textureWidth, LeftCalf.textureHeight);
                    quadList[3].flipFace();
                } catch (Exception e) {
                    TkkGameLib.print(miscTool.getError(e));
                }
                break;
        }
    }

    public void synchronousAllData(CustomAction mainModel){
        synchronousBox(mainModel.Head,Head);
        synchronousBox(mainModel.RightLeg,RightLeg);
        synchronousBox(mainModel.LeftLeg,LeftLeg);
        synchronousBox(mainModel.RightArm,RightArm);
        synchronousBox(mainModel.LeftArm,LeftArm);
        synchronousBox(mainModel.LeftForearm,LeftForearm);
        synchronousBox(mainModel.RightForearm,RightForearm);
        synchronousBox(mainModel.LeftCalf,LeftCalf);
        synchronousBox(mainModel.RightCalf,RightCalf);
        synchronousBox(mainModel.body,body);
    }

    public void synchronousBox(ModelRenderer main,ModelRenderer... target){
        for (ModelRenderer model:target){
            model.rotateAngleX=main.rotateAngleX;
            model.rotateAngleY=main.rotateAngleY;
            model.rotateAngleZ=main.rotateAngleZ;

            model.rotationPointX=main.rotationPointX;
            model.rotationPointY=main.rotationPointY;
            model.rotationPointZ=main.rotationPointZ;
        }
    }

}

