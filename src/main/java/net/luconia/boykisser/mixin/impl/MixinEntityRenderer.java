package net.luconia.boykisser.mixin.impl;

import net.luconia.boykisser.access.AccessEntityRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements AccessEntityRenderer {

    @Override
    @Invoker("loadShader")
    public abstract void loadShader(ResourceLocation resourceLocationIn);

    @Override
    @Invoker("stopUseShader")
    public abstract void stopUseShader();

}
