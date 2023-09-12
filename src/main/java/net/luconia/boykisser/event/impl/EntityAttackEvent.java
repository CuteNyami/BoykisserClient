package net.luconia.boykisser.event.impl;

import lombok.AllArgsConstructor;
import net.luconia.boykisser.event.Event;
import net.minecraft.entity.Entity;

@AllArgsConstructor
public class EntityAttackEvent extends Event {
    public final Entity target;
}