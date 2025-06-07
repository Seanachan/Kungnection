package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 

    @Column(unique = true, nullable = false)
    private String username; // 顯示名稱用帳號（非登入用）

    @Column(unique = true, nullable = false)
    private String email; // 登入用電子信箱（唯一）

    @Column(nullable = false)
    private String password; // 密碼（建議加密儲存）

    private String nickname; // 使用者暱稱（可選）

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembership> channelMemberships;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> friends;

    public User(int id, String username, String nickname, String email, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public  String getPasswordHash() {
        return password; 
    }
}