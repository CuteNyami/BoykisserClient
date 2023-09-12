package net.luconia.boykisser.ui.component.impl.button;

import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.component.impl.ActionHandler;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class CheckBox extends ToggleButton {

    public CheckBox(float x, float y, float size, ActionHandler handler) {
        super(x, y, size, size, handler);
    }

    public CheckBox(float x, float y, float size) {
        super(x, y, size, size);
    }

    public CheckBox(float x, float y, ActionHandler handler) {
        this(x, y, 10, handler);
    }

    public CheckBox(float x, float y) {
        this(x, y, null);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = isToggled() ? getBackgroundToggledColor().getRGB() : getBackgroundColor().getRGB();
        Renderer.drawRoundedRect(x, y, x + width, y + height, getRadius(), color);
        Renderer.drawRoundedOutline(x, y, x + width, y + height, getRadius(), getOutlineWidth(), getOutlineColor() == null ? color : isToggled() ? getOutlineToggledColor().getRGB() : getOutlineColor().getRGB());

        if (!isToggled()) return;
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2);
        glColor3f(1.f, 1.f, 1.f);
        glEnable(GL_LINE_SMOOTH);

        glBegin(GL_LINES);
        glVertex2f(x + 2.5f, y + 5);
        glVertex2f(x + width / 2 - 0.5f, y + height - 2.5f);

        glVertex2f(x + width / 2 - 0.5f, y + height - 2.5f);
        glVertex2f(x + width - 2.5f, y + 2.5f);
        glEnd();

        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    protected float getOutlineWidth() {
        return 1.f;
    }

    protected float getRadius() {
        return 2.f;
    }

    protected Color getBackgroundToggledColor() {
        return new Color(30, 138, 255);
    }

    protected Color getBackgroundColor() {
        return new Color(217, 217, 217);
    }

    protected Color getOutlineToggledColor() {
        return getOutlineColor();
    }

    protected Color getOutlineColor() {
        return null;
    }

    protected Color getForegroundColor() {
        return Color.WHITE;
    }
}
