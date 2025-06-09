package org.kungnection.model;

import lombok.*;
import java.util.List;

// import org.kungnection.model.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendChatRoom {

    private int id;

    private User user1;

    private User user2;

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
        this.user1 = new User();
        this.user1.setId(user1Id);
        this.user2 = new User();
        this.user2.setId(user2Id);
        // messages can be set later by the service/DAO if needed
    }

    public int getUser1Id() {
        return user1 != null ? user1.getId() : 0;
    }

    public int getUser2Id() {
        return user2 != null ? user2.getId() : 0;
    }
}