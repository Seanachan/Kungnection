package org.kungnection.service;

import org.kungnection.model.*;
import org.kungnection.repository.ChannelDAO;
import org.kungnection.repository.ChannelMembershipDAO;
import org.kungnection.repository.FriendChatRoomDAO;
import org.kungnection.repository.FriendshipDAO;
import org.kungnection.repository.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Service layer for user-related business logic including authentication,
 * channel management, and friend operations.
 */
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    private final UserDAO userDAO;
    private final ChannelDAO channelDAO;
    private final ChannelMembershipDAO channelMembershipDAO;
    private final FriendshipDAO friendshipDAO;
    private final FriendChatRoomDAO friendChatRoomDAO;

    public UserService(UserDAO userDAO, ChannelDAO channelDAO,
                       ChannelMembershipDAO channelMembershipDAO,
                       FriendshipDAO friendshipDAO,
                       FriendChatRoomDAO friendChatRoomDAO) {
        this.userDAO = userDAO;
        this.channelDAO = channelDAO;
        this.channelMembershipDAO = channelMembershipDAO;
        this.friendshipDAO = friendshipDAO;
        this.friendChatRoomDAO = friendChatRoomDAO;
    }

    /**
     * Registers a new user.
     */
    public User register(User user) {
        return userDAO.save(user);
    }

    /**
     * Authenticates a user by email and password.
     * @return the authenticated User or null if authentication fails
     */
    public User login(String email, String password) {
        try {
            User user = userDAO.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (java.sql.SQLException e) {
            log.error("Login failed for email {}: {}", email, e.getMessage());
            return null;
        }
    }

    // -------------------- Channel Operations --------------------

    /**
     * Creates a new channel and adds the creator as the first member.
     */
    public Channel createChannel(User user, String name) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setCode(generateUniqueCode());
        try {
            channelDAO.save(channel);
        } catch (java.sql.SQLException e) {
            log.error("Failed to save channel: {}", e.getMessage());
            throw new RuntimeException("Failed to save channel", e);
        }

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        try {
            channelMembershipDAO.save(membership);
        } catch (java.sql.SQLException e) {
            log.error("Failed to save channel membership: {}", e.getMessage());
            throw new RuntimeException("Failed to save channel membership", e);
        }

        return channel;
    }

    /**
     * Joins a user to an existing channel by invite code.
     * @return true if successfully joined, false if channel not found or already a member
     */
    public boolean joinChannel(User user, String code) {
        Channel channel;
        try {
            channel = channelDAO.findByCode(code);
        } catch (java.sql.SQLException e) {
            log.error("Failed to find channel by code {}: {}", code, e.getMessage());
            throw new RuntimeException("Failed to find channel by code", e);
        }
        if (channel == null)
            return false;

        boolean exists;
        try {
            exists = channelMembershipDAO.existsByUserAndChannel(user.getId(), channel.getChannelId());
        } catch (java.sql.SQLException e) {
            log.error("Failed to check channel membership: {}", e.getMessage());
            throw new RuntimeException("Failed to check channel membership", e);
        }
        if (exists)
            return false;

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        try {
            channelMembershipDAO.save(membership);
        } catch (java.sql.SQLException e) {
            log.error("Failed to save channel membership: {}", e.getMessage());
            throw new RuntimeException("Failed to save channel membership", e);
        }

        return true;
    }

    /**
     * Gets all channels a user belongs to.
     */
    public List<ChannelMembership> getChannels(User user) {
        try {
            return channelMembershipDAO.findAllByUserId(user.getId());
        } catch (java.sql.SQLException e) {
            log.error("Failed to get channels for user {}: {}", user.getId(), e.getMessage());
            throw new RuntimeException("Failed to get channels for user", e);
        }
    }

    /**
     * Gets all users in a specific channel.
     */
    public List<User> getUsersInChannel(String channelCode) {
        Channel channel;
        try {
            channel = channelDAO.findByCode(channelCode);
        } catch (java.sql.SQLException e) {
            log.error("Failed to find channel by code {}: {}", channelCode, e.getMessage());
            throw new RuntimeException("Failed to find channel by code: " + channelCode, e);
        }
        if (channel == null) {
            throw new RuntimeException("Channel not found: code = " + channelCode);
        }

        List<ChannelMembership> memberships;
        try {
            memberships = channelMembershipDAO.findAllByChannelId(channel.getChannelId());
        } catch (java.sql.SQLException e) {
            log.error("Failed to get channel memberships for channel {}: {}", channelCode, e.getMessage());
            throw new RuntimeException("Failed to get channel memberships for channel: " + channelCode, e);
        }

        return memberships.stream()
                .map(ChannelMembership::getUser)
                .toList();
    }

    // -------------------- Friend Operations --------------------

    /**
     * Adds a bidirectional friendship between two users and creates a chat room.
     * @return true if friendship created, false if users are same or already friends
     */
    public boolean addFriend(User user, User target) {
        if (user.equals(target))
            return false;

        boolean already;
        try {
            already = friendshipDAO.exists(user.getId(), target.getId());
        } catch (java.sql.SQLException e) {
            log.error("Failed to check friendship existence: {}", e.getMessage());
            throw new RuntimeException("Failed to check if friendship exists", e);
        }
        if (already)
            return false;

        Friendship f1 = new Friendship();
        f1.setUser1(user);
        f1.setUser2(target);

        Friendship f2 = new Friendship();
        f2.setUser1(target);
        f2.setUser2(user);

        // Create one-on-one chat room automatically
        FriendChatRoom room = new FriendChatRoom();
        room.setUser1(user);
        room.setUser2(target);

        try {
            friendshipDAO.save(f1);
            friendshipDAO.save(f2);
            friendChatRoomDAO.save(room);
        } catch (java.sql.SQLException e) {
            log.error("Failed to save friendship or chat room: {}", e.getMessage());
            throw new RuntimeException("Failed to save friendship or chat room", e);
        }

        return true;
    }

    /**
     * Gets all friendships for a user.
     */
    public List<Friendship> getFriends(User user) {
        try {
            return friendshipDAO.findAllByUserId(user.getId());
        } catch (java.sql.SQLException e) {
            log.error("Failed to get friends for user {}: {}", user.getId(), e.getMessage());
            throw new RuntimeException("Failed to get friends for user", e);
        }
    }

    /**
     * Generates a unique 6-character alphanumeric code for channel invites.
     */
    private String generateUniqueCode() {
        String code;
        Random random = new Random();
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
            }
            code = sb.toString();
            try {
                if (!channelDAO.existsByCode(code)) {
                    break;
                }
            } catch (java.sql.SQLException e) {
                log.error("Failed to check if channel code exists: {}", e.getMessage());
                throw new RuntimeException("Failed to check if channel code exists", e);
            }
        }
        return code;
    }

    /**
     * Gets all friend chat rooms for a user.
     */
    public List<FriendChatRoom> getFriendChatRooms(User user) {
        return friendChatRoomDAO.findAllByUserId(user.getId());
    }
}
