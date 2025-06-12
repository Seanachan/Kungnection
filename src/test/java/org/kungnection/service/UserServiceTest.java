package org.kungnection.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kungnection.model.*;
import org.kungnection.repository.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private ChannelDAO channelDAO;
    @Mock
    private ChannelMembershipDAO channelMembershipDAO;
    @Mock
    private FriendshipDAO friendshipDAO;
    @Mock
    private FriendChatRoomDAO friendChatRoomDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        User user = new User();
        when(userDAO.save(user)).thenReturn(user);
        assertEquals(user, userService.register(user));
    }

    @Test
    public void testLoginSuccess() throws SQLException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("pass");
        when(userDAO.findByEmail("test@example.com")).thenReturn(user);
        assertEquals(user, userService.login("test@example.com", "pass"));
    }

    @Test
    public void testLoginFail() throws SQLException {
        when(userDAO.findByEmail("test@example.com")).thenReturn(null);
        assertNull(userService.login("test@example.com", "pass"));
    }

    @Test
    public void testCreateChannel() throws SQLException {
        User user = new User();
        user.setId(1);
        Channel channel = new Channel();
        channel.setName("Test Channel");
        doNothing().when(channelDAO).save(any(Channel.class));
        doNothing().when(channelMembershipDAO).save(any(ChannelMembership.class));
        Channel created = userService.createChannel(user, "Test Channel");
        assertEquals("Test Channel", created.getName());
        assertNotNull(created.getCode());
    }

    @Test
    public void testJoinChannelSuccess() throws SQLException {
        User user = new User();
        user.setId(1);
        Channel channel = new Channel();
        channel.setId(2);
        when(channelDAO.findByCode("CODE")).thenReturn(channel);
        when(channelMembershipDAO.existsByUserAndChannel(1, 2)).thenReturn(false);
        doNothing().when(channelMembershipDAO).save(any(ChannelMembership.class));
        assertTrue(userService.joinChannel(user, "CODE"));
    }

    @Test
    public void testJoinChannelAlreadyMember() throws SQLException {
        User user = new User();
        user.setId(1);
        Channel channel = new Channel();
        channel.setId(2);
        when(channelDAO.findByCode("CODE")).thenReturn(channel);
        when(channelMembershipDAO.existsByUserAndChannel(1, 2)).thenReturn(true);
        assertFalse(userService.joinChannel(user, "CODE"));
    }

    @Test
    public void testJoinChannelNotFound() throws SQLException {
        User user = new User();
        user.setId(1);
        when(channelDAO.findByCode("CODE")).thenReturn(null);
        assertFalse(userService.joinChannel(user, "CODE"));
    }

    @Test
    public void testGetChannels() throws SQLException {
        User user = new User();
        user.setId(1);
        List<ChannelMembership> memberships = List.of(new ChannelMembership());
        when(channelMembershipDAO.findAllByUserId(1)).thenReturn(memberships);
        assertEquals(memberships, userService.getChannels(user));
    }

    @Test
    public void testGetUsersInChannel() throws SQLException {
        Channel channel = new Channel();
        channel.setId(2);
        channel.setCode("CODE");
        User user = new User();
        ChannelMembership membership = new ChannelMembership();
        membership.setUser(user);
        when(channelDAO.findByCode("CODE")).thenReturn(channel);
        when(channelMembershipDAO.findAllByChannelId(2)).thenReturn(List.of(membership));
        List<User> users = userService.getUsersInChannel("CODE");
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void testAddFriendSuccess() throws SQLException {
        User user = new User(); user.setId(1);
        User target = new User(); target.setId(2);
        when(friendshipDAO.exists(1, 2)).thenReturn(false);
        doNothing().when(friendshipDAO).save(any(Friendship.class));
        doNothing().when(friendChatRoomDAO).save(any(FriendChatRoom.class));
        assertTrue(userService.addFriend(user, target));
    }

    @Test
    public void testAddFriendAlreadyExists() throws SQLException {
        User user = new User(); user.setId(1);
        User target = new User(); target.setId(2);
        when(friendshipDAO.exists(1, 2)).thenReturn(true);
        assertFalse(userService.addFriend(user, target));
    }

    @Test
    public void testAddFriendSelf() {
        User user = new User(); user.setId(1);
        assertFalse(userService.addFriend(user, user));
    }

    @Test
    public void testGetFriends() throws SQLException {
        User user = new User(); user.setId(1);
        List<Friendship> friends = List.of(new Friendship());
        when(friendshipDAO.findAllByUserId(1)).thenReturn(friends);
        assertEquals(friends, userService.getFriends(user));
    }

    @Test
    public void testGetFriendChatRooms() {
        User user = new User(); user.setId(1);
        List<FriendChatRoom> rooms = List.of(new FriendChatRoom());
        when(friendChatRoomDAO.findAllByUserId(1)).thenReturn(rooms);
        assertEquals(rooms, userService.getFriendChatRooms(user));
    }
}
