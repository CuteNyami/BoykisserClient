package net.luconia.boykisser.modules.visual;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.module.Category;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.ModuleRect;
import net.luconia.boykisser.module.Option;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerDisplay extends Module {

    // using floats instead of doubles causes a crash!
    // java reflections is thinking that the value would be null
    @Option(displayName = "Radius", max = 30)
    public transient double radius = 0;

    @Option(displayName = "Height", max = 50)
    public transient double height = 15.25f;

    @Option(displayName = "Text", placeholder = "type here...")
    public transient String text = "%ip%";

    public ServerDisplay() {
        super("Ip Display", "Displays the ip of the current server", Category.VISUAL);
    }

    @Override
    public void render() {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;
        String textToRender = text.replace("%ip%", getServerIp());
        float width = fontRenderer.getWidth(textToRender) + 5;

        Renderer.drawRoundedRect(x, y, width, (float) height, Math.min((float) radius, (float) height / 2), new Color(0, 0, 0, 120));
        fontRenderer.drawCenteredString(textToRender, (int) (x + width / 2), (int) (y + (float) height / 2 - fontRenderer.getHeight(textToRender) / 2 - 1), Color.WHITE);
    }

    public String getServerIp() {
        return Minecraft.getMinecraft().getCurrentServerData() == null ? "unknown" : Minecraft.getMinecraft().getCurrentServerData().serverIP;
    }

    @Override
    public ModuleRect getSize() {
        String textToRender = text.replace("%ip%", getServerIp());
        float width = Client.getInstance().fontRenderer.getWidth(textToRender) + 5;
        return new ModuleRect(width, (float) height);
    }
}
