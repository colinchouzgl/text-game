package entity;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Option extends LinkTail {
    private int code;
    private String text;
    private int interactionId;

    public Option() {
    }

    public Option(int code, String text, int interactionId, int nextId, Class nextClazz) {
        this.code = code;
        this.text = text;
        this.interactionId = interactionId;
        super.setNextId(nextId);
        super.setNextClazz(nextClazz);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        this.interactionId = interactionId;
    }

    @Override
    public String toString() {
        return "Option " + getId() + "\n" +
                SPLIT_LINE + "\n" +
                "code: " + code + "\n" +
                "text:\n" +
                text + "\n" +
                "interactionId: " + interactionId + "\n" +
                "nextId: " + getNextId() + "\n" +
                "nextClazz: " + getNextClazz() + "\n" +
                SPLIT_LINE;
    }

    @Override
    public String getDesc() {
        return "o" + getId() + "(" + Entity.cutText(text) + ")";
    }
}
