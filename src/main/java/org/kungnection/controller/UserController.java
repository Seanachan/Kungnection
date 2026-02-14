package org.kungnection.controller;

import org.kungnection.model.*;
import org.kungnection.dto.UserUpdateDTO;
import org.kungnection.repository.UserDAO;
import org.kungnection.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

/**
 * REST controller for user-related operations including channels, friends, and profile management.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserDAO userDAO;

    public UserController(UserService userService, UserDAO userDAO) {
        this.userService = userService;
        this.userDAO = userDAO;
    }

    /**
     * Helper method to fetch a user by ID or throw an exception.
     */
    private User getUserOrThrow(int id) {
        try {
            return userDAO.findById(id);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while fetching user by id: " + id, e);
        }
    }

    /**
     * Creates a new channel with the authenticated user as the owner.
     */
    @PostMapping("/channels")
    public Channel createChannel(HttpServletRequest request, @RequestBody String channelName) {
        int userId = ((Long) request.getAttribute("userId")).intValue();
        User user = getUserOrThrow(userId);
        return userService.createChannel(user, channelName);
    }

    /**
     * Joins an existing channel using a 6-character invite code.
     */
    @PostMapping("/channels/join")
    public String joinChannel(HttpServletRequest request, @RequestParam String code) {
        int userId = ((Long) request.getAttribute("userId")).intValue();
        log.debug("User {} attempting to join channel with code: {}", userId, code);
        User user = getUserOrThrow(userId);
        return userService.joinChannel(user, code) ? "Joined successfully." : "Join failed.";
    }

    /**
     * Adds a friend by user IDs.
     */
    @PostMapping("/friends")
    public String addFriend(@RequestParam int userId, @RequestParam int friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        return userService.addFriend(user, friend) ? "Friend added." : "Add failed.";
    }

    /**
     * Adds a friend by username lookup.
     */
    @PostMapping("/friends/add")
    public String addFriendByUsername(HttpServletRequest request, @RequestParam String username) {
        try {
            int userId = ((Long) request.getAttribute("userId")).intValue();
            log.debug("User {} attempting to add friend: {}", userId, username);

            User currentUser = getUserOrThrow(userId);
            User friend = userDAO.findByUsername(username);

            if (friend == null) {
                return "User not found: " + username;
            }

            if (currentUser.equals(friend)) {
                return "Cannot add yourself as friend.";
            }

            boolean success = userService.addFriend(currentUser, friend);
            return success ? "Friend added." : "Already friends.";

        } catch (Exception e) {
            log.error("Failed to add friend: {}", e.getMessage(), e);
            return "Internal error: " + e.getMessage();
        }
    }

    /**
     * Returns the friend list for a given user.
     */
    @GetMapping("/friends")
    public List<Friendship> getFriends(@RequestParam int userId) {
        User user = getUserOrThrow(userId);
        return userService.getFriends(user);
    }

    /**
     * Returns the list of channels a user belongs to.
     */
    @GetMapping("/channels")
    public List<ChannelMembership> getChannels(@RequestParam int userId) {
        User user = getUserOrThrow(userId);
        return userService.getChannels(user);
    }

    /**
     * Returns all members of a specific channel by channel code.
     */
    @GetMapping("/channel/members")
    public List<User> getChannelMembers(HttpServletRequest request, @RequestParam String code) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not authenticated.");
        }
        return userService.getUsersInChannel(code);
    }

    /**
     * Returns sidebar data including friend rooms and channels for the authenticated user.
     */
    @GetMapping("/sidebar")
    public Map<String, Object> getSidebar(HttpServletRequest request) {
        int userId = ((Long) request.getAttribute("userId")).intValue();
        User user = getUserOrThrow(userId);

        List<Map<String, Object>> friends = userService.getFriendChatRooms(user).stream()
                .map(room -> Map.<String, Object>of(
                        "id", room.getId(),
                        "name", room.getDisplayNameFor(user)))
                .toList();

        List<Map<String, Object>> channels = userService.getChannels(user).stream()
                .map(cm -> Map.<String, Object>of(
                        "code", cm.getChannel().getCode(),
                        "name", cm.getChannel().getName()))
                .toList();

        return Map.of(
                "friends", friends,
                "channels", channels);
    }

    /**
     * Returns the profile of the currently authenticated user.
     */
    @GetMapping("/me")
    public User getMyProfile(HttpServletRequest request) {
        int userId = ((Long) request.getAttribute("userId")).intValue();
        try {
            return userDAO.findById(userId);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while fetching user by id: " + userId, e);
        }
    }

    /**
     * Updates the profile of the currently authenticated user.
     */
    @PatchMapping("/me")
    public User updateMyProfile(HttpServletRequest request, @RequestBody UserUpdateDTO dto) {
        int userId = ((Long) request.getAttribute("userId")).intValue();

        User user;
        try {
            user = userDAO.findById(userId);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Database error while fetching user by id: " + userId, e);
        }

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        return userDAO.save(user);
    }
}