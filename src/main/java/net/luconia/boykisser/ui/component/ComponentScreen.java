package net.luconia.boykisser.ui.component;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentScreen extends GuiScreen {

    protected List<Component> components = new ArrayList<>();

    @Override
    public void initGui() {
        components.forEach(Component::init);
    }

    @Override
    public void onGuiClosed() {
        components.forEach(Component::close);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        components.forEach(component -> component.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        components.forEach(component -> component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
}
