package net.luconia.boykisser.ui.component.impl;

import lombok.Getter;
import net.luconia.boykisser.ui.Rectangle;
import net.luconia.boykisser.ui.component.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Scroll list
 * <br>
 * Handles component scrolling and adds a scrollbar
 * @author Nyami
 */
public class ScrollListComponent extends Component {

    protected final List<Component> components = new ArrayList<>();

    @Getter
    protected ScrollbarComponent scrollbar;

    public ScrollListComponent(float x, float y, float width, float height, ScrollbarComponent scrollbar) {
        super(x, y, width, height);
        this.scrollbar = scrollbar;
        this.scrollbar.setX(this.getScrollbarPos().getX());
        this.scrollbar.setY(this.getScrollbarPos().getY());
        this.scrollbar.shaftSize = height;
        this.scrollbar.scrollSize = new Rectangle(width, height);
        this.scrollbar.type = ScrollbarComponent.ScrollbarType.HORIZONTAL;
    }

    public ScrollListComponent(float x, float y, float width, float height) {
        this(x, y, width, height, new ScrollbarComponent(10));
    }

    @Override
    public void init() {
        components.forEach(Component::init);
    }

    public void add(Component component) {
        components.add(component);
        scrollbar.content += component.getHeight() + getSpacing();
        scrollbar.components += 1;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        float compY = y;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor(x, y, width, height);

        for (Component component : components) {
            if (component.getX() == 0) {
                component.setX(this.x);
            }

            component.setY(compY - scrollbar.getScroll());

            component.drawComponent(mouseX, mouseY, partialTicks);
            compY += component.getHeight() + getSpacing();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        float content = scrollbar.content;
        scrollbar.content -= getSpacing();

        if (scrollbar.content > height)
            scrollbar.drawComponent(mouseX, mouseY, partialTicks);

        scrollbar.content = content;
    }

    protected void glScissor(float x, float y, float width, float height) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        x = x * res.getScaleFactor();
        height = height * res.getScaleFactor();
        y = Minecraft.getMinecraft().displayHeight - (y * res.getScaleFactor()) - height;
        width = width * res.getScaleFactor();
        GL11.glScissor((int) x - 1, (int) y - 2, (int) width + 2, (int) height + 4);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        components.forEach(component -> component.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        scrollbar.mouseClicked(mouseX, mouseY, mouseButton);
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        scrollbar.mouseReleased(mouseX, mouseY, state);
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        components.forEach(component -> component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public float getSpacing() {
        return 10.f;
    }

    public Rectangle getScrollbarPos() {
        return new Rectangle(x + width - scrollbar.size, y);
    }

    public List<Component> getComponents() {
        return components;
    }
}