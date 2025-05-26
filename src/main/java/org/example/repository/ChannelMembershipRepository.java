package com.kungnection.app.repository;

import com.kungnection.app.model.Channel;
import com.kungnection.app.model.ChannelMembership;
import com.kungnection.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, Long> {
    boolean existsByUserAndChannel(User user, Channel channel);
    List<ChannelMembership> findAllByUser(User user);
}