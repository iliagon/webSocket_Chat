package Models;

import java.util.ArrayList;

public class Message extends JsonModel {
    private String from;
    private ArrayList<String> to;
    private String content;

    public Message() {
        super("message");
    }

    public String getFrom() {
        return from;
    }

    public Message setFrom(String from) {
        this.from = from;
        return this;
    }

    public ArrayList getTo() {
        return to;
    }

    public Message setTo(ArrayList to) {
        this.to = to;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }
}
