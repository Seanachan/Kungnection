package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Long getId() {
        return id;
    }

    public int getUser1Id() {
        return user1.getId();
    }

    public int getUser2Id() {
        return user2.getId();
    }

    public String getStatus() {
        // Assuming status is determined by the relationship between user1 and user2
        if (user1 != null && user2 != null) {
            return "Friends";
        }
        return "Not Friends";
    }
}
