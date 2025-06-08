package org.kungnection.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.kungnection.dto.MessageDTO;
import org.kungnection.model.*;
import org.kungnection.repository.*;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

        private final MessageRepository messageRepository;
        private final FriendChatRoomRepository friendChatRoomRepository;
        private final UserRepository userRepository;

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @PostMapping("/friend/{roomId}")
        public MessageDTO sendToFriend(
                        HttpServletRequest request,
                        @PathVariable Long roomId,
                        @RequestBody String content) {

                Long userId = (Long) request.getAttribute("userId");
                if (userId == null)
                        throw new RuntimeException("User not authenticated.");

                User sender = userRepository.findById(userId).orElseThrow();
                FriendChatRoom room = friendChatRoomRepository.findById(roomId).orElseThrow();

                Message message = new Message();
                message.setSender(sender);
                message.setFriendRoom(room);
                message.setContent(content);
                message.setTimestamp(java.time.LocalDateTime.now());

                Message saved = messageRepository.save(message);

                return new MessageDTO(
                                saved.getId(),
                                sender.getId(),
                                sender.getNickname(),
                                saved.getContent(),
                                saved.getTimestampAsLocalDateTime().format(FORMATTER));
        }

        @GetMapping("/friend/{roomId}")
        public List<MessageDTO> getFriendMessages(
                        HttpServletRequest request,
                        @PathVariable Long roomId) {

                Long userId = (Long) request.getAttribute("userId");
                if (userId == null)
                        throw new RuntimeException("User not authenticated.");

                FriendChatRoom room = friendChatRoomRepository.findById(roomId).orElseThrow();

                return messageRepository.findByFriendRoomOrderByTimestampAsc(room)
                                .stream()
                                .map(m -> new MessageDTO(
                                                m.getId(),
                                                m.getSender().getId(),
                                                m.getSender().getNickname(),
                                                m.getContent(),
                                                m.getTimestampAsLocalDateTime().format(FORMATTER)))
                                .toList();
        }
}