package com.ckhub.websocket.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    // 우리가 허용하는 객체
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;


    // 사용자를 위한 새 대기열을 생성하는 메세지 매핑
    // 그렇지 않으면 queue 에 메세지를 게시.
    // 메세지 매핑 꼭 추가
    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ){
        // 채팅 메세지를 제대로 전달하려면 신호를 보내거나
        // 메세지나 객체를 특정 사용자의 대기열에 게시하거나 메세지를 처리하기 때문에
        // 수신자ID의 대기열이 필요. 발신자로부터 수신자에게 메세지를 보내야함.
        ChatMessage saveMsg = chatMessageService.save(chatMessage);
        // 메세지를 대기열에 보내려면 여기에서 메세지를 저장한 후
        // 메세징 템플릿을 작성, 사용자 대기열에 Topic이나 메세지를 보내고 있으므로
        // 변환하여 사용자에게 보냅니다.
        // john/queue/messages
        // 사용자 문자열과 문자열 대상, 객체 페이로드를 문자열로 지정.
        // 수신자에게 메세지를 보냄.
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                ChatNotification.builder()
                        .id(saveMsg.getId())// 수정해야됨.
                        .senderId(saveMsg.getSenderId())
                        .recipientId(saveMsg.getRecipientId())
                        .content(saveMsg.getContent())
                        .build()
        );

    }




    // 채팅 메세지를 조회해서 리스트를 찾아오는 컨트롤러
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable("senderId") String senderId,
                                                              @PathVariable("recipientId") String recipientId){

        log.info("findChatMessages");
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId,recipientId));

    }

}
