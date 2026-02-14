package org.kungnection.controller;

import org.junit.jupiter.api.Test;
import org.kungnection.model.Channel;
import org.kungnection.model.ChannelMembership;
import org.kungnection.model.Friendship;
import org.kungnection.model.User;
import org.kungnection.security.JwtUtil;
import org.kungnection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.kungnection.repository.UserDAO;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDAO userDAO;

    @Test
    public void testCreateChannel() throws Exception {
        User user = new User(); user.setId(1);
        Channel channel = new Channel(); channel.setName("test");
        when(userDAO.findById(1)).thenReturn(user);
        when(userService.createChannel(eq(user), eq("test"))).thenReturn(channel);
        mockMvc.perform(post("/user/channels")
                .requestAttr("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"test\""))
                .andExpect(status().isOk());
    }

    @Test
    public void testJoinChannel() throws Exception {
        User user = new User(); user.setId(1);
        when(userDAO.findById(1)).thenReturn(user);
        when(userService.joinChannel(eq(user), eq("ABC123"))).thenReturn(true);
        mockMvc.perform(post("/user/channels/join")
                .requestAttr("userId", 1L)
                .param("code", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Joined successfully."));
    }

    @Test
    public void testAddFriend() throws Exception {
        User user = new User(); user.setId(1);
        User friend = new User(); friend.setId(2);
        when(userDAO.findById(1)).thenReturn(user);
        when(userDAO.findById(2)).thenReturn(friend);
        when(userService.addFriend(user, friend)).thenReturn(true);
        mockMvc.perform(post("/user/friends")
                .param("userId", "1")
                .param("friendId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend added."));
    }

    @Test
    public void testAddFriendByUsername() throws Exception {
        User user = new User(); user.setId(1);
        User friend = new User(); friend.setId(2); friend.setUsername("bob");
        when(userDAO.findById(1)).thenReturn(user);
        when(userDAO.findByUsername("bob")).thenReturn(friend);
        when(userService.addFriend(user, friend)).thenReturn(true);
        mockMvc.perform(post("/user/friends/add")
                .requestAttr("userId", 1L)
                .param("username", "bob"))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend added."));
    }

    @Test
    public void testGetFriends() throws Exception {
        User user = new User(); user.setId(1);
        Friendship f = new Friendship();
        when(userDAO.findById(1)).thenReturn(user);
        when(userService.getFriends(user)).thenReturn(List.of(f));
        mockMvc.perform(get("/user/friends").param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetChannels() throws Exception {
        User user = new User(); user.setId(1);
        ChannelMembership cm = new ChannelMembership();
        when(userDAO.findById(1)).thenReturn(user);
        when(userService.getChannels(user)).thenReturn(List.of(cm));
        mockMvc.perform(get("/user/channels").param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetChannelMembers() throws Exception {
        User user = new User(); user.setId(1);
        User member = new User(); member.setId(2);
        when(userService.getUsersInChannel("ABC123")).thenReturn(List.of(member));
        mockMvc.perform(get("/user/channel/members")
                .requestAttr("userId", 1L)
                .param("code", "ABC123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSidebar() throws Exception {
        User user = new User(); user.setId(1);
        when(userDAO.findById(1)).thenReturn(user);
        when(userService.getFriendChatRooms(user)).thenReturn(List.of());
        when(userService.getChannels(user)).thenReturn(List.of());
        mockMvc.perform(get("/user/sidebar").requestAttr("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friends").exists())
                .andExpect(jsonPath("$.channels").exists());
    }

    @Test
    public void testGetMyProfile() throws Exception {
        User user = new User(); user.setId(1);
        when(userDAO.findById(1)).thenReturn(user);
        mockMvc.perform(get("/user/me").requestAttr("userId", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMyProfile() throws Exception {
        User user = new User(); user.setId(1);
        User updated = new User(); updated.setId(1); updated.setUsername("newname");
        when(userDAO.findById(1)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(updated);
        String json = "{\"username\":\"newname\"}";
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/user/me")
                        .requestAttr("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
