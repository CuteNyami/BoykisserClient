package net.luconia.installer;

import imgui.*;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main extends Application {

    private final ImBoolean open = new ImBoolean(true);

    private Installer installer;

    private String status = "Idling...";

    private boolean attributeSet = false;

    @Override
    protected void configure(Configuration config) {
        config.setWidth(1);
        config.setHeight(1);
    }

    @Override
    protected void initImGui(Configuration config) {
        super.initImGui(config);

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        this.installer = new Installer();
    }

    private void alignForWidth(float width, float alignment) {
        float avail = ImGui.getContentRegionAvail().x;
        float off = (avail - width) * alignment;
        if (off > 0.0f)
            ImGui.setCursorPosX(ImGui.getCursorPosX() + off);
    }

    private void alignForWidth(float width) {
        alignForWidth(width, 0.5f);
    }

    @Override
    public void process() {
        if (!open.get()) {
            GLFW.glfwSetWindowShouldClose(getHandle(), true);
        }

        final GLFWVidMode vidmode = Objects.requireNonNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()));

        ImGui.setNextWindowPos((float) vidmode.width() / 2 - 150, (float) vidmode.height() / 2 - 65, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(300, 130);
        ImGui.begin("Boykisser Client Installer", open, ImGuiWindowFlags.NoResize);

        ImGui.text("Install:");
        ImGui.dummy(0, 10);

        ImGuiStyle style = ImGui.getStyle();
        float width = 0.0f;
        width += ImGui.calcTextSize("Install").x + 5;
        width += style.getItemSpacing().x;
        width += ImGui.calcTextSize("Delete").x + 5;
        width += style.getItemSpacing().x;
        width += ImGui.calcTextSize("Reinstall!").x + 5;
        alignForWidth(width);

        ImGui.beginDisabled(installer.exists());
        if (ImGui.button("Install")) {
            status = "Installing...";
            installer.install();
            status = "Installed";
        }
        ImGui.endDisabled();

        ImGui.sameLine();
        ImGui.beginDisabled(!installer.exists());

        if (ImGui.button("Delete")) {
            status = "Deleting...";
            installer.delete();
            status = "Deleted";
        }

        ImGui.sameLine();

        if (ImGui.button("Reinstall")) {
            status = "Deleting...";
            installer.delete();
            status = "Installing...";
            installer.install();
            status = "Installed";
        }

        ImGui.endDisabled();

        ImGui.dummy(0, 20);
        ImGui.separator();
        ImGui.text(status);

        if (ImGui.getWindowViewport() != null && ImGui.getWindowViewport().getPlatformHandle() != 0 && !attributeSet) {
            GLFW.glfwSetWindowAttrib(ImGui.getWindowViewport().getPlatformHandle(), GLFW.GLFW_FLOATING, GLFW.GLFW_TRUE);
            attributeSet = true;
        }

        ImGui.end();
    }

    private static byte[] loadFromResources(String name) {
        try {
            return Files.readAllBytes(Paths.get(Objects.requireNonNull(Main.class.getClassLoader().getResource(name)).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        launch(new Main());
    }
}