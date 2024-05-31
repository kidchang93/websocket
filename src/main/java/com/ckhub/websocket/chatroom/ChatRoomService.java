package com.ckhub.websocket.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방을 가져오는 메서드
    // 플래그 - 존재하지 않는 경우 새방 만들기를 호출하는 변수
    public Optional<String> getChatRoomId (String senderId, String recipientId, boolean createNewRoomIfNotExists){
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);


    }
}
