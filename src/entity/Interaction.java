package entity;

import java.util.List;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Interaction extends Entity{
    private String preface;
    private List<Integer> optionIds;
    private int preId;
    private Class preClazz;

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

    public int getPreId() {
        return preId;
    }

    public void setPreId(int preId) {
        this.preId = preId;
    }

    public Class getPreClazz() {
        return preClazz;
    }

    public void setPreClazz(Class preClazz) {
        this.preClazz = preClazz;
    }
}
