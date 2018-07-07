package entity;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Entity {
    public static final String SPLIT_LINE = "---------------";
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean seems(Entity entity) {
        return entity.getId() == this.id && entity.getClass() == this.getClass();
    }

    public String getDesc() {
        return this.getClass().getSimpleName() + id;
    }

    public static String cutText(String text) {
        if (text.length() > 4) {
            return text.substring(0, 4) + "...";
        }
        return text;
    }
}
