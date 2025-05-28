package org.example.db;

public class ChatMessage {
    private int id;
    private String sender;
    private String message;
    private long timestamp;

    public ChatMessage(int id, String sender, String message, long timestamp) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
