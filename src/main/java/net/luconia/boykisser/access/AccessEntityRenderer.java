package net.luconia.boykisser.access;

import net.minecraft.util.ResourceLocation;

public interface AccessEntityRenderer {

    void loadShader(ResourceLocation resourceLocationIn);

    void stopUseShader();

}