package dao;

import common.Cache;
import common.Constants;
import common.LinkHeadUtils;
import common.Utils;
import entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class BaseDao {
    public static <T extends Entity> T get(int id, Class<T> clazz) {
        Map<Integer, T> entityMap = Cache.get(getCacheKey(clazz));
        if (entityMap == null) {
            return null;
        }
        return entityMap.get(id);
    }

    public static <T extends Entity> T insert(T entity) {
        int id = Utils.generateId(entity.getClass());
        entity.setId(id);
        Map<Integer, T> entityMap = Cache.get(getCacheKey(entity.getClass()));
        if (entityMap == null) {
            entityMap = new HashMap<>();
        }
        entityMap.put(id, entity);
        Cache.put(getCacheKey(entity.getClass()), entityMap);
        return entity;
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

    public static <T extends Entity> boolean remove(int id, Class<T> clazz) {
        Map<Integer, T> entityMap = Cache.get(getCacheKey(clazz));
        if (entityMap == null || entityMap.get(id) == null) {
            return false;
        }

        T entity = entityMap.get(id);

        entityMap.remove(id);
        Cache.put(getCacheKey(clazz), entityMap);

//        if (entity instanceof LinkTail) {
//            LinkTail linkTail = (LinkTail) entity;
//            if (linkTail.getNextId() > 0) {
//                Object next = get(linkTail.getNextId(), linkTail.getNextClazz());
//                if (next != null) {
//                    if (LinkHeadUtils.isLinkHead(next.getClass())) {
//                        LinkHeadUtils.setPreId(next, 0);
//                        LinkHeadUtils.setPreClazz(next, null);
//                    }
//                }
//            }
//        }

        if (LinkHeadUtils.isLinkHead(clazz)) {
//            if (LinkHeadUtils.getPreId(entity) > 0) {
//                Object pre = get(LinkHeadUtils.getPreId(entity), LinkHeadUtils.getPreClazz(entity));
//                if (pre != null) {
//                    if (pre instanceof LinkTail) {
//                        ((LinkTail) pre).setNextId(0);
//                        ((LinkTail) pre).setNextClazz(null);
//                    }
//                }
//            }

            List<Entity> preTails = getPreTails(entity);
            preTails.forEach(linkTail -> {
                if (linkTail instanceof LinkTail) {
                    ((LinkTail) linkTail).setNextId(0);
                    ((LinkTail) linkTail).setNextClazz(null);
                }
            });
        }
        return true;
    }

    public static <T extends Entity> List<Entity> getNexts(T entity) {
        List<Entity> nexts = new ArrayList<>();
        if (entity instanceof LinkTail) {
            int nextId = ((LinkTail) entity).getNextId();
            Class nextClazz = ((LinkTail) entity).getNextClazz();
            if (nextId == 0) {
                return nexts;
            }
            Object nextNode = get(nextId, nextClazz);
            nexts.add((Entity) nextNode);
        } else if (entity instanceof Interaction) {
            List<Integer> optionIds = ((Interaction) entity).getOptionIds();
            if (optionIds == null || optionIds.size() == 0) {
                return nexts;
            }
            optionIds.forEach(optionId -> nexts.add(get(optionId, Option.class)));
        }
        return nexts;
    }

    public static <T extends Entity> List<Entity> getPreTails(T entity) {
        List<Entity> pres = new ArrayList<>();
        if (LinkHeadUtils.isLinkHead(entity.getClass())) {
            Map<Integer, Statement> statementMap = Cache.get(Constants.KEY_STATEMENTS);
            Map<Integer, Option> optionMap = Cache.get(Constants.KEY_OPTIONS);
            if (statementMap != null) {
                statementMap.forEach((statementId, statement) -> {
                    if (statement.getNextId() == entity.getId()) {
                        pres.add(statement);
                    }
                });
            }
            if (optionMap != null) {
                optionMap.forEach((optionId, option) -> {
                    if (option.getNextId() == entity.getId()) {
                        pres.add(option);
                    }
                });
            }
        }
        return pres;
    }

    public static List<Option> getOptionsByInteraction(int interactionId) {
        List<Option> options = new ArrayList<>();
        Interaction interaction = get(interactionId, Interaction.class);
        if (interaction == null) {
            return options;
        }
        List<Integer> optionIds = interaction.getOptionIds();
        optionIds.forEach(optionId -> {
            Option option = get(optionId, Option.class);
            if (option != null) {
                options.add(option);
            }
        });
        return options;
    }

    public static boolean isAncestor(Entity e1, Entity e2) {
        List<Entity> nexts = getNexts(e1);
        for (Entity next : nexts) {
            if (next.seems(e2)) {
                return true;
            }
        }
        for (Entity next : nexts) {
            boolean found = isAncestor(next, e2);
            if (found) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBeginning() {
        Integer beginningId = Cache.get(Constants.KEY_BEGINNING_ID);
        return beginningId != null;
    }

    public static List<Entity> getUnfinished() {
        List<Entity> unfinished = new ArrayList<>();
        Map<Integer, Statement> statementMap = Cache.get(Constants.KEY_STATEMENTS);
        Map<Integer, Interaction> interactionMap = Cache.get(Constants.KEY_INTERACTIONS);
        Map<Integer, Option> optionMap = Cache.get(Constants.KEY_OPTIONS);

        if (statementMap != null) {
            statementMap.keySet().forEach(statementId -> {
                Statement statement = get(statementId, Statement.class);
                if (statement.getNextId() == 0) {
                    unfinished.add(statement);
                }
            });
        }

        if (interactionMap != null) {
            interactionMap.keySet().forEach(interactionId -> {
                Interaction interaction = get(interactionId, Interaction.class);
                if (interaction.getOptionIds().size() < interaction.getOptionSize()) {
                    unfinished.add(interaction);
                }
            });
        }

        if (optionMap != null) {
            optionMap.keySet().forEach(optionId -> {
                Option option = get(optionId, Option.class);
                if (option.getNextId() == 0) {
                    unfinished.add(option);
                }
            });
        }

        return unfinished;
    }

    private static String getCacheKey(Class clazz) {
        return clazz.getSimpleName().toLowerCase() + "s";
    }
}
