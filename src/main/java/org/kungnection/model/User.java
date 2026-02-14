package org.kungnection.model;

import lombok.*;

import java.util.List;

/**
 * Represents a user in the Kungnection chat application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;

    /** Display username (not used for login) */
    private String username;

    /** Email address used for authentication (unique) */
    private String email;

    /** Password (should be stored hashed in production) */
    private String password;

    /** Optional user nickname */
    private String nickname;

    private List<ChannelMembership> channelMemberships;

    private List<Friendship> friends;

    public User(int id, String username, String nickname, String email, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public User(int id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return password;
    }
}