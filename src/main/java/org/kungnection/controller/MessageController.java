package org.kungnection.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.kungnection.dto.MessageDTO;
import org.kungnection.model.*;
import org.kungnection.repository.FriendChatRoomDAO;
import org.kungnection.repository.MessageDAO;
import org.kungnection.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

        @Autowired
        private MessageDAO messageDAO;
        @Autowired
        private FriendChatRoomDAO friendChatRoomDAO;
        @Autowired
        private UserDAO userDAO;

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @PostMapping("/friend/{roomId}")
        public MessageDTO sendToFriend(
                        HttpServletRequest request,
                        @PathVariable int roomId,
                        @RequestBody String content) {

                int userId = (int) request.getAttribute("userId");

                try {
                        User sender = userDAO.findById(userId);
                        FriendChatRoom room = friendChatRoomDAO.findByRoomId(roomId);

                        Message message = new Message();
                        message.setSender(sender);
                        message.setFriendRoom(room);
                        message.setContent(content);
                        message.setTimestamp(java.time.LocalDateTime.now());

                        Message saved = messageDAO.save(message);

                        return new MessageDTO(
                                        saved.getId(),
                                        sender.getId(),
                                        sender.getNickname(),
                                        saved.getContent(),
                                        saved.getTimestampAsLocalDateTime().format(FORMATTER));
                } catch (java.sql.SQLException e) {
                        throw new RuntimeException("Failed to save message", e);
                }
        }

        @GetMapping("/friend/{roomId}")
        public List<MessageDTO> getFriendMessages(
                        HttpServletRequest request,
                        @PathVariable int roomId) {

                FriendChatRoom room = friendChatRoomDAO.findByRoomId(roomId);

                try {
                        return messageDAO.findByFriendRoomOrderByTimestampAsc(room.getId())
                                        .stream()
                                        .map(m -> new MessageDTO(
                                                        m.getId(),
                                                        m.getSender().getId(),
                                                        m.getSender().getNickname(),
                                                        m.getContent(),
                                                        m.getTimestampAsLocalDateTime().format(FORMATTER)))
                                        .toList();
                } catch (java.sql.SQLException e) {
                        throw new RuntimeException("Failed to retrieve messages", e);
                }
        }
}