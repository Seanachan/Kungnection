package org.kungnection.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
	private final Connection conn;

	public MessageDAO(Connection conn) {
		this.conn = conn;
	}

	public void save(ChatMessage msg) throws SQLException {
		String sql = "INSERT INTO messages (conversation_id, sender_id, message_text, sent_at, message_type)"
				+ " VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, msg.getConversationId());
			ps.setInt(2, msg.getSenderId());
			ps.setString(3, msg.getMessageText());
			ps.setTimestamp(4, new Timestamp(msg.getTimestamp()));
			ps.setString(5, msg.getMessageType());

			ps.executeUpdate();
			System.out.println("Message saved: " + msg);
		} catch (SQLException e) {
			System.err.println("error: save" + e.getMessage());
			throw e;
		}
	}

	public List<ChatMessage> findMessagesByConversationId(int conversationId) throws SQLException {
		List<ChatMessage> list = new ArrayList<>();
		String sql = "SELECT conversation_id, sender_id, message_text, sent_at, message_type FROM messages WHERE conversation_id = ? ORDER BY sent_at ASC";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, conversationId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("message_id");
					int convId = rs.getInt("conversation_id");
					int senderId = rs.getInt("sender_id");
					String messageText = rs.getString("message_text");
					long timestamp = rs.getTimestamp("sent_at").getTime();
					String messageType = rs.getString("message_type");

					ChatMessage msg = new ChatMessage(id, senderId, convId, messageText, timestamp, messageType);
					list.add(msg);
				}
			}
		} catch (SQLException e) {
			System.err.println("error: finding message" + e.getMessage());
			throw e;
		}
		return list;
	}
}