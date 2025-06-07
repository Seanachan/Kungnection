package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// import org.kungnection.model.Message;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 雙方成員
    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    // 雙方訊息紀錄
    @OneToMany(mappedBy = "friendRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    /**
     * 根據目前使用者傳回聊天室顯示名稱（對方的名字）
     */
    public String getDisplayNameFor(User viewer) {
        if (viewer.equals(user1)) return user2.getNickname();
        else if (viewer.equals(user2)) return user1.getNickname();
        else return "(未知使用者)";
    }

    /**
     * 回傳聊天室的所有成員
     */
    public List<User> getMembers() {
        return List.of(user1, user2);
    }
}