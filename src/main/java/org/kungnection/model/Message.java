package org.kungnection.model;

import lombok.*;
import java.time.LocalDateTime;

// import org.kungnection.model.User;
// import org.kungnection.model.Channel;
// import org.kungnection.model.FriendChatRoom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private int id;

    private User sender;

    private LocalDateTime timestamp;

    private String content;

    private FriendChatRoom friendRoom;

    private Channel channel;

    public Message(int id, int senderId, int convId, String messageText, long timestamp, String messageType) {
        this.id = id;
        this.sender = new User();
        this.sender.setId(senderId);
        this.timestamp = java.time.Instant.ofEpochMilli(timestamp).atZone(java.time.ZoneOffset.UTC).toLocalDateTime();
        this.content = messageText;
        // friendRoom and channel can be set by the DAO/service if needed
    }

    public int getConversationId() {
        if (friendRoom != null) {
            return friendRoom.getId();
        } else if (channel != null) {
            return channel.getId();
        }
        return 0; // or throw an exception if neither is set
    }

    public int getSenderId() {
        return sender != null ? sender.getId() : 0;
    }

    public String getMessageText() {
        return content != null ? content : "";
    }

    public long getTimestamp() {
        return timestamp != null ? timestamp.atZone(java.time.ZoneOffset.UTC).toInstant().toEpochMilli() : 0L;
    }

    public LocalDateTime getTimestampAsLocalDateTime() {
        return timestamp;
    }

    public String getMessageType() {
        if (friendRoom != null) {
            return "FRIEND_CHAT";
        } else if (channel != null) {
            return "CHANNEL_CHAT";
        }
        return "UNKNOWN";
    }
}