package org.kungnection.service;

import org.kungnection.model.*;
import org.kungnection.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMembershipRepository channelMembershipRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendChatRoomRepository friendChatRoomRepository;

    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CODE_CHARACTERS_NUMBER = "0123456789";
    private static final int CODE_LENGTH = 6;

    // -------------------- 基本帳號功能 --------------------

    public User register(User user) {
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // -------------------- 頻道功能 --------------------

    @Transactional
    public Channel createChannel(User user, String name) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setCode(generateUniqueChannelCode());
        channelRepository.save(channel);

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        channelMembershipRepository.save(membership);

        return channel;
    }

    @Transactional
    public boolean joinChannel(User user, String code) {
        Optional<Channel> optional = channelRepository.findById(code);
        if (optional.isEmpty())
            return false;

        Channel channel = optional.get();

        boolean exists = channelMembershipRepository.existsByUserAndChannel(user, channel);
        if (exists)
            return false;

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        channelMembershipRepository.save(membership);

        return true;
    }

    public List<ChannelMembership> getChannels(User user) {
        return channelMembershipRepository.findAllByUser(user);
    }

    public List<User> getUsersInChannel(String channelCode) {
        Optional<Channel> optional = channelRepository.findById(channelCode);
        if (optional.isEmpty()) {
            throw new RuntimeException("Channel not found: code = " + channelCode);
        }

        Channel channel = optional.get();
        List<ChannelMembership> memberships = channelMembershipRepository.findAllByChannel(channel);

        return memberships.stream()
                .map(ChannelMembership::getUser)
                .toList();
    }

    // -------------------- 好友功能 --------------------

    @Transactional
    public boolean addFriend(User user, User target) {
        if (user.equals(target))
            return false;

        boolean already = friendshipRepository.existsByUsers(user.getId(), target.getId());
        if (already)
            return false;

        Friendship f1 = new Friendship();
        f1.setUser1(user);
        f1.setUser2(target);
        friendshipRepository.save(f1);

        Friendship f2 = new Friendship();
        f2.setUser1(target);
        f2.setUser2(user);
        friendshipRepository.save(f2);

        // 自動建立一對一聊天室
        FriendChatRoom room = new FriendChatRoom();
        room.setUser1(user);
        room.setUser2(target);
        friendChatRoomRepository.save(room);

        return true;
    }

    public List<Friendship> getFriends(User user) {
        return friendshipRepository.findAllByUser(user);
    }

    private String generateUniqueCode() {
        String code;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
            }
            code = sb.toString();
        } while (channelRepository.existsById(code));
        return code;
    }

    private int generateUniqueChannelCode() {
        String code;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARACTERS_NUMBER.charAt(random.nextInt(CODE_CHARACTERS_NUMBER.length())));
            }
            code = sb.toString();
        } while (channelRepository.existsById(code));
        return Integer.parseInt(code, 10); // 將字串轉為整數
    }

    public List<FriendChatRoom> getFriendChatRooms(User user) {
        return friendChatRoomRepository.findAllByUser(user);
    }
}