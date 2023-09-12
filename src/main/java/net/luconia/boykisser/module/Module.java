package net.luconia.boykisser.module;

import lombok.SneakyThrows;
import net.luconia.boykisser.internal.Internal;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Module class
 * <h3>Usage of settings</h3>
 * <br>
 * You have to add
 * <br>
 *
 * @author Nyami
 */
public class Module {

    /**
     * The display name of the module
     * <br>
     * Used to show the name of a mod in the mod menu
     */
    protected final transient String name;

    /**
     * The description of a module
     * <br>
     * Used to tell the user more about a module
     */
    protected final transient String description;

    protected boolean enabled;

    protected float x;
    protected float y;

    protected Map<String, Object> options = new HashMap<>();

    protected final transient boolean visual;

    public final Category category;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.visual = category == Category.VISUAL;
    }

    public Module(String name, Category category) {
        this.name = name;
        this.visual = category == Category.VISUAL;
        this.category = category;
        this.description = "";
    }

    /**
     * Loads module settings and other data
     */
    @SneakyThrows
    @Internal("Called in the clients start function")
    public void load() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Option.class)) {
                Option option = field.getAnnotation(Option.class);
                if (!option.displayName().isEmpty()) {
                    if (!options.containsKey(field.getName()))
                        options.put(field.getName(), field.get(this));
                    this.setSetting(field.getName(), options.get(field.getName()));
                }
            }
        }
    }

    /**
     * Set the value of an option field
     * <br>
     * Used by the mod menu/module settings
     * @param setting the name of the option
     * @param value the value that you want to set it
     */
    public void setSetting(String setting, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(setting);
            field.setAccessible(true);

            if (value != null)
                field.set(this, value);
            options.remove(field.getName());
            options.put(field.getName(), field.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LogManager.getLogger().error("Failed to set the value", e);
        }
    }

    /**
     * Called when a mod is getting enabled
     * <br>
     * use it to listen to events
     */
    public void onEnable() {}
    public void onDisable() {}

    public void render() {}
    public ModuleRect getSize() {
        return new ModuleRect(0, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisual() {
        return visual;
    }

    public void setEnabled(boolean enabled, boolean callEnable) {
        this.enabled = enabled;
        if (!callEnable) return;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setEnabled(boolean enabled) {
        this.setEnabled(enabled, true);
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
