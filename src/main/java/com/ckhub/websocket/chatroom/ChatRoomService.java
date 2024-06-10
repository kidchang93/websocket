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
    public Optional<String> getChatRoomId (String senderId,
                                           String recipientId,
                                           boolean createNewRoomIfNotExists){
        // 없으면 새 방을 만들 것이므로 람다 표현식을 써서 만들어 준다.
        // 여기서 발신자 ID 수신자 ID를 찾으려고 하지만 이는 채팅방의 옵션을 반환하므로
        // 매핑해야한다.
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(()-> {
                    if (createNewRoomIfNotExists){
                        var chatId = createChatId(senderId,recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });


    }

    // 위에서 쓸 메서드를 만든다.
    private String createChatId(String senderId, String recipientId) {
        // 채팅 아이디에 대한 형식을 정해 변수에 담는다.
        var chatId = String.format("%s_%s", senderId,recipientId); // ck_ckhub

        // 각 채팅방의 발신자와 수신자ID를 전환하여 보낸 사람과 받는 사람을 위한 두개의 채팅방을 만듭니다.
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}
