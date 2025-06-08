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
    @Column(unique = true, nullable = false)
    private int code;

    @Column(nullable = false)
    private String name;

    // 加入成員關聯
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembership> members;

    // 新增：頻道的所有訊息
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Channel(int code, String name, String description, long lastActiveTime) {
        this.code = code;
        this.name = name;
    }

    public int getChannelId() {
        return code;
    }

    public String getChannelName() {
        return name;
    }

    public String getChannelDescription() {
        return "Channel: " + name + " (Code: " + code + ")";
    }

    public long getLastActiveTime() {
        // TODO
        // return lastActiveTime;
        return 0;
    }
}