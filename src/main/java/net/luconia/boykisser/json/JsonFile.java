package net.luconia.boykisser.json;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class JsonFile implements IDocument<JsonDocument> {

    private final Gson gson;

    private IDocument<JsonDocument> document;

    private final File file;

    public JsonFile(Gson gson, String path) {
        this.gson = gson;
        this.document = new JsonDocument(gson);
        this.file = new File(path);
    }

    public JsonFile(String path) {
        this(new Gson(), path);
    }

    public boolean exists() {
        return file.exists();
    }

    public void create() {
        try {
            if (file.getParentFile() != null) Files.createDirectories(file.getParentFile().toPath());
            Files.createFile(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonDocument append(String key, Object value) {
        return document.append(key, value);
    }

    @Override
    public JsonDocument depend(String key) {
        return document.depend(key);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return document.get(key, clazz);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        return document.getList(key, clazz);
    }

    @Override
    public <T1, T2> Map<T1, T2> getMap(String key, Class<T1> clazz1, Class<T2> clazz2) {
        return document.getMap(key, clazz1, clazz2);
    }

    public void load() {
        try {
            final BufferedReader reader = Files.newBufferedReader(file.toPath());
            final StringBuilder builder = new StringBuilder();
            while (reader.ready()) builder.append(reader.readLine());
            reader.close();
            this.document = new JsonDocument(gson, builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writer = Files.newBufferedWriter(file.toPath());
            writer.write(document.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return document.toString();
    }
}