package org.kungnection.repository;

import org.kungnection.model.FriendChatRoom;
import org.kungnection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendChatRoomRepository extends JpaRepository<FriendChatRoom, Long> {

    @Query("SELECT r FROM FriendChatRoom r WHERE r.user1 = :user OR r.user2 = :user")
    List<FriendChatRoom> findAllByUser(@Param("user") User user);
}