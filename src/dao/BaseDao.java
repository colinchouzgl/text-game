package dao;

import common.Cache;
import common.LinkHeadUtils;
import common.Utils;
import entity.Entity;
import entity.LinkTail;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class BaseDao {
    public static <T> T get(int id, Class<T> clazz) {
        Map<Integer, T> entityMap = Cache.get(getCacheKey(clazz));
        if (entityMap == null) {
            return null;
        }
        return entityMap.get(id);
    }

    public static <T extends Entity> void insert(T entity) {
        int id = Utils.generateId(entity.getClass());
        entity.setId(id);
        Map<Integer, T> entityMap = Cache.get(getCacheKey(entity.getClass()));
        if (entityMap == null) {
            entityMap = new HashMap<>();
        }
        entityMap.put(id, entity);
        Cache.put(getCacheKey(entity.getClass()), entityMap);
    }

    public static <T extends Entity> boolean update(T entity) {
        Map<Integer, T> entityMap = Cache.get(getCacheKey(entity.getClass()));
        if (entityMap == null || entityMap.get(entity.getId()) == null) {
            return false;
        }
        entityMap.put(entity.getId(), entity);
        Cache.put(getCacheKey(entity.getClass()), entityMap);
        return true;
    }

    public static <T> boolean remove(int id, Class<T> clazz) {
        Map<Integer, T> entityMap = Cache.get(getCacheKey(clazz));
        if (entityMap == null || entityMap.get(id) == null) {
            return false;
        }

        T entity = entityMap.get(id);

        entityMap.remove(id);
        Cache.put(getCacheKey(clazz), entityMap);

        if (entity instanceof LinkTail) {
            LinkTail linkTail = (LinkTail) entity;
            if (linkTail.getNextId() > 0) {
                Object next = get(linkTail.getNextId(), linkTail.getNextClazz());
                if (next != null) {
                    if (LinkHeadUtils.isLinkHead(next.getClass())) {
                        LinkHeadUtils.setPreId(next, 0);
                        LinkHeadUtils.setPreClazz(next, null);
                    }
                }
            }
        }

        if (LinkHeadUtils.isLinkHead(clazz)) {
            if (LinkHeadUtils.getPreId(entity) > 0) {
                Object pre = get(LinkHeadUtils.getPreId(entity), LinkHeadUtils.getPreClazz(entity));
                if (pre != null) {
                    if (pre instanceof LinkTail) {
                        ((LinkTail) pre).setNextId(0);
                        ((LinkTail) pre).setNextClazz(null);
                    }
                }
            }
        }
        return true;
    }

    private static String getCacheKey(Class clazz) {
        return clazz.getSimpleName().toLowerCase() + "s";
    }
}
