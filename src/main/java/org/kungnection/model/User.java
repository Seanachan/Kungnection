package org.kungnection.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;

    private String username; // 顯示名稱用帳號（非登入用）

    private String email; // 登入用電子信箱（唯一）

    private String password; // 密碼（建議加密儲存）

    private String nickname; // 使用者暱稱（可選）

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