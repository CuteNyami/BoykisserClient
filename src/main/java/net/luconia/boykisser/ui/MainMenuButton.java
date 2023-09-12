package net.luconia.boykisser.ui;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.render.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
 * Simple main menu button using GuiButton
 * @author Nyami
 */
public class MainMenuButton extends GuiButton {

    public MainMenuButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public MainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        Color color = hovered ? new Color(145, 158, 255, 120) : new Color(50, 50, 50, 120);
        Color hoverColor = hovered ? new Color(161, 172, 255, 120) : new Color(60, 60, 60, 120);

        Renderer.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 5.f, color.getRGB());
        Renderer.drawRoundedOutline(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 5.f, 1.f, hoverColor.getRGB());

        this.mouseDragged(mc, mouseX, mouseY);
        fontRenderer.drawCenteredString(this.displayString, (int) this.xPosition + this.width / 2, (int) this.yPosition + (this.height - (8 + 15 - 12)) / 2, hovered ? new Color(190, 190, 190) : Color.WHITE);
    }
}
