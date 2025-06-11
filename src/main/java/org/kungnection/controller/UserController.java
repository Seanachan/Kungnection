package org.kungnection.controller;

import org.kungnection.dto.UserUpdateDTO;
import org.kungnection.model.*;
import org.kungnection.repository.UserRepository;
import org.kungnection.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

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
    public Channel createChannel(HttpServletRequest request, @RequestBody String channelName) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated.");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return userService.createChannel(user, channelName);
    }

    // ✅ 加入頻道（用六碼代碼）
    @PostMapping("/channels/join")
    public String joinChannel(HttpServletRequest request, @RequestParam String code) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated.");
        System.out.println("✅ token userId = " + userId + ", code = " + code);
        User user = getUserOrThrow(userId);
        return userService.joinChannel(user, code) ? "Joined successfully." : "Join failed.";
    }

    // ✅ 加好友
    @PostMapping("/friends")
    public String addFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        return userService.addFriend(user, friend) ? "Friend added." : "Add failed.";
    }

    @PostMapping("/friends/add")
    public String addFriendByUsername(HttpServletRequest request, @RequestParam String username) {
        System.out.println("🚨 Controller 中取得的 userId = " + request.getAttribute("userId"));
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) throw new RuntimeException("User not authenticated.");

            System.out.println("🔍 登入者 userId: " + userId);
            System.out.println("🔍 欲加好友 username: " + username);

            User currentUser = getUserOrThrow(userId);
            User friend = userRepository.findByUsername(username);

            System.out.println("✅ friend: " + (friend != null ? friend.getId() : "找不到"));

            if (friend == null) {
                return "User not found: " + username;
            }

            if (currentUser.equals(friend)) {
                return "Cannot add yourself as friend.";
            }

            boolean success = userService.addFriend(currentUser, friend);
            System.out.println("✅ addFriend() 回傳: " + success);

            return success ? "Friend added." : "Already friends.";

        } catch (Exception e) {
            e.printStackTrace();  // ✅ 印出完整錯誤訊息
            return "Internal error: " + e.getMessage();
        }
    }

    // ✅ 顯示好友清單
    @GetMapping("/friends")
    public List<Friendship> getFriends(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        return userService.getFriends(user);
    }

    // ✅ 顯示使用者所屬的頻道清單
    @GetMapping("/channels")
    public List<ChannelMembership> getChannels(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        return userService.getChannels(user);
    }

    // ✅ 顯示特定頻道的所有成員（用 code 查）
    @GetMapping("/channel/members")
    public List<User> getChannelMembers(HttpServletRequest request, @RequestParam String code) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated."); // 沒帶 token 或無效 token
        return userService.getUsersInChannel(code);
    }

    @GetMapping("/sidebar")
    public Map<String, Object> getSidebar(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated.");

        User user = getUserOrThrow(userId);

        // 🔹 Friend Rooms
        List<Map<String, Object>> friends = userService.getFriendChatRooms(user).stream()
        .map(room -> Map.<String, Object>of(
            "id", room.getId(),
            "name", room.getDisplayNameFor(user)
        ))
        .toList();

        // 🔹 Channels
        List<Map<String, Object>> channels = userService.getChannels(user).stream()
        .map(cm -> Map.<String, Object>of(
            "code", cm.getChannel().getCode(),
            "name", cm.getChannel().getName()
        ))
        .toList();

        return Map.of(
            "friends", friends,
            "channels", channels
        );
    }

    @GetMapping("/me")
    public User getMyProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated.");
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @PatchMapping("/me")
    public User updateMyProfile(HttpServletRequest request, @RequestBody UserUpdateDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // ✅ 僅在不為 null 的情況下更新欄位
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword()); // 👉 如有加密需求，記得加密

        return userRepository.save(user);
    }
}