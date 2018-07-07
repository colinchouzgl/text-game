package common;

import entity.Interaction;
import entity.Statement;

/**
 * @author Zhou Guanliang
 * @since 2018/5/28
 */
public class LinkHeadUtils {
    public static boolean isLinkHead(Class clazz) {
        return clazz == Statement.class || clazz == Interaction.class;
    }

//    public static int getPreId(Object linkHead) {
//        if (linkHead instanceof Statement) {
//            return ((Statement) linkHead).getPreId();
//        }
//        if (linkHead instanceof Interaction) {
//            return ((Interaction) linkHead).getPreId();
//        }
//        return 0;
//    }
//
//    public static Class getPreClazz(Object linkHead) {
//        if (linkHead instanceof Statement) {
//            return ((Statement) linkHead).getPreClazz();
//        }
//        if (linkHead instanceof Interaction) {
//            return ((Interaction) linkHead).getPreClazz();
//        }
//        return null;
//    }
//
//    public static void setPreId(Object linkHead, int id) {
//        if (linkHead instanceof Statement) {
//            ((Statement) linkHead).setPreId(id);
//        }
//        if (linkHead instanceof Interaction) {
//            ((Interaction) linkHead).setPreId(id);
//        }
//    }
//
//    public static void setPreClazz(Object linkHead, Class clazz) {
//        if (linkHead instanceof Statement) {
//            ((Statement) linkHead).setPreClazz(clazz);
//        }
//        if (linkHead instanceof Interaction) {
//            ((Interaction) linkHead).setPreClazz(clazz);
//        }
//    }
}
