package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 所屬使用者

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel; // 所屬頻道

    private boolean isAdmin; // 是否為該頻道的管理員
}
