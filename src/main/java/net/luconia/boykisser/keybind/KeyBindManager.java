package net.luconia.boykisser.keybind;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to register new keybindings
 */
public class KeyBindManager {

    private static final Map<String, KeyBinding> keyBindings = new HashMap<>();

    /**
     * Only registers the minecraft key binding without storing it
     * <br>
     * Take a look at {@link KeyBindManager#registerKeyBind(String, KeyBinding)}
     * @param key the key binding
     */
    public static void registerKeyBind(KeyBinding key) {
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, key);
    }

    public static void unregisterKeyBind(KeyBinding key) {
        if (Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings).contains(key)) {
            Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.remove(Minecraft.getMinecraft().gameSettings.keyBindings, Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings).indexOf(key));
        }
    }

    public static void unregisterKeyBind(String name) {
        unregisterKeyBind(getKeyBinding(name));
        keyBindings.remove(name);
    }

    public static void registerKeyBind(String name, KeyBinding key) {
        keyBindings.put(name, key);
        registerKeyBind(key);
    }

    public static boolean isPressed(String name) {
        return getKeyBinding(name).isPressed();
    }

    public static boolean isKeyDown(String name) {
        return getKeyBinding(name).isKeyDown();
    }

    public static KeyBinding getKeyBinding(String name) {
        return keyBindings.get(name);
    }

    public static Map<String, KeyBinding> getKeyBindings() {
        return keyBindings;
    }
}
