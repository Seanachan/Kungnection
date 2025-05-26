package com.kungnection.app.repository;

import com.kungnection.app.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}