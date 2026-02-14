package org.kungnection.repository;

import org.kungnection.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDAO {
	private static final Logger log = LoggerFactory.getLogger(MessageDAO.class);
	private final DataSource dataSource;

	public MessageDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Message save(Message msg) throws SQLException {
		String sql = "INSERT INTO messages (conversation_id, sender_id, message_text, sent_at, message_type) VALUES (?, ?, ?, ?, ?)";
		try (var conn = dataSource.getConnection();
				var ps = conn.prepareStatement(sql)) {
			ps.setInt(1, msg.getConversationId());
			ps.setInt(2, msg.getSenderId());
			ps.setString(3, msg.getMessageText());
			ps.setTimestamp(4, new Timestamp(msg.getTimestamp()));
			ps.setString(5, msg.getMessageType());
			ps.executeUpdate();
			log.debug("Message saved: {}", msg);
		} catch (SQLException e) {
			log.error("Failed to save message: {}", e.getMessage());
			throw e;
		}
		return msg;
	}

	public List<Message> findByFriendChatRoomId(int conversationId) throws SQLException {
		List<Message> list = new ArrayList<>();
		String sql = "SELECT * FROM messages WHERE chat_room_id = ? ORDER BY sent_at";
		try (var conn = dataSource.getConnection();
				var ps = conn.prepareStatement(sql)) {
			ps.setInt(1, conversationId);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					int senderId = rs.getInt("sender_id");
					String messageText = rs.getString("message_text");
					Timestamp sentAt = rs.getTimestamp("sent_at");
					String messageType = rs.getString("message_type");
					Message msg = new Message(id, conversationId, senderId, messageText, sentAt.getTime(), messageType);
					list.add(msg);
				}
			}
		} catch (SQLException e) {
			log.error("Failed to find messages by friend chat room ID: {}", e.getMessage());
			throw e;
		}
		return list;
	}

	public List<Message> findByChannelId(int channelId) throws SQLException {
		List<Message> list = new ArrayList<>();
		String sql = "SELECT * FROM messages WHERE channel_id = ? ORDER BY sent_at";
		try (var conn = dataSource.getConnection();
				var ps = conn.prepareStatement(sql)) {
			ps.setInt(1, channelId);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					int senderId = rs.getInt("sender_id");
					String messageText = rs.getString("message_text");
					Timestamp sentAt = rs.getTimestamp("sent_at");
					String messageType = rs.getString("message_type");
					Message msg = new Message(id, channelId, senderId, messageText, sentAt.getTime(), messageType);
					list.add(msg);
				}
			}
		} catch (SQLException e) {
			log.error("Failed to find messages by channel ID: {}", e.getMessage());
			throw e;
		}
		return list;
	}

	public List<Message> findByFriendRoomOrderByTimestampAsc(int roomId) throws SQLException {
		List<Message> list = new ArrayList<>();
		String sql = "SELECT * FROM messages WHERE conversation_id = ? ORDER BY sent_at ASC";
		try (var conn = dataSource.getConnection();
				var ps = conn.prepareStatement(sql)) {
			ps.setInt(1, roomId);
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					int senderId = rs.getInt("sender_id");
					String messageText = rs.getString("message_text");
					Timestamp sentAt = rs.getTimestamp("sent_at");
					String messageType = rs.getString("message_type");
					Message msg = new Message(id, roomId, senderId, messageText, sentAt.getTime(), messageType);
					list.add(msg);
				}
			}
		}
		return list;
	}

	public List<Message> findByChannelOrderByTimestampAsc(Channel channel) throws SQLException {
		List<Message> list = new ArrayList<>();
		String sql = "SELECT * FROM messages WHERE channel_id = ? ORDER BY sent_at ASC";
		try (var conn = dataSource.getConnection();
				var ps = conn.prepareStatement(sql)) {
			ps.setInt(1, channel.getId());
			try (var rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					int senderId = rs.getInt("sender_id");
					String messageText = rs.getString("message_text");
					Timestamp sentAt = rs.getTimestamp("sent_at");
					String messageType = rs.getString("message_type");
					Message msg = new Message(id, channel.getId(), senderId, messageText, sentAt.getTime(), messageType);
					list.add(msg);
				}
			}
		} catch (SQLException e) {
			log.error("Failed to find messages by channel: {}", e.getMessage());
			throw e;
		}
		list.sort((m1, m2) -> Long.compare(m1.getTimestamp(), m2.getTimestamp()));
		return list;
	}
}