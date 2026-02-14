package org.kungnection.repository;

import javax.sql.DataSource;

import org.kungnection.model.FriendChatRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FriendChatRoomDAO {
    private static final Logger log = LoggerFactory.getLogger(FriendChatRoomDAO.class);
    private final DataSource dataSource;

    public FriendChatRoomDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(int userId, int friendId) {
        String sql = "INSERT INTO friend_chat_rooms (user_id, friend_id) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            ps.executeUpdate();
            log.debug("Friend chat room saved: userId={}, friendId={}", userId, friendId);
        } catch (Exception e) {
            log.error("Failed to save friend chat room: {}", e.getMessage());
        }
    }

    public void save(FriendChatRoom chatRoom) {
        save(chatRoom.getUser1Id(), chatRoom.getUser2Id());
    }

    public FriendChatRoom findById(int userId, int friendId) {
        String sql = "SELECT * FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FriendChatRoom(userId, friendId);
                }
            }
        } catch (Exception e) {
            log.error("Failed to find friend chat room: {}", e.getMessage());
        }
        return null;
    }

    public FriendChatRoom findByRoomId(int roomId) {
        String sql = "SELECT user_id, friend_id FROM friend_chat_rooms WHERE room_id = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FriendChatRoom(rs.getInt("user_id"), rs.getInt("friend_id"));
                }
            }
        } catch (Exception e) {
            log.error("Failed to find friend chat room by room ID: {}", e.getMessage());
        }
        return null;
    }

    public void delete(int userId, int friendId) {
        String sql = "DELETE FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            int rowsAffected = ps.executeUpdate();
            log.debug("Friend chat room delete affected {} rows for userId={}, friendId={}", rowsAffected, userId, friendId);
        } catch (Exception e) {
            log.error("Failed to delete friend chat room: {}", e.getMessage());
        }
    }

    public List<FriendChatRoom> findAllByUserId(int userId) {
        String sql = "SELECT friend_id FROM friend_chat_rooms WHERE user_id = ?";
        List<FriendChatRoom> chatRooms = new ArrayList<>();
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int friendId = rs.getInt("friend_id");
                    chatRooms.add(new FriendChatRoom(userId, friendId));
                }
            }
        } catch (Exception e) {
            log.error("Failed to find friend chat rooms: {}", e.getMessage());
        }
        return chatRooms;
    }

    public List<FriendChatRoom> findAllByFriendId(int friendId) {
        String sql = "SELECT user_id FROM friend_chat_rooms WHERE friend_id = ?";
        List<FriendChatRoom> chatRooms = new ArrayList<>();
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    chatRooms.add(new FriendChatRoom(userId, friendId));
                }
            }
        } catch (Exception e) {
            log.error("Failed to find friend chat rooms by friend ID: {}", e.getMessage());
        }
        return chatRooms;
    }

    public boolean exists(int userId, int friendId) {
        String sql = "SELECT COUNT(*) FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            log.error("Failed to check friend chat room existence: {}", e.getMessage());
        }
        return false;
    }
}
