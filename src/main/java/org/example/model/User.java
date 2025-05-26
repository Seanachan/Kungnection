package com.kungnection.app.model;

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
    private Long id; // 使用者唯一識別碼

    @Column(unique = true, nullable = false)
    private String username; // 使用者帳號（唯一）

    @Column(nullable = false)
    private String password; // 使用者密碼（建議加密儲存）

    private String nickname; // 使用者暱稱

    // 與 ChannelMembership 的一對多關聯，表示此使用者所加入的所有頻道（含是否為管理員）
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembership> channelMemberships;

    // 與 Friendship 的一對多關聯，表示使用者加的好友（雙向好友關係須由程式邏輯控制）
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<Friendship> friends;

    // 單向一對一：使用者的狀態設定（如 IDLE、ONLINE）
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "setting_id")
    private Setting setting;
}