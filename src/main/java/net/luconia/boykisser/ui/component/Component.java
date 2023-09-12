package net.luconia.boykisser.ui.component;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;

public abstract class Component {

    @Setter
    @Getter
    protected float x, y;

    @Setter
    @Getter
    protected float width, height;

    public Component(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Called in {@link GuiScreen#initGui()}
     */
    public void init() {}

    /**
     * Called in {@link GuiScreen#onGuiClosed()}
     */
    public void close() {}

    public abstract void drawComponent(int mouseX, int mouseY, float partialTicks);

    public void keyTyped(char typedChar, int keyCode) {}
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int state) {}
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {}

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }
}
