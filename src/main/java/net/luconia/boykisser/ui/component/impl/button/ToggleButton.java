package net.luconia.boykisser.ui.component.impl.button;

import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.component.impl.ActionHandler;

import java.awt.*;

public class ToggleButton extends InvisibleButton {

    private boolean toggled;

    public ToggleButton(float x, float y, float width, float height, ActionHandler handler) {
        super(x, y, width, height, handler);
    }

    public ToggleButton(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public ToggleButton(float x, float y, ActionHandler handler) {
        super(x, y, 20, 11, handler);
    }

    public ToggleButton(float x, float y) {
        super(x, y, 20, 11);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Renderer.drawRoundedRect(x, y, width, height, height / 2, isToggled() ? new Color(30, 138, 255) : new Color(217, 217, 217));
        Renderer.drawRoundedRect(isToggled() ? x + width - (height - 2.f) - 1 : x + 1, y + 1.f, height - 2.f, height - 2.f, (height - 2.f) / 2, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            toggled = !toggled;
            if (handler != null) {
                handler.handle(this);
            }
        }
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }
}
