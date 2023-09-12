package net.luconia.installer.json;

import java.util.List;
import java.util.Map;

public interface IDocument<D extends IDocument<?>> {
    D append(String key, Object value);
    D depend(String key);
    <T> T get(String key, Class<T> clazz);
    <T> List<T> getList(String key, Class<T> clazz);
    <T1, T2> Map<T1, T2> getMap(String key, Class<T1> clazz1, Class<T2> clazz2);
}