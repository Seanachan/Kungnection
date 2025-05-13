package com.kungnection.app.service;

import com.kungnection.app.model.*;
import com.kungnection.app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserService 負責處理與使用者相關的業務邏輯：
 * 包含註冊、登入、頻道操作、好友操作與狀態管理。
 */
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

    // -------------------- 基本帳號功能 --------------------

    /**
     * 註冊新使用者
     */
    public User register(User user) {
        return userRepository.save(user);
    }

    /**
     * 使用者登入驗證（帳號密碼比對）
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // -------------------- 頻道功能 --------------------

    /**
     * 創建一個新頻道，並設定目前使用者為管理員
     */
    @Transactional
    public Channel createChannel(User user, String name) {
        Channel channel = new Channel();
        channel.setName(name);
        channelRepository.save(channel);

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        membership.setAdmin(true);
        channelMembershipRepository.save(membership);

        return channel;
    }

    /**
     * 使用者加入一個已存在的頻道
     */
    @Transactional
    public boolean joinChannel(User user, Long channelId) {
        Optional<Channel> optional = channelRepository.findById(channelId);
        if (optional.isEmpty()) return false;

        Channel channel = optional.get();

        // 若已加入則不可重複加入
        boolean exists = channelMembershipRepository.existsByUserAndChannel(user, channel);
        if (exists) return false;

        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        membership.setChannel(channel);
        membership.setAdmin(false);
        channelMembershipRepository.save(membership);

        return true;
    }

    /**
     * 查詢使用者加入的所有頻道（含是否為管理員）
     */
    public List<ChannelMembership> getChannels(User user) {
        return channelMembershipRepository.findAllByUser(user);
    }

    // -------------------- 好友功能 --------------------

    /**
     * 新增好友關係（雙向新增）
     */
    @Transactional
    public boolean addFriend(User user, User target) {
        if (user.equals(target)) return false;

        // 判斷雙方是否已經是朋友
        boolean already = friendshipRepository.existsByUsers(user.getId(), target.getId());
        if (already) return false;

        // 建立雙向關係
        Friendship f1 = new Friendship();
        f1.setUser1(user);
        f1.setUser2(target);
        friendshipRepository.save(f1);

        Friendship f2 = new Friendship();
        f2.setUser1(target);
        f2.setUser2(user);
        friendshipRepository.save(f2);

        return true;
    }

    /**
     * 查詢目前使用者的所有好友
     */
    public List<Friendship> getFriends(User user) {
        return friendshipRepository.findAllByUser1(user);
    }

    // -------------------- 狀態功能 --------------------

    /**
     * 設定使用者狀態（ONLINE / IDLE / DO_NOT_DISTURB）
     */
    public void setStatus(User user, State state) {
        if (user.getSetting() == null) {
            Setting setting = new Setting();
            setting.setState(state);
            user.setSetting(setting);
        } else {
            user.getSetting().setState(state);
        }
        userRepository.save(user);
    }

    /**
     * 查詢使用者狀態
     */
    public State getStatus(User user) {
        if (user.getSetting() != null) {
            return user.getSetting().getState();
        }
        return State.IDLE;
    }
}