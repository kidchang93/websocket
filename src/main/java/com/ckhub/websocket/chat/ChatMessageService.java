package com.ckhub.websocket.chat;

import com.ckhub.websocket.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    // 수신자ID와 발신자ID로 메세지를 찾는 또 다른 방법이 필요.
    // chatRoomService 에서 getChatRoomId 메서드를 사용.
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true        // 플래그
        ).orElseThrow();    // 그게 아니면 던진다. 원하는 예외를 정할 수 있다.
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(
            String senderId, String recipientId
    ){

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
