package com.dynious.blex.model;// Date: 25-12-2013 16:32:19
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBuffer extends ModelBase
{
    // fields
    ModelRenderer OutsideGlass;
    ModelRenderer Pilar1;
    ModelRenderer Pilar2;
    ModelRenderer Pilar3;
    ModelRenderer Pilar4;
    ModelRenderer Pilar5;
    ModelRenderer Pilar10;
    ModelRenderer Pilar7;
    ModelRenderer Pilar8;
    ModelRenderer Pilar6;
    ModelRenderer Pilar9;
    ModelRenderer Pilar12;
    ModelRenderer Pilar11;
    private float size = 0.0625F;

    public ModelBuffer()
    {
        textureWidth = 128;
        textureHeight = 128;

        OutsideGlass = new ModelRenderer(this, 64, 32);
        OutsideGlass.addBox(0F, 0F, 0F, 16, 16, 16);
        OutsideGlass.setRotationPoint(-8F, 8F, -8F);
        OutsideGlass.setTextureSize(128, 128);
        setRotation(OutsideGlass, 0F, 0F, 0F);
        
        Pilar1 = new ModelRenderer(this, 0, 0);
        Pilar1.addBox(0F, 0F, 0F, 2, 16, 2);
        Pilar1.setRotationPoint(-8F, 8F, -8F);
        Pilar1.setTextureSize(64, 32);
        Pilar1.mirror = true;
        setRotation(Pilar1, 0F, 0F, 0F);
        Pilar2 = new ModelRenderer(this, 0, 0);
        Pilar2.addBox(0F, 0F, 0F, 2, 16, 2);
        Pilar2.setRotationPoint(-8F, 8F, 6F);
        Pilar2.setTextureSize(64, 32);
        Pilar2.mirror = true;
        setRotation(Pilar2, 0F, 0F, 0F);
        Pilar3 = new ModelRenderer(this, 0, 0);
        Pilar3.addBox(0F, 0F, 0F, 2, 16, 2);
        Pilar3.setRotationPoint(6F, 8F, 6F);
        Pilar3.setTextureSize(64, 32);
        Pilar3.mirror = true;
        setRotation(Pilar3, 0F, 0F, 0F);
        Pilar4 = new ModelRenderer(this, 0, 0);
        Pilar4.addBox(0F, 0F, 0F, 2, 16, 2);
        Pilar4.setRotationPoint(6F, 8F, -8F);
        Pilar4.setTextureSize(64, 32);
        Pilar4.mirror = true;
        setRotation(Pilar4, 0F, 0F, 0F);
        Pilar5 = new ModelRenderer(this, 0, 0);
        Pilar5.addBox(0F, 0F, 0F, 12, 2, 2);
        Pilar5.setRotationPoint(-6F, 8F, -8F);
        Pilar5.setTextureSize(64, 32);
        Pilar5.mirror = true;
        setRotation(Pilar5, 0F, 0F, 0F);
        Pilar10 = new ModelRenderer(this, 0, 0);
        Pilar10.addBox(0F, 0F, 0F, 12, 2, 2);
        Pilar10.setRotationPoint(-6F, 22F, -8F);
        Pilar10.setTextureSize(64, 32);
        Pilar10.mirror = true;
        setRotation(Pilar10, 0F, 0F, 0F);
        Pilar7 = new ModelRenderer(this, 0, 0);
        Pilar7.addBox(0F, 0F, 0F, 2, 2, 12);
        Pilar7.setRotationPoint(-8F, 8F, -6F);
        Pilar7.setTextureSize(64, 32);
        Pilar7.mirror = true;
        setRotation(Pilar7, 0F, 0F, 0F);
        Pilar8 = new ModelRenderer(this, 0, 0);
        Pilar8.addBox(0F, 0F, 0F, 2, 2, 12);
        Pilar8.setRotationPoint(6F, 8F, -6F);
        Pilar8.setTextureSize(64, 32);
        Pilar8.mirror = true;
        setRotation(Pilar8, 0F, 0F, 0F);
        Pilar6 = new ModelRenderer(this, 0, 0);
        Pilar6.addBox(0F, 0F, 0F, 12, 2, 2);
        Pilar6.setRotationPoint(-6F, 8F, 6F);
        Pilar6.setTextureSize(64, 32);
        Pilar6.mirror = true;
        setRotation(Pilar6, 0F, 0F, 0F);
        Pilar9 = new ModelRenderer(this, 0, 0);
        Pilar9.addBox(0F, 0F, 0F, 12, 2, 2);
        Pilar9.setRotationPoint(-6F, 22F, 6F);
        Pilar9.setTextureSize(64, 32);
        Pilar9.mirror = true;
        setRotation(Pilar9, 0F, 0F, 0F);
        Pilar12 = new ModelRenderer(this, 0, 0);
        Pilar12.addBox(0F, 0F, 0F, 2, 2, 12);
        Pilar12.setRotationPoint(-8F, 22F, -6F);
        Pilar12.setTextureSize(64, 32);
        Pilar12.mirror = true;
        setRotation(Pilar12, 0F, 0F, 0F);
        Pilar11 = new ModelRenderer(this, 0, 0);
        Pilar11.addBox(0F, 0F, 0F, 2, 2, 12);
        Pilar11.setRotationPoint(6F, 22F, -6F);
        Pilar11.setTextureSize(64, 32);
        Pilar11.mirror = true;
        setRotation(Pilar11, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Pilar1.render(f5);
        Pilar2.render(f5);
        Pilar3.render(f5);
        Pilar4.render(f5);
        Pilar5.render(f5);
        Pilar6.render(f5);
        Pilar7.render(f5);
        Pilar8.render(f5);
        Pilar9.render(f5);
        Pilar10.render(f5);
        Pilar11.render(f5);
        Pilar12.render(f5);
        OutsideGlass.render(f5);
    }

    public void renderPilars()
    {
        Pilar1.render(size);
        Pilar2.render(size);
        Pilar3.render(size);
        Pilar4.render(size);
        Pilar5.render(size);
        Pilar6.render(size);
        Pilar7.render(size);
        Pilar8.render(size);
        Pilar9.render(size);
        Pilar10.render(size);
        Pilar11.render(size);
        Pilar12.render(size);
    }

    public void renderSides()
    {
        OutsideGlass.render(size);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
