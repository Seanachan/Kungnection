package org.kungnection.repository;

import org.kungnection.model.Friendship;
import org.kungnection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friendship f " +
           "WHERE (f.user1.id = :userId1 AND f.user2.id = :userId2) " +
           "   OR (f.user1.id = :userId2 AND f.user2.id = :userId1)")
    boolean existsByUsers(Long userId1, Long userId2);

    List<Friendship> findAllByUser1(User user);
}