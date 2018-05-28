package common;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Entity;
import entity.Statement;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.CollectionUtils;

import java.io.*;
import java.util.*;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Utils {
    public static void init(String configPath) {
        String content = read(configPath);
        initAll(JSONObject.fromObject(content));
    }

    public static void initAll(JSONObject json) {
        initField(json, Constants.TABLE_ID_STATEMENT, Integer.class);
        initField(json, Constants.TABLE_ID_INTERACTION, Integer.class);
        initField(json, Constants.TABLE_ID_OPTION, Integer.class);

        initMapField(json, Constants.KEY_STATEMENTS, Statement.class);
        initMapField(json, Constants.KEY_INTERACTIONS, Statement.class);
        initMapField(json, Constants.KEY_OPTIONS, Statement.class);

        initField(json, Constants.KEY_BEGINNING_ID, Integer.class);
        initField(json, Constants.KEY_BEGINNING_CLASS, Class.class);
    }

    public static <T> void initField(JSONObject json, String key, Class<T> clazz) {
        String value = JSONUtils.valueToString(json.get(key));
        Cache.put(key, fromJson(value, clazz));
    }

    public static <T> void initListField(JSONObject json, String key, Class<T> clazz) {
        JSONArray array = json.optJSONArray(key);
        if (!CollectionUtils.isEmpty(array)) {
            List<T> list = new ArrayList<>();
            array.forEach(e -> list.add(fromJson(e.toString(), clazz)));
            Cache.put(key, list);
        }
    }

    public static <T> void initMapField(JSONObject json, String key, Class<T> clazz) {
        String value = JSONUtils.valueToString(json.get(key));
        Map<String, T> map = fromJson(value, Map.class);

        Map<Integer, T> finalMap = new HashMap<>();
        if (map != null) {
            map.forEach((k, v) -> finalMap.put(Integer.parseInt(k), v));
        }
        Cache.put(key, finalMap);
    }

    public static <T extends Entity> void initId(List<T> entities) {
        if (entities != null) {
            entities.forEach(e -> e.setId(generateId(getTableKey(e.getClass()))));
        }
    }

    public static Integer generateId(Class clazz) {
        return generateId(getTableKey(clazz));
    }

    public static Integer generateId(String tableKey) {
        Integer id = Cache.get(tableKey);
        id = id == null ? 0 : id;
        Cache.put(tableKey, ++id);
        return id;
    }

    public static String getTableKey(Class clazz) {
        return "id_" + clazz.getSimpleName().toLowerCase();
    }

    public static String toJson(Object o) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toPrettyJson(Object o) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String read(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\r\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void write(String filePath, String text) {
        try {
            PrintWriter out = new PrintWriter(new File(filePath).getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
