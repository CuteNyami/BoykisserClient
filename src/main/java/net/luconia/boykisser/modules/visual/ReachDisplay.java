package net.luconia.boykisser.modules.visual;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.event.EventManager;
import net.luconia.boykisser.event.EventTarget;
import net.luconia.boykisser.event.impl.EntityAttackEvent;
import net.luconia.boykisser.module.Category;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.ModuleRect;
import net.luconia.boykisser.module.Option;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.text.DecimalFormat;

public class ReachDisplay extends Module {

    // using floats instead of doubles causes a crash!
    // java reflections is thinking that the value would be null
    @Option(displayName = "Radius", max = 30)
    public transient double radius = 0;

    @Option(displayName = "Width", max = 100)
    public transient double width = 60;

    @Option(displayName = "Height", max = 50)
    public transient double height = 15.25f;

    @Option(displayName = "Text", placeholder = "type here...")
    public transient String text = "%reach% Blocks";

    private transient double reach = 0;
    private transient long lastAttackTime;

    private transient final DecimalFormat formatter = new DecimalFormat("0.00");

    public ReachDisplay() {
        super("Reach Display", "Displays your current reach", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        EventManager.register(this);
    }

    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }

    @Override
    public void render() {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        Renderer.drawRoundedRect(x, y, (float) width, (float) height, Math.min((float) radius, (float) height / 2), new Color(0, 0, 0, 120));
        String textToRender = text.replace("%reach%", formatter.format(reach));
        fontRenderer.drawCenteredString(textToRender, (int) (x + width / 2), (int) (y + (float) height / 2 - fontRenderer.getHeight(textToRender) / 2 - 1), Color.WHITE);

        if (lastAttackTime != 0 && System.currentTimeMillis() - lastAttackTime >= 5 * 1000) {
            reach = 0;
            lastAttackTime = 0;
        }
    }

    @EventTarget
    public void onEntityAttack(EntityAttackEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit.getEntityId() == event.target.getEntityId()) {
            final Vec3 vec3 = mc.getRenderViewEntity().getPositionEyes(1.0F);
            reach = mc.objectMouseOver.hitVec.distanceTo(vec3);
        }
        lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public ModuleRect getSize() {
        return new ModuleRect((float) width, (float) height);
    }
}
