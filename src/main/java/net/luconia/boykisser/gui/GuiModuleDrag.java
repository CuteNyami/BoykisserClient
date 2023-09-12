package net.luconia.boykisser.gui;

import lombok.SneakyThrows;
import net.luconia.boykisser.Client;
import net.luconia.boykisser.access.AccessEntityRenderer;
import net.luconia.boykisser.mixin.DevTweaker;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.ui.component.ComponentScreen;
import net.luconia.boykisser.ui.component.impl.button.ButtonComponent;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

public class GuiModuleDrag extends ComponentScreen {

    private Optional<Module> currentMod = Optional.empty();

    private int prevX;
    private int prevY;

    @Override
    @SneakyThrows
    public void initGui() {
        Method method = mc.entityRenderer.getClass().getDeclaredMethod(DevTweaker.devEnv ? "loadShader" : "a", ResourceLocation.class);
        method.setAccessible(true);
        method.invoke(mc.entityRenderer, new ResourceLocation("shaders/post/menu_blur.json"));

        this.components.clear();
        this.components.add(new ButtonComponent("Mod Menu", this.width / 2.f - 50.f, this.height / 2.f - 15.f, 100, 30, component -> this.mc.displayGuiScreen(new GuiModMenu())) {
            @Override
            protected Color getTextColor() {
                return Color.WHITE;
            }

            @Override
            protected Color getColor() {
                return new Color(31, 138, 255);
            }

            @Override
            protected Color getHoverColor() {
                return new Color(29, 121, 222);
            }
        });
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        currentMod.ifPresent(module -> {
            module.setX(mouseX + module.getX() - prevX);
            module.setY(mouseY + module.getY() - prevY);
            prevX = mouseX;
            prevY = mouseY;
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        prevX = mouseX;
        prevY = mouseY;

        currentMod = Client.getInstance().getModules()
                .stream()
                .filter(m -> m.isEnabled()
                                && mouseX >= m.getX()
                                && mouseX <= m.getX() + m.getSize().getX()
                                && mouseY >= m.getY()
                                && mouseY <= m.getY() + m.getSize().getY())
                .findFirst();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        currentMod = Optional.empty();
        prevX = 0;
        prevY = 0;

    }

    @Override
    @SneakyThrows
    public void onGuiClosed() {
        Client.getInstance().getConfig().saveMods();

        Method method = mc.entityRenderer.getClass().getDeclaredMethod(DevTweaker.devEnv ? "stopUseShader" : "b");
        method.setAccessible(true);
        method.invoke(mc.entityRenderer);
    }
}
