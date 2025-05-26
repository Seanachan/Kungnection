package org.kungnection.repository;

import org.kungnection.model.Channel;
import org.kungnection.model.ChannelMembership;
import org.kungnection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, Long> {
    boolean existsByUserAndChannel(User user, Channel channel);
    List<ChannelMembership> findAllByUser(User user);
}