package org.kungnection.service;

import org.kungnection.db.*;
import org.kungnection.model.*;
// import org.kungnection.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    // @Autowired
    // private UserRepository userRepository;

    // @Autowired
    // private ChannelRepository channelRepository;

    // @Autowired
    // private ChannelMembershipRepository channelMembershipRepository;

    // @Autowired
    // private FriendshipRepository friendshipRepository;

    // @Autowired
    // private FriendChatRoomRepository friendChatRoomRepository;

    @Autowired
    UserDAO userDAO;
    @Autowired
    ChannelDAO channelDAO;
    @Autowired
    ChannelMembershipDAO channelMembershipDAO;
    @Autowired
    FriendshipDAO friendshipDAO;
    @Autowired
    FriendChatRoomDAO friendChatRoomDAO;

    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    public User register(User user) {
        return userDAO.save(user);
    }

    public User login(String email, String password) {
        try {
            User user = userDAO.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (java.sql.SQLException e) {
            // Handle exception, e.g., log it and return null or rethrow as a runtime
            // exception
            e.printStackTrace();
            return null;
        }
    }

    // -------------------- 頻道功能 --------------------

    public Channel createChannel(User user, String name) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setCode(generateUniqueCode());
        try {
            channelDAO.save(channel);
        } catch (java.sql.SQLException e) {
            // Handle exception, e.g., log it or rethrow as a runtime exception
            e.printStackTrace();
            throw new RuntimeException("Failed to save channel", e);
        }

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        try {
            channelMembershipDAO.save(membership);
        } catch (java.sql.SQLException e) {
            // Handle exception, e.g., log it or rethrow as a runtime exception
            e.printStackTrace();
            throw new RuntimeException("Failed to save channel membership", e);
        }

        return channel;
    }

    public boolean joinChannel(User user, String code) {
        Channel channel;
        try {
            channel = channelDAO.findByCode(code);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find channel by code", e);
        }
        if (channel == null)
            return false;

        boolean exists;
        try {
            exists = channelMembershipDAO.existsByUserAndChannel(user.getId(), channel.getChannelId());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            throw new RuntimeException("Failed to save channel membership", e);
        }

        return true;
    }

    public List<ChannelMembership> getChannels(User user) {
        try {
            return channelMembershipDAO.findAllByUserId(user.getId());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get channels for user", e);
        }
    }

    public List<User> getUsersInChannel(String channelCode) {
        int channelId;
        try {
            channelId = Integer.parseInt(channelCode);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid channel code: " + channelCode, e);
        }
        Channel channel;
        try {
            channel = channelDAO.findById(channelId);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find channel by id: " + channelId, e);
        }
        if (channel == null) {
            throw new RuntimeException("Channel not found: code = " + channelCode);
        }

        List<ChannelMembership> memberships;
        try {
            memberships = channelMembershipDAO.findAllByChannelId(channel.getChannelId());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get channel memberships for channel: " + channelCode, e);
        }

        return memberships.stream()
                .map(ChannelMembership::getUser)
                .toList();
    }

    // -------------------- 好友功能 --------------------

    public boolean addFriend(User user, User target) {
        if (user.equals(target))
            return false;

        boolean already;
        try {
            already = friendshipDAO.exists(user.getId(), target.getId());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
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

        // 自動建立一對一聊天室
        FriendChatRoom room = new FriendChatRoom();
        room.setUser1(user);
        room.setUser2(target);

        try {
            friendshipDAO.save(f1);
            friendshipDAO.save(f2);
            friendChatRoomDAO.save(room);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save friendship or chat room", e);
        }

        return true;
    }

    public List<Friendship> getFriends(User user) {
        try {
            return friendshipDAO.findAllByUserId(user.getId());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get friends for user", e);
        }
    }

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
                e.printStackTrace();
                throw new RuntimeException("Failed to check if channel code exists", e);
            }
        }
        return code;
    }

    public List<FriendChatRoom> getFriendChatRooms(User user) {
        return friendChatRoomDAO.findAllByUserId(user.getId());
    }
}