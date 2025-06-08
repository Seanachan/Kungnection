package org.kungnection.repository;

import org.kungnection.model.Friendship;
import org.kungnection.model.User;

import java.util.List;

// public interface FriendshipRepository extends JpaRepository<Friendship, Long>
// {

// @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM
// Friendship f " +
// "WHERE (f.user1.id = :userId1 AND f.user2.id = :userId2) " +
// " OR (f.user1.id = :userId2 AND f.user2.id = :userId1)")
// boolean existsByUsers(Long userId1, Long userId2);

// List<Friendship> findAllByUser1(User user);
// }

public class FriendshipRepository {

    public FriendshipRepository() {
        // Constructor logic if needed
    }

    public Friendship save(User user1, User user2) {
        Friendship friendship = new Friendship(user1, user2);
        return friendship;
    }

    public Friendship save(Friendship friendship) {
        // TODO
        return friendship;
    }

    public void updateStatus(User user1, User user2, String status) {
        // TODO
        return;
    }

    public void delete(User user1, User user2) {

    }

    public boolean exists(User user1, User user2) {
        // TODO
        return false;
    }

    public List<Friendship> findAllByUser(User user) {
        // TODO
        return List.of();
    }

    public boolean existsByUsers(int userId1, int userId2) {
        // TODO
        return false;
    }
}