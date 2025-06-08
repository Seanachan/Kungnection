package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import org.kungnection.db.MessageDAO;
import org.kungnection.db.UserDAO;

// import org.kungnection.model.Message;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
        if (viewer.equals(user1))
            return user2.getNickname();
        else if (viewer.equals(user2))
            return user1.getNickname();
        else
            return "(未知使用者)";
    }

    /**
     * 回傳聊天室的所有成員
     */
    public List<User> getMembers() {
        return List.of(user1, user2);
    }

    public FriendChatRoom(int user1Id, int user2Id) {
        UserDAO userDAO = new UserDAO();
        MessageDAO messageDAO = new MessageDAO();
        try {
            this.user1 = userDAO.findById(user1Id);
            if (this.user1 == null) {
                throw new IllegalArgumentException("User with ID " + user1Id + " does not exist.");
            }
            this.user2 = userDAO.findById(user2Id);
            if (this.user2 == null) {
                throw new IllegalArgumentException("User with ID " + user2Id + " does not exist.");
            }
            this.messages = messageDAO.findByFriendChatRoomId(this.id);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while fetching users or messages", e);
        }
    }

    public int getUser1Id() {
        return user1 != null ? user1.getId() : 0; // 或拋出異常
    }

    public int getUser2Id() {
        return user2 != null ? user2.getId() : 0; // 或拋出異常
    }
}