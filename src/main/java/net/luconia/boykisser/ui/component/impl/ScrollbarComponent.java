package net.luconia.boykisser.ui.component.impl;


import net.luconia.boykisser.ui.Rectangle;
import net.luconia.boykisser.ui.component.Component;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * Only horizontal scrolling supported
 * <br>
 * TODO: vertical scrolling
 * @author Nyami
 */
public class ScrollbarComponent extends Component {

    public float size;
    protected float shaftSize;

    public float content;

    protected ScrollbarType type;
    protected Rectangle scrollSize;

    protected float thumbSize;
    protected float location;

    protected float cursor;

    protected int prevY;
    protected boolean scrolling;

    public int components;

    public ScrollbarComponent(float x, float y, float size, float shaftSize, float contentSize, Rectangle scrollSize, ScrollbarType type) {
        super(x, y, size, size);
        this.size = size;
        this.shaftSize = shaftSize;
        this.content = contentSize;
        this.scrollSize = scrollSize;
        this.type = type;
    }

    public ScrollbarComponent(float x, float y, float size, float shaftSize, float contentSize, Rectangle scrollSize) {
        super(x, y, size, size);
        this.size = size;
        this.shaftSize = shaftSize;
        this.content = contentSize;
        this.scrollSize = scrollSize;
        this.type = ScrollbarType.HORIZONTAL;
    }

    public ScrollbarComponent(float size) {
        super(0, 0, size, size);
        this.size = size;
        this.shaftSize = 0;
        this.content = 0;
        this.scrollSize = null;
        this.type = ScrollbarType.HORIZONTAL;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (type == ScrollbarType.HORIZONTAL) {
            if (this.location == 0) this.location = y;

            this.thumbSize = (scrollSize.getY() / content) * shaftSize;
            this.cursor = content * ((location - y) / shaftSize);

            boolean hovered = mouseX >= this.x && mouseY >= location && mouseX < this.x + size && mouseY < location + this.thumbSize;

            if (Mouse.isButtonDown(0) && (hovered || scrolling)) {
                location = mouseY + location - prevY;
                prevY = mouseY;
                scrolling = true;
            } else {
                scrolling = false;
            }

            int wheel = Mouse.getDWheel();

            if (wheel < 0) {
                location += (content / components) / 10;
            } else if (wheel > 0) {
                location -= (content / components) / 10;
            }

            if ((location - y) + thumbSize > shaftSize) {
                location = shaftSize - thumbSize + y;
            }

            if (location - y < 0) {
                location = y;
            }

            draw(mouseX, mouseY, partialTicks);
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (type == ScrollbarType.HORIZONTAL) {
            Gui.drawRect((int) x, (int) y, (int) (x + size), (int) (y + shaftSize), Color.BLACK.getRGB());
            Gui.drawRect((int) x, (int) (location), (int) (x + size), (int) (location + thumbSize), -1);
        } else {
            Gui.drawRect((int) x, (int) y, (int) (x + shaftSize), (int) (y + size), Color.BLACK.getRGB());
            //TODO vertical scrolling
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        prevY = mouseY;
        if (type == ScrollbarType.HORIZONTAL) {
            boolean thumbHovered = mouseX >= this.x && mouseY >= location && mouseX < this.x + size && mouseY < location + this.thumbSize;
            if ((mouseX >= this.x && mouseY >= this.y && mouseX < this.x + size && mouseY < this.y + this.shaftSize) && !thumbHovered && mouseButton == 0) {
                location = mouseY - thumbSize / 2;

                if ((location - y) + thumbSize > shaftSize) {
                    location = shaftSize - thumbSize + y;
                }

                if (location - y < 0) {
                    location = y;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        prevY = 0;
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Returns the scroll progress
     * <br>
     * use pos - scroll
     * @return the scroll progress
     */
    public float getScroll() {
        return cursor;
    }

    public enum ScrollbarType {
        VERTICAL,
        HORIZONTAL
    }

}
