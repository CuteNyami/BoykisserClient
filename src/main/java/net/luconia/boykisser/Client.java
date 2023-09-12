package net.luconia.boykisser;

import net.luconia.boykisser.event.EventManager;
import net.luconia.boykisser.event.EventTarget;
import net.luconia.boykisser.event.impl.RenderEvent;
import net.luconia.boykisser.event.impl.TickEvent;
import net.luconia.boykisser.gui.GuiModuleDrag;
import net.luconia.boykisser.internal.Internal;
import net.luconia.boykisser.keybind.KeyBindManager;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.modules.other.ToggleSprint;
import net.luconia.boykisser.modules.render.ItemPhysics;
import net.luconia.boykisser.modules.render.TNTTimer;
import net.luconia.boykisser.modules.visual.*;
import net.luconia.boykisser.ui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main client class
 * @author Nyami
 */
public class Client {

    private static Client instance;

    private File directory;

    private Config config;

    public FontRenderer fontRenderer;

    private final List<Module> modules = new ArrayList<>();

    private static final List<Long> lmbClicks = new ArrayList<>();
    private static final List<Long> rmbClicks = new ArrayList<>();
    private static boolean lmbWasPressed;
    private static boolean rmbWasPressed;

    @Internal("Called on Minecraft#startGame()")
    public static void run() {
        if (instance != null) {
            throw new IllegalStateException("Client already initialized!");
        }

        new Client().start();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void start() {
        instance = this;
        directory = new File(Minecraft.getMinecraft().mcDataDir, "boykisser-client");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        fontRenderer = new FontRenderer("fonts/product_sans.ttf", 20.f);
        config = new Config();

        modules.add(new FPSCounter());
        modules.add(new CPSCounter());
        modules.add(new PingDisplay());
        modules.add(new ToggleSprint());
        modules.add(new ItemPhysics());
        modules.add(new TNTTimer());
        modules.add(new TimeDisplay());
        modules.add(new ServerDisplay());
        modules.add(new ReachDisplay());
        modules.add(new MemoryDisplay());
        modules.add(new KeyStrokes());

        for (Module module : modules) {
            Module savedMod = config.getMods().get(module.getName());
            if (savedMod != null) {
                module.setEnabled(savedMod.isEnabled(), false);

                if (module.isEnabled())
                    module.onEnable();

                module.setOptions(savedMod.getOptions());
                module.setX(savedMod.getX());
                module.setY(savedMod.getY());
            }
            module.load();
        }

        KeyBindManager.registerKeyBind("module_drag", new KeyBinding("Open the module drag gui", Keyboard.KEY_RSHIFT, "Boykisser Client"));
        EventManager.register(this);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (KeyBindManager.isPressed("module_drag")) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiModuleDrag());
        }
    }

    @EventTarget
    public void onRender(RenderEvent ignored) {
        final boolean lmbPressed = Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();

        if(lmbPressed != lmbWasPressed) {
            long lmbLastPressed = System.currentTimeMillis();
            lmbWasPressed = lmbPressed;
            if(lmbPressed) {
                lmbClicks.add(lmbLastPressed);
            }
        }

        final boolean rmbPressed = Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();

        if(rmbPressed != rmbWasPressed) {
            long rmbLastPressed = System.currentTimeMillis();
            rmbWasPressed = rmbPressed;
            if(rmbPressed) {
                rmbClicks.add(rmbLastPressed);
            }
        }
    }

    public static int getLMB() {
        final long time = System.currentTimeMillis();
        lmbClicks.removeIf(aLong -> aLong + 1000 < time);
        return lmbClicks.size();
    }
    public static int getRMB() {
        final long time = System.currentTimeMillis();
        rmbClicks.removeIf(aLong -> aLong + 1000 < time);
        return rmbClicks.size();
    }

    public static Client getInstance() {
        return instance;
    }

    public File getDirectory() {
        return directory;
    }

    public List<Module> getModules() {
        return modules;
    }

    public Config getConfig() {
        return config;
    }
}
