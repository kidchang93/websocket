package com.ckhub.websocket.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    // ChatRoom 객체여야함
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
