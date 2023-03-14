package com.twokktwo.tkklib.custonActions.tool;


import com.twokktwo.tkklib.TkkGameLib;
import com.twokktwo.tkklib.custonActions.Model.CustomAction;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.nbt.NBTTagCompound;

public class ActionTool {
    public static void tweenModel(int startTick, int amountTick, ModelRenderer target,ModelActionData startAction, ModelActionData overAction,float speed,float tick){
        if(startAction==null || overAction==null){return;}
        tweenModel(startTick,amountTick,target,startAction.rotationPointX,startAction.rotationPointY, startAction.rotationPointZ, startAction.rotateAngleX, startAction.rotateAngleY, startAction.rotateAngleZ,overAction.rotationPointX,overAction.rotationPointY, overAction.rotationPointZ, overAction.rotateAngleX, overAction.rotateAngleY, overAction.rotateAngleZ, speed,tick,overAction.doRotationPoint,overAction.doRotateAngle);
    }
    public static void tweenModel(int startTick, int amountTick, ModelRenderer target,float startPointX,float startPointY,float startPointZ,float startAngleX,float startAngleY,float startAngleZ,float overPointX,float overPointY,float overPointZ,float overAngleX,float overAngleY,float overAngleZ,float speed,float tick,boolean doRotationPoint,boolean doRotateAngle){
        //TkkGameLib.print("startTick:"+startTick+",amountTick:"+amountTick+",tick:"+tick);
        if(startTick+amountTick<tick){return;}
        float progress=tick-startTick;
        //TkkGameLib.print("test:"+getRatioFramesSummation(amountTick,speed,progress));
        //float ratioFrames=getRatioFramesArray(amountTick,speed)[Math.max(0,progress-1)];
        float ratioFrames=getRatioFramesSummation(amountTick,speed,progress);
        if(doRotationPoint) {
            target.rotationPointX = startPointX+tweenFloat((float) amountTick, startPointX, overPointX, ratioFrames);
            target.rotationPointY = startPointY+tweenFloat((float) amountTick, startPointY, overPointY, ratioFrames);
            target.rotationPointZ = startPointZ+tweenFloat((float) amountTick, startPointZ, overPointZ, ratioFrames);
        }
        if(doRotateAngle){
            TkkGameLib.print("progress:"+progress+",ratioFrames:"+ratioFrames+",test:"+tweenFloat((float) amountTick,startAngleX,overAngleX,ratioFrames));
            target.rotateAngleX=startAngleX+tweenFloat((float) amountTick,startAngleX,overAngleX,ratioFrames);
            target.rotateAngleY=startAngleY+tweenFloat((float) amountTick,startAngleY,overAngleY,ratioFrames);
            target.rotateAngleZ=startAngleZ+tweenFloat((float) amountTick,startAngleZ,overAngleZ,ratioFrames);
        }
    }

    public static float[] getRatioFramesArray(int amount,float speed){
        float[] array=new float[amount];
        int size=(1+amount)*amount/2-amount;
        float fix=size*speed/amount;
        array[0]=1-fix;
        for(int i=2;i<amount+1;i++){
            array[i-1]=array[0]+(i-1)*speed;
        }
        return array;
    }
    public static float getRatioFramesSummation(int amount,float speed,float nowProgress){
        float first=getRatioFrames(amount,speed,0);
        return (float) (nowProgress*first+nowProgress*(nowProgress-1.0)*speed/2.0);
    }
    public static float getRatioFrames(int amount,float speed,float nowProgress){
        int size=(1+amount)*amount/2-amount;
        float fix=size*speed/amount;
        fix=1-fix;
        return fix+(nowProgress-1)*speed;
    }

    public static float tweenFloat(float amount,float start,float over,float ratioFrame){
        return (over-start)/amount*ratioFrame;
    }

    public static class ModelActionData{
        public float rotationPointX;
        public float rotationPointY;
        public float rotationPointZ;
        public float rotateAngleX;
        public float rotateAngleY;
        public float rotateAngleZ;
        public boolean doRotationPoint=false;
        public boolean doRotateAngle=true;

        public ModelActionData(ModelRenderer model){
            rotateAngleX=model.rotateAngleX;
            rotateAngleY=model.rotateAngleY;
            rotateAngleZ=model.rotateAngleZ;
            rotationPointX=model.rotationPointX;
            rotationPointY=model.rotationPointY;
            rotationPointZ=model.rotationPointZ;
        }
        public ModelActionData(){}
        public ModelActionData(NBTTagCompound nbt){this.readNBT(nbt);}
        public void readNBT(NBTTagCompound nbt){
            rotationPointX=nbt.getFloat("rotationPointX");
            rotationPointY=nbt.getFloat("rotationPointY");
            rotationPointZ=nbt.getFloat("rotationPointZ");
            rotateAngleX=nbt.getFloat("rotateAngleX");
            rotateAngleY=nbt.getFloat("rotateAngleY");
            rotateAngleZ=nbt.getFloat("rotateAngleZ");
            doRotateAngle=nbt.getBoolean("doRotateAngle");
            doRotationPoint= nbt.getBoolean("doRotationPoint");
        }
        public NBTTagCompound writeNBT(NBTTagCompound nbt){
            nbt.setFloat("rotationPointX",rotationPointX);
            nbt.setFloat("rotationPointY",rotationPointY);
            nbt.setFloat("rotationPointZ",rotationPointZ);
            nbt.setFloat("rotateAngleX",rotateAngleX);
            nbt.setFloat("rotateAngleY",rotateAngleY);
            nbt.setFloat("rotateAngleZ",rotateAngleZ);
            nbt.setBoolean("doRotateAngle",doRotateAngle);
            nbt.setBoolean("doRotationPoint",doRotationPoint);
            return nbt;
        }


    }
    public static class ActionDataSet{
        public ModelActionData body;
        public ModelActionData RightLeg;
        public ModelActionData RightCalf;
        public ModelActionData LeftLeg;
        public ModelActionData LeftCalf;
        public ModelActionData LeftArm;
        public ModelActionData LeftForearm;
        public ModelActionData RightArm;
        public ModelActionData RightForearm;
        public ModelActionData Head;
        public ActionDataSet(){}
        public ActionDataSet(CustomAction model){
            body=new ModelActionData(model.body);
            RightLeg=new ModelActionData(model.RightLeg);
            RightCalf=new ModelActionData(model.RightCalf);
            LeftLeg=new ModelActionData(model.LeftLeg);
            LeftCalf=new ModelActionData(model.LeftCalf);
            LeftArm=new ModelActionData(model.LeftArm);
            LeftForearm=new ModelActionData(model.LeftForearm);
            RightArm=new ModelActionData(model.RightArm);
            RightForearm=new ModelActionData(model.RightForearm);
            Head=new ModelActionData(model.Head);
        }
        public void tweenModel(int startTick, int amountTick, CustomAction target, ActionDataSet startAction, float speed, float tick){
            if(body!=null){tweenModel(startTick,amountTick,actionType.body,target.body,startAction,speed,tick);}
            if(RightLeg!=null){tweenModel(startTick,amountTick,actionType.RightLeg,target.RightLeg,startAction,speed,tick);}
            if(RightCalf!=null){tweenModel(startTick,amountTick,actionType.RightCalf,target.RightCalf,startAction,speed,tick);}
            if(LeftLeg!=null){tweenModel(startTick,amountTick,actionType.LeftLeg,target.LeftLeg,startAction,speed,tick);}
            if(LeftCalf!=null){tweenModel(startTick,amountTick,actionType.LeftCalf,target.LeftCalf,startAction,speed,tick);}
            if(LeftArm!=null){tweenModel(startTick,amountTick,actionType.LeftArm,target.LeftArm,startAction,speed,tick);}
            if(LeftForearm!=null){tweenModel(startTick,amountTick,actionType.LeftForearm,target.LeftForearm,startAction,speed,tick);}
            if(RightArm!=null){tweenModel(startTick,amountTick,actionType.RightArm,target.RightArm,startAction,speed,tick);}
            if(RightForearm!=null){tweenModel(startTick,amountTick,actionType.RightForearm,target.RightForearm,startAction,speed,tick);}
            if(Head!=null){tweenModel(startTick,amountTick,actionType.Head,target.Head,startAction,speed,tick);}
        }
        public void tweenModel(int startTick, int amountTick, actionType type, ModelRenderer target, ActionDataSet startAction, float speed, float tick){
            ModelActionData overAction;
            ModelActionData start;
            switch (type){
                case body:
                    overAction=body;
                    start=startAction.body;
                    break;
                case RightLeg:
                    overAction=RightLeg;
                    start=startAction.RightLeg;
                    break;
                case RightCalf:
                    overAction=RightCalf;
                    start=startAction.RightCalf;
                    break;
                case LeftLeg:
                    overAction=LeftLeg;
                    start=startAction.LeftLeg;
                    break;
                case LeftCalf:
                    overAction=LeftCalf;
                    start=startAction.LeftCalf;
                    break;
                case LeftArm:
                    overAction=LeftArm;
                    start=startAction.LeftArm;
                    break;
                case LeftForearm:
                    overAction=LeftForearm;
                    start=startAction.LeftForearm;
                    break;
                case RightArm:
                    overAction=RightArm;
                    start=startAction.RightArm;
                    break;
                case RightForearm:
                    overAction=RightForearm;
                    start=startAction.RightForearm;
                    break;
                case Head:
                    overAction=Head;
                    start=startAction.Head;
                    break;
                default:
                    return;
            }
            ActionTool.tweenModel(startTick,amountTick,target,start,overAction,speed,tick);
        }
        public void readNBT(NBTTagCompound nbt){
            if(nbt.hasKey("body")){body=new ModelActionData(nbt.getCompoundTag("body"));}
            if(nbt.hasKey("RightLeg")){RightLeg=new ModelActionData(nbt.getCompoundTag("RightLeg"));}
            if(nbt.hasKey("RightCalf")){RightCalf=new ModelActionData(nbt.getCompoundTag("RightCalf"));}
            if(nbt.hasKey("LeftLeg")){LeftLeg=new ModelActionData(nbt.getCompoundTag("LeftLeg"));}
            if(nbt.hasKey("LeftCalf")){LeftCalf=new ModelActionData(nbt.getCompoundTag("LeftCalf"));}
            if(nbt.hasKey("LeftArm")){LeftArm=new ModelActionData(nbt.getCompoundTag("LeftArm"));}
            if(nbt.hasKey("LeftForearm")){LeftForearm=new ModelActionData(nbt.getCompoundTag("LeftForearm"));}
            if(nbt.hasKey("RightArm")){RightArm=new ModelActionData(nbt.getCompoundTag("RightArm"));}
            if(nbt.hasKey("RightForearm")){RightForearm=new ModelActionData(nbt.getCompoundTag("RightForearm"));}
            if(nbt.hasKey("Head")){Head=new ModelActionData(nbt.getCompoundTag("Head"));}
        }
        public NBTTagCompound writeNBT(NBTTagCompound nbt){
            if(body!=null){nbt.setTag("body",body.writeNBT(new NBTTagCompound()));}
            if(RightLeg!=null){nbt.setTag("RightLeg",RightLeg.writeNBT(new NBTTagCompound()));}
            if(RightCalf!=null){nbt.setTag("RightCalf",RightCalf.writeNBT(new NBTTagCompound()));}
            if(LeftLeg!=null){nbt.setTag("LeftLeg",LeftLeg.writeNBT(new NBTTagCompound()));}
            if(LeftCalf!=null){nbt.setTag("LeftCalf",LeftCalf.writeNBT(new NBTTagCompound()));}
            if(LeftArm!=null){nbt.setTag("LeftArm",LeftArm.writeNBT(new NBTTagCompound()));}
            if(LeftForearm!=null){nbt.setTag("LeftForearm",LeftForearm.writeNBT(new NBTTagCompound()));}
            if(RightArm!=null){nbt.setTag("RightArm",RightArm.writeNBT(new NBTTagCompound()));}
            if(RightForearm!=null){nbt.setTag("RightForearm",RightForearm.writeNBT(new NBTTagCompound()));}
            if(Head!=null){nbt.setTag("Head",Head.writeNBT(new NBTTagCompound()));}
            return nbt;
        }
        public void setRotationAngle(actionType type, float x, float y, float z){
            switch (type){
                case body:
                    if(body==null){body=new ModelActionData();}
                    body.rotateAngleX=x;
                    body.rotateAngleY=y;
                    body.rotateAngleZ=z;
                    body.doRotateAngle=true;
                    break;
                case RightLeg:
                    if(RightLeg==null){RightLeg=new ModelActionData();}
                    RightLeg.rotateAngleX=x;
                    RightLeg.rotateAngleY=y;
                    RightLeg.rotateAngleZ=z;
                    RightLeg.doRotateAngle=true;
                    break;
                case RightCalf:
                    if(RightCalf==null){RightCalf=new ModelActionData();}
                    RightCalf.rotateAngleX=x;
                    RightCalf.rotateAngleY=y;
                    RightCalf.rotateAngleZ=z;
                    RightCalf.doRotateAngle=true;
                    break;
                case LeftLeg:
                    if(LeftLeg==null){LeftLeg=new ModelActionData();}
                    LeftLeg.rotateAngleX=x;
                    LeftLeg.rotateAngleY=y;
                    LeftLeg.rotateAngleZ=z;
                    LeftLeg.doRotateAngle=true;
                    break;
                case LeftCalf:
                    if(LeftCalf==null){LeftCalf=new ModelActionData();}
                    LeftCalf.rotateAngleX=x;
                    LeftCalf.rotateAngleY=y;
                    LeftCalf.rotateAngleZ=z;
                    LeftCalf.doRotateAngle=true;
                    break;
                case LeftArm:
                    if(LeftArm==null){LeftArm=new ModelActionData();}
                    LeftArm.rotateAngleX=x;
                    LeftArm.rotateAngleY=y;
                    LeftArm.rotateAngleZ=z;
                    LeftArm.doRotateAngle=true;
                    break;
                case LeftForearm:
                    if(LeftForearm==null){LeftForearm=new ModelActionData();}
                    LeftForearm.rotateAngleX=x;
                    LeftForearm.rotateAngleY=y;
                    LeftForearm.rotateAngleZ=z;
                    LeftForearm.doRotateAngle=true;
                    break;
                case RightArm:
                    if(RightArm==null){RightArm=new ModelActionData();}
                    RightArm.rotateAngleX=x;
                    RightArm.rotateAngleY=y;
                    RightArm.rotateAngleZ=z;
                    RightArm.doRotateAngle=true;
                    break;
                case RightForearm:
                    if(RightForearm==null){RightForearm=new ModelActionData();}
                    RightForearm.rotateAngleX=x;
                    RightForearm.rotateAngleY=y;
                    RightForearm.rotateAngleZ=z;
                    RightForearm.doRotateAngle=true;
                    break;
                case Head:
                    if(Head==null){Head=new ModelActionData();}
                    Head.rotateAngleX=x;
                    Head.rotateAngleY=y;
                    Head.rotateAngleZ=z;
                    Head.doRotateAngle=true;
                    break;
            }

        }
    }
    public enum actionType{
        body,RightLeg,RightCalf,LeftLeg,LeftCalf,LeftArm,LeftForearm,RightArm,RightForearm,Head

    }
}
