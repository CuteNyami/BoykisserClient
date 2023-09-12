package net.luconia.boykisser.mixin.impl;

import net.luconia.boykisser.event.impl.EntityAttackEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    private EntityAttackEvent entityAttackEvent;

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, CallbackInfo ci) {
        if (targetEntity.canAttackWithItem()) {
            entityAttackEvent = new EntityAttackEvent(targetEntity);
            entityAttackEvent.call();
        }
    }

}