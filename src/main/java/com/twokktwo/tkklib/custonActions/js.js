var TkkGameLib=Java.type("com.twokktwo.tkklib.TkkGameLib")
var NBTTagCompound=Java.type("net.minecraft.nbt.NBTTagCompound")
var EntityEquipmentSlot=Java.type("net.minecraft.inventory.EntityEquipmentSlot")
var ActionTool=Java.type("com.twokktwo.tkklib.custonActions.tool.ActionTool")
var EventLoader=Java.type("com.twokktwo.tkklib.event.EventLoader")
var CustomAction=Java.type("com.twokktwo.tkklib.custonActions.Model.CustomAction")
//动作初始化
var testPoses=new ActionTool.ActionDataSet()
testPoses.setRotationAngle(ActionTool.actionType.RightForearm, -0.7418, 0.0, 0.0);
testPoses.setRotationAngle(ActionTool.actionType.RightArm, -0.1309, -0.3491, 0.3054);

function getEntityTexture(self,render,entity){
	return Java.type("com.twokktwo.tkklib.custonActions.Render.ActionRender").TEXTURE
}

function LayerActionArmorBaseDoRenderLayer(self,layer,entitylivingbaseIn,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale){
	layer.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
	layer.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
	layer.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
	layer.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
}

function LayerHeldItemDoRenderLayer(self,layer,entitylivingbaseIn,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale){
	layer.renderItem(entitylivingbaseIn,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale);
}

function CustomActionRender(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor){
	//默认动作
	model.setRotationAngles(limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn)
	//渲染
	model.Head.render(scaleFactor);
	model.RightArm.render(scaleFactor);
	model.LeftArm.render(scaleFactor);
	model.LeftLeg.render(scaleFactor);
	model.RightLeg.render(scaleFactor);
	model.body.render(scaleFactor);
}
function CustomActionSetRotationAngles(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor){
	//渲染默认动作时触发该函数
	//绘制自定义动作
	doAction(self.getTempDataString(),self.getTempDataInt(),self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor)
}

//绘制自定义动作
function doAction(String_type,Int_Tick,self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor){
	var amount;
	var nbt=self.getNBT()
	var startActions;
	var nowTick=EventLoader.clientTime;
	var renderTick=nowTick+model.partialTick;
	//进行初始化，起始动作，设置起始时间
	if(Int_Tick==0){
		startActions=initStartAction(self,model,true)
		self.setTempDataInt(nowTick)
		Int_Tick=self.getTempDataInt()
	}else{
		startActions=initStartAction(self,model,false)
	}
	switch(String_type){
		case "test":
			//自定义的挥动手臂例子
			amount=40;
			if(Int_Tick+amount < nowTick){
				defaultAction(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor)
				return
			}
			model.runningAction(CustomAction.actionType.HEAD_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			model.runningAction(CustomAction.actionType.LEG_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			model.runningAction(CustomAction.actionType.RIDING,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			model.runningAction(CustomAction.actionType.LEFT_ARM_POSE,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			model.runningAction(CustomAction.actionType.SNEAK,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			model.runningAction(CustomAction.actionType.BOW_AND_ARROW,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
			defaultAction(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor)
			testPoses.tweenModel(Int_Tick,amount,model,startActions,-0.04,renderTick)
			//model.RightForearm.rotateAngleX = -0.7418;
			//model.RightForearm.rotateAngleY = 0;
			//model.RightForearm.rotateAngleZ = 0;
			break;
		default:
			TkkGameLib.print("default:"+String_type)
			defaultAction(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor)
			return;
	}
}
function defaultAction(self,model,entitylivingbaseIn,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor){
	model.runningAction(CustomAction.actionType.INIT,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.HEAD_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.ARM_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.LEG_ROTATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.RIDING,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.LEFT_ARM_POSE,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.RIGHT_ARM_POSE,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.SWING_ARM,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.SNEAK,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.NORMAL_OSCILLATION,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	model.runningAction(CustomAction.actionType.BOW_AND_ARROW,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scaleFactor,entitylivingbaseIn);
	
}
function initStartAction(self,model,boolean_init){
	if(boolean_init){
		startActions=new ActionTool.ActionDataSet(model);
		startActions.writeNBT(self.getNBT())
	}else{
		startActions=new ActionTool.ActionDataSet()
		startActions.readNBT(self.getNBT())
	}
	return startActions;
	
}








