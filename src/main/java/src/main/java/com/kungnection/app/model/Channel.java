package com.kungnection.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 頻道名稱

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<ChannelMembership> members; // 所有成員（含管理員）
}