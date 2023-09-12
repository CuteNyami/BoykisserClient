package net.luconia.installer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Installer {

    public static File directory = new File(OSHelper.getOS().getMc(), File.separator + "versions" + File.separator + "Boykisser-Client");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    private final Config config;

    public Installer() {
        this.config = new Config();
    }

    public void install() {
        config.createJson();

        String mc = OSHelper.getOS().getMc();
        String installDate = sdf.format(new Date());

        JsonObject profile = new JsonObject();
        profile.addProperty("name", "Boykisser Client");
        profile.addProperty("type", "custom");
        profile.addProperty("created", installDate);
        profile.addProperty("lastUsed", installDate);
        profile.addProperty("icon", "Grass");
        profile.addProperty("lastVersionId", "Boykisser-Client");

        File launcherProfileFile = new File(mc, File.separator + "launcher_profiles.json");
        JsonObject json = new JsonObject();

        if (launcherProfileFile.exists()) {
            json = JsonParser.parseString(readFile(launcherProfileFile)).getAsJsonObject();
        } else {
            json.add("profiles", new JsonObject());
        }

        json.get("profiles").getAsJsonObject().add("Boykisser-Client", profile);
        json.addProperty("selectedProfile", "Boykisser-Client");

        String jsonToWrite = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create().toJson(json);
        write(launcherProfileFile, jsonToWrite);

        File file = new File(OSHelper.getOS().getMc() + File.separator + "versions" + File.separator + "1.8.9" + File.separator + "1.8.9.jar");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "You need to have 1.8.9 installed!", "An error occurred", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Files.copy(file.toPath(), new File(directory, "Boykisser-Client.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        String mc = OSHelper.getOS().getMc();
        File launcherProfileFile = new File(mc, File.separator + "launcher_profiles.json");
        JsonObject json;

        if (directory.exists())
            deleteDir(directory);

        File lib = new File(mc, "libraries" + File.separator + "net" + File.separator + "luconia" + File.separator + "Boykisser-Client");
        if (lib.exists()) {
            deleteDir(lib);
        }

        if (launcherProfileFile.exists()) {
            json = JsonParser.parseString(readFile(launcherProfileFile)).getAsJsonObject();

            if (json.get("profiles").getAsJsonObject().get("Boykisser-Client") != null) {
                json.get("profiles").getAsJsonObject().remove("Boykisser-Client");
            }

            if (json.get("selectedProfile") != null) {
                json.remove("selectedProfile");
            }

            String jsonToWrite = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create().toJson(json);
            write(launcherProfileFile, jsonToWrite);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteDir(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children != null) {
                for (String child : children) {
                    deleteDir(new File(file, child));
                }
            }
        }
        file.delete();
    }

    private void write(File file, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
            String str;
            while ((str = buffer.readLine()) != null)
                builder.append(str).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public boolean exists() {
        return config.exists();
    }

}
