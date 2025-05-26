package org.kungnection.controller;

import org.kungnection.model.*;
import org.kungnection.repository.UserRepository;
import org.kungnection.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 🔧 工具方法：依 ID 取出 User 實體（簡化重複程式碼）
    private User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("User not found: id = " + id));
    }

    // ✅ 創立頻道
    @PostMapping("/channels")
    public Channel createChannel(@RequestParam Long userId, @RequestBody String channelName) {
        User user = getUserOrThrow(userId);
        return userService.createChannel(user, channelName);
    }

    // ✅ 加入頻道
    @PostMapping("/channels/join")
    public String joinChannel(@RequestParam Long userId, @RequestParam Long channelId) {
        User user = getUserOrThrow(userId);
        return userService.joinChannel(user, channelId) ? "Joined successfully." : "Join failed.";
    }

    // ✅ 加好友
    @PostMapping("/friends")
    public String addFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        return userService.addFriend(user, friend) ? "Friend added." : "Add failed.";
    }

    // ✅ 設定狀態
    @PostMapping("/status")
    public String setStatus(@RequestParam Long userId, @RequestParam State state) {
        User user = getUserOrThrow(userId);
        userService.setStatus(user, state);
        return "Status updated.";
    }

    // ✅ 顯示好友清單
    @GetMapping("/friends")
    public List<Friendship> getFriends(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        return userService.getFriends(user);
    }

    // ✅ 顯示頻道清單（含是否為管理員）
    @GetMapping("/channels")
    public List<ChannelMembership> getChannels(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        return userService.getChannels(user);
    }

    // ✅ 顯示目前狀態
    @GetMapping("/status")
    public State getStatus(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        return userService.getStatus(user);
    }
}