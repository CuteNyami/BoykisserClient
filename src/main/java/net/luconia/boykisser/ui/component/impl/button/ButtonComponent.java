package net.luconia.boykisser.ui.component.impl.button;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.luconia.boykisser.ui.component.impl.ActionHandler;

import java.awt.*;

public class ButtonComponent extends InvisibleButton {

    private final String text;

    public ButtonComponent(String text, float x, float y, float width, float height, ActionHandler handler) {
        super(x, y, width, height, handler);
        this.text = text;
    }

    public ButtonComponent(String text, float x, float y, float width, float height) {
        super(x, y, width, height, null);
        this.text = text;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        Color color = isHovered(mouseX, mouseY) ? getHoverColor() : getColor();
        Color outlineColor = getOutlineColor() == null ? color : isHovered(mouseX, mouseY) ? getOutlineHoverColor() : getOutlineColor();

        Renderer.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, getRadius(), color.getRGB());
        Renderer.drawRoundedOutline(this.x, this.y, this.x + this.width, this.y + this.height, getRadius(), getOutlineWidth(), outlineColor.getRGB());
        fontRenderer.drawCenteredString(this.text, (int) this.x + this.width / 2, (int) this.y + (this.height - (8 + 15 - 12)) / 2, isHovered(mouseX, mouseY) ? getTextHoverColor() : getTextColor());
    }

    protected float getRadius() {
        return 5.f;
    }

    protected float getOutlineWidth() {
        return 1.f;
    }

    protected Color getColor() {
        return new Color(200, 200, 200);
    }

    protected Color getHoverColor() {
        return new Color(176, 176, 176);
    }

    protected Color getTextColor() {
        return Color.black;
    }

    protected Color getTextHoverColor() {
        return getTextColor();
    }

    protected Color getOutlineColor() {
        return null;
    }

    protected Color getOutlineHoverColor() {
        return getOutlineColor();
    }
}
