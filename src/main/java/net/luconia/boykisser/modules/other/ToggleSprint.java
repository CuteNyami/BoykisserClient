package net.luconia.boykisser.modules.other;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.event.EventManager;
import net.luconia.boykisser.event.EventTarget;
import net.luconia.boykisser.event.impl.EventUpdate;
import net.luconia.boykisser.gui.GuiModuleDrag;
import net.luconia.boykisser.module.Category;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.ModuleRect;
import net.luconia.boykisser.module.Option;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

import java.awt.*;

public class ToggleSprint extends Module {

    private transient final Minecraft mc = Minecraft.getMinecraft();
    private transient boolean sprintingToggled = false;

    private transient String text = "[Sprinting (Toggled)]";

    @Option(displayName = "Radius", max = 30)
    public transient double radius = 0;

    @Option(displayName = "Width", max = 150)
    public transient double width = 100;

    @Option(displayName = "Height", max = 50)
    public transient double height = 15.25f;

    public ToggleSprint() {
        super("Toggle Sprint", Category.OTHER);
    }

    @Override
    public void onEnable() {
        EventManager.register(this);
    }

    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        float f = 0.8F;

        if(mc.gameSettings.keyBindSprint.isPressed()) {
            if(mc.thePlayer.isSprinting() && !sprintingToggled) sprintingToggled = true;
            else if(!mc.thePlayer.isSprinting()) sprintingToggled = !sprintingToggled;
        }

        boolean flags = !mc.thePlayer.movementInput.sneak &&
                (mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying) &&
                !mc.thePlayer.isPotionActive(Potion.blindness) &&
                mc.thePlayer.movementInput.moveForward >= f &&
                !mc.thePlayer.isSprinting() &&
                !mc.thePlayer.isUsingItem() &&
                !mc.thePlayer.isCollidedHorizontally &&
                !mc.gameSettings.keyBindBack.isKeyDown() &&
                sprintingToggled;

        if (flags) { mc.thePlayer.setSprinting(true); }

        if(mc.thePlayer.isSprinting()) text = ("[Sprinting " + (sprintingToggled ? "(Toggled)]" : "(Vanilla)]"));
    }

    @Override
    public void render() {
        if (sprintingToggled || mc.thePlayer.isSprinting() || mc.currentScreen instanceof GuiModuleDrag) {
            FontRenderer fontRenderer = Client.getInstance().fontRenderer;

            Renderer.drawRoundedRect(x, y, (float) width, (float) height, Math.min((float) radius, (float) height / 2), new Color(0, 0, 0, 120));
            fontRenderer.drawCenteredString(text, (int) (x + width / 2), (int) (y + (float) height / 2 - fontRenderer.getHeight(text) / 2 - 1), Color.WHITE);
        }
    }

    @Override
    public ModuleRect getSize() {
        return new ModuleRect((float) width, (float) height);
    }
}
