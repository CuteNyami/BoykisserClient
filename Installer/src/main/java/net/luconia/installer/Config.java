package net.luconia.installer;

import com.google.gson.GsonBuilder;
import net.luconia.installer.json.JsonFile;

import java.io.File;
import java.util.Arrays;

public class Config extends JsonFile {

    public Config() {
        super(new GsonBuilder().setPrettyPrinting().serializeNulls().create(), OSHelper.getOS().getMc() + File.separator + "versions" + File.separator + "Boykisser-Client" + File.separator + "Boykisser-Client.json");
    }

    public void createJson() {
        if (!exists()) {
            create();
        }
        append("id", "Boykisser-Client");
        append("inheritsFrom", "1.8.9");
        append("time", "2023-09-11T17:18:35+02:00");
        append("releaseTime", "2023-09-11T17:18:35+02:00");
        append("type", "release");
        append("libraries", Arrays.asList(
                new Lib("optifine:OptiFine:1.8.9_HD_U_M5", "https://luconia.net/repositories/"),
                new Lib("net.luconia:Boykisser-Client:1.8.9", "https://luconia.net/repositories/"),
                new Lib("optifine:launchwrapper-of:2.2", "https://luconia.net/repositories/"),
                new Lib("com.google.code.gson:gson:2.10.1", "https://repo1.maven.org/maven2/")
        ));
        append("mainClass", "net.minecraft.launchwrapper.Launch");
        append("minecraftArguments", "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userProperties ${user_properties} --userType ${user_type} --tweakClass optifine.OptiFineTweaker --tweakClass net.luconia.boykisser.mixin.Tweaker");
        save();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteJson() {
        if (exists()) {
            this.getFile().delete();
        }
    }

    private static class Lib {
        private final String name;
        private final String url;

        private Lib(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
