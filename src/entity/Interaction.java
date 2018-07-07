package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Interaction extends Entity {
    private String preface;
    private List<Integer> optionIds = new ArrayList<>();
    private int optionSize;
//    private int preId;
//    private Class preClazz;

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public int getOptionSize() {
        return optionSize;
    }

    public void setOptionSize(int optionSize) {
        this.optionSize = optionSize;
    }

//    public int getPreId() {
//        return preId;
//    }
//
//    public void setPreId(int preId) {
//        this.preId = preId;
//    }
//
//    public Class getPreClazz() {
//        return preClazz;
//    }
//
//    public void setPreClazz(Class preClazz) {
//        this.preClazz = preClazz;
//    }

    @Override
    public String toString() {
        String desc = "Interaction " + getId() + "\n" +
                SPLIT_LINE + "\n" +
                "preface:\n" +
                preface + "\n" +
                "optionSize: " + optionSize + "\n" +
                "optionIds:[";
        for (Integer optionId : optionIds) {
            desc += optionId + ", ";
        }
        if (optionIds.size() > 0) {
            desc = desc.substring(0, desc.length() - 2);
        }
        desc += "]\n" + SPLIT_LINE;
        return desc;
    }

    @Override
    public String getDesc() {
        return "i" + getId() + "(" + Entity.cutText(preface) + ")";
    }
}
