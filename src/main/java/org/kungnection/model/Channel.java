package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

//import org.kungnection.model.Message;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @Column(length = 6, unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    // 加入成員關聯
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembership> members;

    // ✅ 新增：頻道的所有訊息
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}