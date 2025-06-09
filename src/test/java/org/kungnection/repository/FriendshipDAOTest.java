package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FriendshipDAOTest {
    @Autowired
    private FriendshipDAO friendshipDAO;

    @Test
    void testFindFriendshipsByUserId() throws Exception {
        var friendships = friendshipDAO.findAllByUserId(1);
        assertThat(friendships).isNotNull();
    }
}
