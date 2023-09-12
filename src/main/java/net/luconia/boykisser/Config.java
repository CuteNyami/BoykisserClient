package net.luconia.boykisser;

import com.google.gson.GsonBuilder;
import net.luconia.boykisser.json.JsonFile;
import net.luconia.boykisser.module.Module;

import java.util.HashMap;
import java.util.Map;

public class Config extends JsonFile {

    private final Map<String, Module> mods;

    public Config() {
        super(new GsonBuilder().setPrettyPrinting().serializeNulls().create(), Client.getInstance().getDirectory().getPath() + "/config.json");
        if (!exists()) {
            create();
            append("mods", new HashMap<>());
            save();
        }
        load();
        this.mods = getMap("mods", String.class, Module.class);
    }

    public void saveMods() {
        this.mods.clear();
        for (Module module : Client.getInstance().getModules()) {
            this.mods.put(module.getName(), module);
        }
        this.append("mods", this.mods);
        this.save();
    }

    public Map<String, Module> getMods() {
        return mods;
    }
}
