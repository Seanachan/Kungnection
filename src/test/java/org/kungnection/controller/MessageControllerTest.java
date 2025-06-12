package org.kungnection.controller;

import org.junit.jupiter.api.Test;
import org.kungnection.dto.MessageDTO;
import org.kungnection.model.FriendChatRoom;
import org.kungnection.model.Message;
import org.kungnection.model.User;
import org.kungnection.repository.ChannelDAO;
import org.kungnection.repository.FriendChatRoomDAO;
import org.kungnection.repository.MessageDAO;
import org.kungnection.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelDAO channelDAO;
    @MockBean
    private FriendChatRoomDAO friendChatRoomDAO;
    @MockBean
    private UserDAO userDAO;
    @MockBean
    private MessageDAO messageDAO;

    @Test
    public void testGetFriendMessages() throws Exception {
        FriendChatRoom room = new FriendChatRoom();
        room.setId(1);
        User sender = new User();
        sender.setId(2);
        sender.setNickname("Alice");
        Message message = new Message();
        message.setId(10);
        message.setSender(sender);
        message.setFriendRoom(room);
        message.setContent("Hello");
        message.setTimestamp(LocalDateTime.of(2025, 6, 9, 12, 0));
        when(friendChatRoomDAO.findByRoomId(1)).thenReturn(room);
        when(messageDAO.findByFriendRoomOrderByTimestampAsc(1)).thenReturn(List.of(message));

        mockMvc.perform(get("/messages/friend/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].senderId").value(2))
                .andExpect(jsonPath("$[0].senderName").value("Alice"))
                .andExpect(jsonPath("$[0].content").value("Hello"));
    }

    @Test
    public void testSendToFriend() throws Exception {
        int userId = 2;
        FriendChatRoom room = new FriendChatRoom();
        room.setId(1);
        User sender = new User();
        sender.setId(userId);
        sender.setNickname("Alice");
        Message message = new Message();
        message.setId(10);
        message.setSender(sender);
        message.setFriendRoom(room);
        message.setContent("Hi");
        message.setTimestamp(LocalDateTime.of(2025, 6, 9, 13, 0));
        when(userDAO.findById(userId)).thenReturn(sender);
        when(friendChatRoomDAO.findByRoomId(1)).thenReturn(room);
        when(messageDAO.save(org.mockito.ArgumentMatchers.any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages/friend/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Hi\"")
                .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.senderId").value(userId))
                .andExpect(jsonPath("$.senderName").value("Alice"))
                .andExpect(jsonPath("$.content").value("Hi"));
    }

    @Test
    public void testSendToChannel() throws Exception {
        int userId = 2;
        User sender = new User();
        sender.setId(userId);
        sender.setNickname("Alice");
        Message message = new Message();
        message.setId(20);
        message.setSender(sender);
        message.setChannel(new org.kungnection.model.Channel());
        message.setContent("Channel Hi");
        message.setTimestamp(LocalDateTime.of(2025, 6, 13, 10, 0));
        when(userDAO.findById(userId)).thenReturn(sender);
        when(channelDAO.findById(1)).thenReturn(message.getChannel());
        when(messageDAO.save(org.mockito.ArgumentMatchers.any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages/channel/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"Channel Hi\"")
                .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.senderId").value(userId))
                .andExpect(jsonPath("$.senderName").value("Alice"))
                .andExpect(jsonPath("$.content").value("Channel Hi"));
    }

    @Test
    public void testGetChannelMessage() throws Exception {
        org.kungnection.model.Channel channel = new org.kungnection.model.Channel();
        channel.setId(1);
        channel.setCode("CHAN123");
        channel.setName("Test Channel");
        channel.setLastActiveTime(System.currentTimeMillis());
        User sender = new User();
        sender.setId(2);
        sender.setNickname("Alice");
        Message message = new Message();
        message.setId(21);
        message.setSender(sender);
        message.setChannel(channel);
        message.setContent("Channel Message");
        message.setTimestamp(LocalDateTime.of(2025, 6, 13, 11, 0));
        when(channelDAO.findById(1)).thenReturn(channel);
        when(channelDAO.findByCode("CHAN123")).thenReturn(channel);
        when(messageDAO.findByChannelOrderByTimestampAsc(channel)).thenReturn(List.of(message));

        mockMvc.perform(get("/messages/channel/CHAN123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(21))
                .andExpect(jsonPath("$[0].senderId").value(2))
                .andExpect(jsonPath("$[0].senderName").value("Alice"))
                .andExpect(jsonPath("$[0].content").value("Channel Message"));
    }
}
