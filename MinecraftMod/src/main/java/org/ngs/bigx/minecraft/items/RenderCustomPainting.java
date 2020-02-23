package org.ngs.bigx.minecraft.items;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


@SideOnly(Side.CLIENT)
public class RenderCustomPainting  extends Render
{
    private static final ResourceLocation field_110807_a = new ResourceLocation("customnpcs:textures/painting/custompainting.png");

    // Added the default constructor that matches super.
    protected RenderCustomPainting(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(CustomPainting p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(p_76986_2_, p_76986_4_, p_76986_6_);
        GL11.glRotatef(p_76986_8_, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.bindEntityTexture(p_76986_1_);
        CustomPainting.EnumArt enumart = p_76986_1_.art;
        float f2 = 0.0625F;
        GL11.glScalef(f2, f2, f2);
        this.func_77010_a(p_76986_1_, enumart.sizeX, enumart.sizeY, enumart.offsetX, enumart.offsetY);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }


    protected ResourceLocation getEntityTexture(CustomPainting p_110775_1_)
    {
        return field_110807_a;
    }

    private void func_77010_a(CustomPainting p_77010_1_, int p_77010_2_, int p_77010_3_, int p_77010_4_, int p_77010_5_)
    {
        float f = (float)(-p_77010_2_) / 2.0F;
        float f1 = (float)(-p_77010_3_) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i1 = 0; i1 < p_77010_2_ / 16; ++i1)
        {
            for (int j1 = 0; j1 < p_77010_3_ / 16; ++j1)
            {
                float f15 = f + (float)((i1 + 1) * 16);
                float f16 = f + (float)(i1 * 16);
                float f17 = f1 + (float)((j1 + 1) * 16);
                float f18 = f1 + (float)(j1 * 16);
                this.func_77008_a(p_77010_1_, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float)(p_77010_4_ + p_77010_2_ - i1 * 16) / 512.0F;
                float f20 = (float)(p_77010_4_ + p_77010_2_ - (i1 + 1) * 16) / 512.0F;
                float f21 = (float)(p_77010_5_ + p_77010_3_ - j1 * 16) / 512.0F;
                float f22 = (float)(p_77010_5_ + p_77010_3_ - (j1 + 1) * 16) / 512.0F;
                Tessellator tessellator = Tessellator.getInstance();
                //tessellator.
                BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f20, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f19, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                buffer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f19, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                buffer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f20, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();

                //tessellator.startDrawingQuads();
                //tessellator.setNormal(0.0F, 0.0F, -1.0F);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f20, (double)f21);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f19, (double)f21);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f19, (double)f22);
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f20, (double)f22);
                //tessellator.setNormal(0.0F, 0.0F, 1.0F);
                buffer.pos((double)f15, (double)f17, (double)(f2)).tex((double)f3, (double)f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                buffer.pos((double)f16, (double)f17, (double)(f2)).tex((double)f4, (double)f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(f2)).tex((double)f4, (double)f6).normal(0.0F, 0.0F, 1.0F).endVertex();
                buffer.pos((double)f15, (double)f18, (double)(f2)).tex((double)f3, (double)f6).normal(0.0F, 0.0F, 1.0F).endVertex();
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f3, (double)f5);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f4, (double)f5);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f4, (double)f6);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f3, (double)f6);
                //tessellator.setNormal(0.0F, 1.0F, 0.0F);
                buffer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f7, (double)f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f8, (double)f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f17, (double)(f2)).tex((double)f8, (double)f10).normal(0.0F, 1.0F, 0.0F).endVertex();
                buffer.pos((double)f15, (double)f17, (double)(f2)).tex((double)f7, (double)f10).normal(0.0F, 1.0F, 0.0F).endVertex();
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f7, (double)f9);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f8, (double)f9);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f8, (double)f10);
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f7, (double)f10);
                buffer.pos((double)f15, (double)f18, (double)(f2)).tex((double)f7, (double)f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(f2)).tex((double)f8, (double)f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f8, (double)f10).normal(0.0F, -1.0F, 0.0F).endVertex();
                buffer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f7, (double)f10).normal(0.0F, -1.0F, 0.0F).endVertex();
//                tessellator.setNormal(0.0F, -1.0F, 0.0F);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f7, (double)f9);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f8, (double)f9);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f8, (double)f10);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f7, (double)f10);
                buffer.pos((double)f15, (double)f17, (double)(f2)).tex((double)f12, (double)f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f15, (double)f18, (double)(f2)).tex((double)f12, (double)f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f11, (double)f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f11, (double)f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
//                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)f2, (double)f12, (double)f13);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)f2, (double)f12, (double)f14);
//                tessellator.addVertexWithUV((double)f15, (double)f18, (double)(-f2), (double)f11, (double)f14);
//                tessellator.addVertexWithUV((double)f15, (double)f17, (double)(-f2), (double)f11, (double)f13);
                buffer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f12, (double)f13).normal(1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f12, (double)f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f18, (double)(f2)).tex((double)f11, (double)f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                buffer.pos((double)f16, (double)f17, (double)(f2)).tex((double)f11, (double)f13).normal(1.0F, 0.0F, 0.0F).endVertex();
//                tessellator.setNormal(1.0F, 0.0F, 0.0F);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)(-f2), (double)f12, (double)f13);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)(-f2), (double)f12, (double)f14);
//                tessellator.addVertexWithUV((double)f16, (double)f18, (double)f2, (double)f11, (double)f14);
//                tessellator.addVertexWithUV((double)f16, (double)f17, (double)f2, (double)f11, (double)f13);
                tessellator.draw();
            }
        }
    }

    private void func_77008_a(CustomPainting p_77008_1_, float p_77008_2_, float p_77008_3_)
    {
        int i = MathHelper.floor(p_77008_1_.posX);
        int j = MathHelper.floor(p_77008_1_.posY + (double)(p_77008_3_ / 16.0F));
        int k = MathHelper.floor(p_77008_1_.posZ);

        if (p_77008_1_.facingDirection == EnumFacing.NORTH)
        {
            i = MathHelper.floor(p_77008_1_.posX + (double)(p_77008_2_ / 16.0F));
        }
        else if(p_77008_1_.facingDirection == EnumFacing.UP)
        //if (p_77008_1_.hangingDirection == 1)
        {
            k = MathHelper.floor(p_77008_1_.posZ - (double)(p_77008_2_ / 16.0F));
        }
        else if(p_77008_1_.facingDirection == EnumFacing.DOWN)
        //else if (p_77008_1_.hangingDirection == 0)
        {
            i = MathHelper.floor(p_77008_1_.posX - (double)(p_77008_2_ / 16.0F));
        }
        else if(p_77008_1_.facingDirection == EnumFacing.SOUTH)
        //if (p_77008_1_..hangingDirection == 3)
        {
            k = MathHelper.floor(p_77008_1_.posZ + (double)(p_77008_2_ / 16.0F));
        }
        // Note: EnumSkyBlock.Block == 0;
        //       EnumSkyBlock.SKY = 15;
        int l = this.renderManager.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(i,j,k));
        //int l = this.renderManager.world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((CustomPainting)p_110775_1_);
    }

 
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((CustomPainting)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}