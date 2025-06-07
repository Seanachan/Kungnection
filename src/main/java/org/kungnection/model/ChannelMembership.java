package org.kungnection.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private User user; // 所屬使用者

    @ManyToOne
    @JoinColumn(name = "channel_code", referencedColumnName = "code")  // ✅ 指定用頻道代碼當 FK
    @JsonIgnore
    private Channel channel; // 所屬頻道
}