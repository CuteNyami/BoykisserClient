package net.luconia.boykisser.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class JsonDocument implements IDocument<JsonDocument> {

    private final Gson gson;

    private final JsonObject object;

    public JsonDocument(Gson gson, JsonObject object) {
        this.gson = gson;
        this.object = object;
    }

    public JsonDocument(JsonObject object) {
        this(new Gson(), object);
    }

    public JsonDocument(String jsonString) {
        this.gson = new Gson();
        this.object = gson.fromJson(jsonString, JsonObject.class);
    }

    public JsonDocument(Gson gson, String jsonString) {
        this(gson, gson.fromJson(jsonString, JsonObject.class));
    }

    public JsonDocument(Gson gson) {
        this(gson, new JsonObject());
    }

    public JsonDocument() {
        this(new Gson(), new JsonObject());
    }

    @Override
    public JsonDocument append(String key, Object value) {
        object.add(key, gson.toJsonTree(value));
        return this;
    }

    @Override
    public JsonDocument depend(String key) {
        object.remove(key);
        return this;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return gson.fromJson(object.get(key), clazz);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        return gson.fromJson(object.get(key), TypeToken.getParameterized(List.class, clazz).getType());
    }

    @Override
    public <T1, T2> Map<T1, T2> getMap(String key, Class<T1> clazz1, Class<T2> clazz2) {
        return gson.fromJson(object.get(key), TypeToken.getParameterized(Map.class, clazz1, clazz2).getType());
    }

    @Override
    public String toString() {
        return gson.toJson(object);
    }
}