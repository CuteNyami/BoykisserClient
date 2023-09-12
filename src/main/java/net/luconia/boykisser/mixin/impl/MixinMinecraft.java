package net.luconia.boykisser.mixin.impl;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.Delta;
import net.luconia.boykisser.event.impl.TickEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    private long lastFrame = getTime();

    private final TickEvent event = new TickEvent();

    @Inject(method = "startGame", at = @At("RETURN"))
    public void startGame(CallbackInfo ci) {
        Client.run();
    }

    @Inject(method = "runTick", at = @At("TAIL"))
    public void runTick(CallbackInfo ci) {
        event.call();
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void delta(CallbackInfo ci) {
        long currentTime = getTime();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;
        Delta.DELTA_TIME = deltaTime;
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
