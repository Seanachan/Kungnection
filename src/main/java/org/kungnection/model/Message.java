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
    private Long id;

    @ManyToOne
    private User sender;

    private LocalDateTime timestamp;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private FriendChatRoom friendRoom;

    @ManyToOne
    private Channel channel; // 可以先留著，未來實作頻道訊息用
}