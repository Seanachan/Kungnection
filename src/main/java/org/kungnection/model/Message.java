package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// import org.kungnection.model.User;
// import org.kungnection.model.Channel;
// import org.kungnection.model.FriendChatRoom;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User sender;

    private LocalDateTime timestamp;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private FriendChatRoom friendRoom;

    @ManyToOne
    private Channel channel;

    public Message(int id, int senderID, int convId, String messageText, long timestamp, String messageType) {
        // this.sender = sender;
        // this.timestamp = timestamp;
        this.id = id;
        this.timestamp = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, java.time.ZoneOffset.UTC);
        this.content = messageText;
        this.friendRoom = null; // or set based on messageType if needed
        this.channel = null; // or set based on messageType if needed
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
        return sender != null ? sender.getId() : 0; // or throw an exception if sender is null
    }

    public String getMessageText() {
        return content != null ? content : "";
    }

    public long getTimestamp() {
        return timestamp != null ? timestamp.toInstant(java.time.ZoneOffset.UTC).toEpochMilli() : 0L;
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