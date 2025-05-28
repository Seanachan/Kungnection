package org.kungnection.repository;

import org.kungnection.model.Channel;
import org.kungnection.model.ChannelMembership;
import org.kungnection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, Long> {

    /**
     * 檢查某個使用者是否已經加入某個頻道
     */
    boolean existsByUserAndChannel(User user, Channel channel);

    /**
     * 查詢使用者所加入的所有頻道
     */
    List<ChannelMembership> findAllByUser(User user);

    /**
     * 查詢某個頻道中所有的成員關係（含 user 資訊）
     */
    List<ChannelMembership> findAllByChannel(Channel channel);
}