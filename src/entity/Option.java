package entity;

/**
 * @author Zhou Guanliang
 * @since 2018/5/27
 */
public class Option extends LinkTail {
    private int code;
    private String text;
    private int interactionId;

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
}
