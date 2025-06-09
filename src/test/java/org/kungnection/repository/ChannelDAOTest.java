package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChannelDAOTest {
    @Autowired
    private ChannelDAO channelDAO;

    @Test
    void testFindAllChannels() throws Exception {
        var channels = channelDAO.findAll();
        assertThat(channels).isNotNull();
    }
}
