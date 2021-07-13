package com.zto.testcase.util;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap工具类,精简代码
 * <p>Description: HashMap工具类,精简代码</p>
 */
public class MapBuilder<K, V> {

    private Map<K, V> map = new HashMap<K, V>(8);

    private boolean isBuild = false;


    public MapBuilder() {
    }

    public MapBuilder(K key, V value) {
        map.put(key, value);
    }

    /**
     * 创建key类型为K, value类型为V的map
     */
    public static <K, V> MapBuilder<K, V> make(K key, V value) {
        MapBuilder<K, V> builder = new MapBuilder<K, V>(key, value);
        return builder;
    }

    /**
     * 创建key类型为K, value类型为Object的map
     */
    public static <K, V> MapBuilder<K, Object> makeO(K key, Object value) {
        MapBuilder<K, Object> builder = new MapBuilder<K, Object>(key, value);
        return builder;
    }

    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public V get(K key) {
        return map.get(key);
    }

    public Map<K, V> build(K key, V value) {
        checkBuild();
        map.put(key, value);
        return map;
    }

    public Map<K, V> build() {
        checkBuild();
        return map;
    }

    private void checkBuild() {
        if (isBuild) {
            throw new IllegalStateException("build方法只能调用一次!");
        } else {
            isBuild = true;
        }
    }

}
