package com.ckhub.websocket.config.user;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String nickName;
    private String fullName;

    // Status 는 열거형으로 상수를 만들어준다.
    private Status status;
}
