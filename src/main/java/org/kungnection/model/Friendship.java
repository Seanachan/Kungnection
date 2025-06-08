package org.kungnection.model;

import org.kungnection.db.UserDAO;

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

    public Friendship(int user1Id, int user2Id) {
        // use UserDAO to fetch User objects
        UserDAO userDAO = new UserDAO();
        try {
            this.user1 = userDAO.findById(user1Id);
            if (this.user1 == null) {
                throw new IllegalArgumentException("User with ID " + user1Id + " does not exist.");
            }
            this.user2 = userDAO.findById(user2Id);
            if (this.user2 == null) {
                throw new IllegalArgumentException("User with ID " + user2Id + " does not exist.");
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while fetching users", e);
        }
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
