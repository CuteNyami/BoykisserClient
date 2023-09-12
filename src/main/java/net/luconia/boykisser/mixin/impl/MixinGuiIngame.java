package net.luconia.boykisser.mixin.impl;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.event.impl.RenderEvent;
import net.luconia.boykisser.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Shadow @Final public Minecraft mc;

    private final RenderEvent event = new RenderEvent();

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        if (/*!(mc.currentScreen instanceof GuiModuleDrag) && */!mc.gameSettings.showDebugInfo) {
            event.partialTicks = partialTicks;
            event.call();

            for (Module module : Client.getInstance().getModules()) {
                if (module.isEnabled()) {
                    module.render();
                }
            }
        }
    }

}
