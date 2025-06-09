package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MessageDAOTest {
    @Autowired
    private MessageDAO messageDAO;

    @Test
    void testFindMessagesByChannelId() throws Exception {
        var messages = messageDAO.findByChannelId(1);
        assertThat(messages).isNotNull();
    }
}
