package com.verda.BE.chat.repository;

import com.verda.BE.chat.entity.MessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
//    List<MessageEntity> findByRoomId(long roomId);
}
