// should be removed when the file is moved to the correct package
package org.example.db;

public class ChatMessage {
  private int id;
  private int senderId;
  private int conversationId;
  private String message;
  private long timestamp;
  private String messageType;

  public ChatMessage(int id, int senderId, int conversationId, String message, long timestamp, String messageType) {
    this.id = id;
    this.conversationId = conversationId;
    this.senderId = senderId;
    this.message = message;
    this.timestamp = timestamp;
    this.messageType = messageType;
  }

  public int getId() {
    return id;
  }

  public int getSenderId() {
    return senderId;
  }

  public int getConversationId() {
    return conversationId;
  }

  public String getMessageText() {
    return message;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getMessageType() {
    return messageType;
  }
}
