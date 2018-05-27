package entity;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Statement extends LinkTail {
    private String text;
    private int preId;
    private Class preClazz;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
