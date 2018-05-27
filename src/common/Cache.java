package common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Cache {
    private static Map<String, Object> data = new HashMap<>();

    public static <T> T get(String key) {
        Object value = data.get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    public static void put(String key, Object value) {
        data.put(key, value);
    }

    public static Map<String, Object> getData() {
        return data;
    }
}
