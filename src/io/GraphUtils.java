package io;

import common.Cache;
import common.Constants;
import dao.BaseDao;
import entity.Entity;
import entity.Interaction;
import entity.Option;
import entity.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhou Guanliang
 * @since 2018/7/7
 */
public class GraphUtils {
    public static void graph() {
        Integer beginningId = Cache.get(Constants.KEY_BEGINNING_ID);
        Class beginningClazz = Cache.get(Constants.KEY_BEGINNING_CLASS);

        Entity beginning = BaseDao.get(beginningId, beginningClazz);

        List<List<Entity>> tree = new ArrayList<>();
        List<Entity> root = new ArrayList<>();
        root.add(beginning);
        scan(root, tree);

        int maxWidth = 0, markIndex = -1;
        List<Entity> maxLevel = new ArrayList<>();
        for (int i = 0; i < tree.size(); i++) {
            if (tree.get(i).size() > maxWidth) {
                maxWidth = tree.get(i).size();
                markIndex = i;
                maxLevel = tree.get(i);
            }
        }

        for (int i = 0; i < markIndex - 1; i++) {
            int printIndex = 0;
            for (Entity scaleItem : maxLevel) {
                if (printIndex >= tree.get(i).size()) {
                    print(null);
                } else {
                    if (BaseDao.isAncestor(tree.get(i).get(printIndex), scaleItem)) {
                        print(tree.get(i).get(printIndex++));
                    } else {
                        print(null);
                    }
                }
            }
        }
        maxLevel.forEach(GraphUtils::print);
        for (int i = markIndex; i < tree.size(); i++) {
            int printIndex = 0;
            for (Entity scaleItem : maxLevel) {
                if (printIndex >= tree.get(i).size()) {
                    print(null);
                } else {
                    if (BaseDao.isAncestor(scaleItem, tree.get(i).get(printIndex))) {
                        print(tree.get(i).get(printIndex++));
                    } else {
                        print(null);
                    }
                }
            }
        }
    }

    private static void print(Entity entity) {
        //TODO
    }

    private static void scan(List<Entity> level, List<List<Entity>> tree) {
        tree.add(level);
        List<Entity> nextLevel = new ArrayList<>();
        level.forEach(entity -> nextLevel.addAll(BaseDao.getNexts(entity)));
        if (nextLevel.size() == 0) {
            return;
        }
        scan(nextLevel, tree);
    }

    private static String convertEntity(Entity entity) {
        String className = entity.getClass().getSimpleName();
        String desc = className.substring(0, 1) + entity.getId();
        if (entity instanceof Statement) {
            desc += " " + cutText(((Statement) entity).getText());
        } else if (entity instanceof Interaction) {
            desc += " " + cutText(((Interaction) entity).getPreface());
        } else if (entity instanceof Option) {
            desc += " " + cutText(((Option) entity).getText());
        }
        return desc;
    }

    private static String cutText(String text) {
        if (text.length() > 2) {
            text = text.substring(0, 2);
        }
        for (int i = 0; i < 2 - text.length(); i++) {
            text += " ";
        }
        return text + "...";
    }
}
