package com.twokktwo.tkklib.gui;

import com.twokktwo.tkklib.TkkGameLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class testClientGui extends GuiScreen {
    private ItemStack item;
    public double xSize;
    public double zSize;

    public testClientGui(){

    }
    public void initGui(){
        this.item=new ItemStack(Item.getItemById(14));
    }
    public void onGuiClosed(){

    }
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc,width,height);
    }
    protected void keyTyped (char typedChar, int keyCode) throws IOException {
        //按键事件,typedChar为输入值,keyCode为按键代码
        super.keyTyped(typedChar,keyCode);
        TkkGameLib.print("keyTyped:"+typedChar+" , "+keyCode);
    }

    public void drawScreen(int mx, int my, float par3){
        //每帧触发一次,渲染画面，mx my为指针坐标,
        //this.drawDefaultBackground();
        this.updateXYSize();
        this.drawDefaultBackground();
        this.drawItemStack(this.item,(int) (400.0*this.xSize),(int) (100.0*this.zSize),"1");
        this.drawGradientRect(mx-1, my-1, mx+1, my+1, -1072689136, -804253680);
        super.drawScreen(mx,my,par3);
    }

    public void updateScreen(){
        //每tick触发一次
        super.updateScreen();
    }
    protected void mouseClicked(int mx, int my, int par3){
        //super.mouseClicked(mx,my,par3);
    }
    protected void actionPerformed(GuiButton button) throws IOException{
        super.actionPerformed(button);
    }
    public boolean doesGuiPauseGame(){
        //游戏是否暂停
        return false;
    }
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - 0, altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }
    private void updateXYSize(){
        this.xSize=this.width/1000.0;
        this.zSize=this.height/1000.0;
    }

}
