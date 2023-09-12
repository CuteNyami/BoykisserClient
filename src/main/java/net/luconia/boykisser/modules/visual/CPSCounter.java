package net.luconia.boykisser.modules.visual;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.module.Category;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.ModuleRect;
import net.luconia.boykisser.module.Option;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;

import java.awt.*;

public class CPSCounter extends Module {

    // using floats instead of doubles causes a crash!
    // java reflections is thinking that the value would be null
    @Option(displayName = "Radius", max = 30)
    public transient double radius = 0;

    @Option(displayName = "Width", max = 100)
    public transient double width = 50;

    @Option(displayName = "Height", max = 50)
    public transient double height = 15.25f;

    @Option(displayName = "Text", placeholder = "type here...")
    public transient String text = "%lmb% | %rmb% CPS";

    public CPSCounter() {
        super("CPS Counter", "Displays your current cps", Category.VISUAL);
    }

    @Override
    public void render() {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        Renderer.drawRoundedRect(x, y, (float) width, (float) height, Math.min((float) radius, (float) height / 2), new Color(0, 0, 0, 120));
        String textToRender = text.replace("%lmb%", String.valueOf(Client.getLMB())).replace("%rmb%", String.valueOf(Client.getRMB()));
        fontRenderer.drawCenteredString(textToRender, (int) (x + width / 2), (int) (y + (float) height / 2 - fontRenderer.getHeight(textToRender) / 2 - 1), Color.WHITE);
    }

    @Override
    public ModuleRect getSize() {
        return new ModuleRect((float) width, (float) height);
    }
}
