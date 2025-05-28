package org.kungnection.repository;

import org.kungnection.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFriendRoomOrderByTimestampAsc(FriendChatRoom room);
}