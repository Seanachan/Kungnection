package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserDAOTest {
    @Autowired
    private UserDAO userDAO;

    @Test
    void testFindUserById() throws Exception {
        var user = userDAO.findById(1);
        assertThat(user).isNotNull();
    }
}
