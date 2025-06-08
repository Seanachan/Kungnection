package org.kungnection.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.kungnection.model.FriendChatRoom;

public class FriendChatRoomDAO {
    private static final Connection conn = DatabaseUtil.getConnection();

    public FriendChatRoomDAO() {
        // Constructor can be used for initialization if needed
    }

    public void save(int userId, int friendId) {
        String sql = "INSERT INTO friend_chat_rooms (user_id, friend_id) VALUES (?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            ps.executeUpdate();
            System.out.println("Friend chat room saved: " + userId + " - " + friendId);
        } catch (Exception e) {
            System.err.println("Error saving friend chat room: " + e.getMessage());
        }
    }

    public void save(FriendChatRoom chatRoom) {
        save(chatRoom.getUser1Id(), chatRoom.getUser2Id());
    }

    public FriendChatRoom findById(int userId, int friendId) {
        String sql = "SELECT * FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FriendChatRoom(userId, friendId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding friend chat room: " + e.getMessage());
        }
        return null;
    }

    public FriendChatRoom findByRoomId(int roomId) {
        String sql = "SELECT user_id, friend_id FROM friend_chat_rooms WHERE room_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FriendChatRoom(rs.getInt("user_id"), rs.getInt("friend_id"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding friend chat room by room ID: " + e.getMessage());
        }
        return null;
    }

    public void delete(int userId, int friendId) {
        String sql = "DELETE FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Friend chat room deleted: " + userId + " - " + friendId);
            } else {
                System.out.println("No friend chat room found to delete for: " + userId + " - " + friendId);
            }
        } catch (Exception e) {
            System.err.println("Error deleting friend chat room: " + e.getMessage());
        }
    }

    public List<FriendChatRoom> findAllByUserId(int userId) {
        String sql = "SELECT friend_id FROM friend_chat_rooms WHERE user_id = ?";
        List<FriendChatRoom> chatRooms = new ArrayList<>();
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int friendId = rs.getInt("friend_id");
                    chatRooms.add(new FriendChatRoom(userId, friendId));
                    System.out.println("Friend chat room found: " + userId + " - " + friendId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding friend chat rooms: " + e.getMessage());
        }
        return chatRooms;
    }

    public void findAllByFriendId(int friendId) {
        String sql = "SELECT user_id FROM friend_chat_rooms WHERE friend_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, friendId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    System.out.println("Friend chat room found: " + userId + " - " + friendId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding friend chat rooms: " + e.getMessage());
        }
    }

    public boolean exists(int userId, int friendId) {
        String sql = "SELECT COUNT(*) FROM friend_chat_rooms WHERE user_id = ? AND friend_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, friendId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking existence of friend chat room: " + e.getMessage());
        }
        return false;
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}