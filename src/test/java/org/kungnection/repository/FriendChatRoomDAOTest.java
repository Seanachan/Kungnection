package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FriendChatRoomDAOTest {
    @Autowired
    private FriendChatRoomDAO friendChatRoomDAO;

    @Test
    void testFindChatRoomsByUserId() {
        var rooms = friendChatRoomDAO.findAllByUserId(1);
        assertThat(rooms).isNotNull();
    }
}
