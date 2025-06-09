package org.kungnection.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChannelMembershipDAOTest {
    @Autowired
    private ChannelMembershipDAO channelMembershipDAO;

    @Test
    void testFindMembershipsByUserId() throws Exception {
        var memberships = channelMembershipDAO.findAllByUserId(1);
        assertThat(memberships).isNotNull();
    }
}
