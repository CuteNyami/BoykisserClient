package net.luconia.boykisser.ui.component.impl;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.luconia.boykisser.ui.component.Component;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class SliderComponent extends Component {

    private String text;
    private float value;
    private float val;
    private final float min, max;

    private boolean down;

    private static FontRenderer smallFont = null;

    private float defaultWidth;

    private final ActionHandler handler;

    public SliderComponent(String text, float x, float y, float value, float min, float max, ActionHandler handler) {
        super(x, y, 100, 15);
        this.text = text;
        this.value = value;
        this.min = min;
        this.max = max;
        this.defaultWidth = width;
        this.handler = handler;

        if (smallFont == null) {
            smallFont = new FontRenderer("fonts/product_sans.ttf", 16.f);
        }
    }

    public SliderComponent(String text, float x, float y, float value, float min, float max) {
        this(text, x, y, value, min, max, null);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        this.width = defaultWidth;

        float trackWidth = this.width;
        float trackHeight = 2.f;

        float trackX = this.text.isEmpty() ? this.x : this.x + fontRenderer.getWidth(text) + 2;
        float trackY = this.y + this.height / 2 - trackHeight / 2;

        boolean trackHovered = mouseX >= trackX && mouseY >= trackY && mouseX < trackX + trackWidth && mouseY < trackY + trackHeight;

        if (trackHovered) {
            down = true;
        } else if (!Mouse.isButtonDown(0)) {
            down = false;
        }

        if (Mouse.isButtonDown(0) && down) {
            val = (mouseX - trackX) / this.width;

            if (val < 0.f) val = 0.f;
            if (val > 1.f) val = 1.f;

            value = val * (max - min) + min;
            if (handler != null) {
                handler.handle(this);
            }
        }

        val = (value - min) / (max - min);

        String valText = String.format("%.2f", value);
        //width = trackWidth + 2 + smallFont.getWidth(valText);

        if (!text.isEmpty()) {
            //width += 2 + fontRenderer.getWidth(text);
            fontRenderer.drawString(text, x, y + height / 2 - 7, getTextColor());
        }

        smallFont.drawString(valText, trackX + trackWidth + 2, y + height / 2 - 5, getTextColor());
        Gui.drawRect((int) trackX, (int) trackY, (int) (trackX + trackWidth), (int) (trackY + trackHeight), getTrackColor().getRGB());
        Renderer.drawRoundedRect(trackX + (val * (trackWidth - 6)), y + height / 2 - 6 / 2.f, (trackX + (val * (trackWidth - 6))) + 6, (y + height / 2 - 6 / 2.f) + 6, 6, getThumbColor().getRGB());
        Renderer.drawRoundedOutline(trackX + (val * (trackWidth - 6)), y + height / 2 - 6 / 2.f, (trackX + (val * (trackWidth - 6))) + 6, (y + height / 2 - 6 / 2.f) + 6, 6, getOutlineWidth(), getThumbOutlineColor() != null ? getThumbOutlineColor().getRGB() : getThumbColor().getRGB());
    }

    protected float getOutlineWidth() {
        return 1.f;
    }

    protected Color getTextColor() {
        return Color.WHITE;
    }

    protected Color getTrackColor() {
        return new Color(30, 138, 255);
    }

    protected Color getThumbColor() {
        return Color.WHITE;
    }

    protected Color getThumbOutlineColor() {
        return new Color(231, 231, 231);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
