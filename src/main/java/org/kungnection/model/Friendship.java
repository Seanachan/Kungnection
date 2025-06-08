package org.kungnection.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    private Long id;

    private User user1;

    private User user2;

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Friendship(int user1Id, int user2Id) {
        this.user1 = new User();
        this.user1.setId(user1Id);
        this.user2 = new User();
        this.user2.setId(user2Id);
    }

    public Long getId() {
        return id;
    }

    public int getUser1Id() {
        return user1 != null ? user1.getId() : 0;
    }

    public int getUser2Id() {
        return user2 != null ? user2.getId() : 0;
    }

    public String getStatus() {
        // You may want to set status from DB or service layer
        return "Friends";
    }
}
