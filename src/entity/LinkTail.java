package entity;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class LinkTail extends Entity {
    private int nextId;
    private Class nextClazz;

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public Class getNextClazz() {
        return nextClazz;
    }

    public void setNextClazz(Class nextClazz) {
        this.nextClazz = nextClazz;
    }
}
