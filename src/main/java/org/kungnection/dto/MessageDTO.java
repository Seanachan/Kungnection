package org.kungnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO {
    private int id;
    private int senderId;
    private String senderName;
    private String content;
    private String timestamp; // 建議回傳 ISO 格式字串
}