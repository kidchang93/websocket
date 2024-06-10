package com.ckhub.websocket.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;


    // 사용자가 입장되었다고 모든 유저에게 말해야됨.
    // 요청의 객체 또는 본문이라고 말하기 위해 웹소켓에서는 이를 Payload 라고 부릅니다.
    @MessageMapping("/user.addUser")    // 나중에 프론트엔드에서 이 Queue 를 구독해야 하기때문에 사용자를 반환한다.
    @SendTo("/user/public")
    public User addUser(@Payload User user) {
        service.saveUser(user);
        log.info("addUser: {}", user);
        return user;
    }

    // 끊어진 사용자를 또 모두에게 알려준다.
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnect(@Payload User user) {
        service.disconnect(user);
        return user;

    }


    // 사용자 목록을 반환한다.
    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers(){
        return ResponseEntity.ok(service.findConnectedUsers());

    }


}
