package com.ckhub.websocket.config.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user){
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user){
        var storedUser = repository.findById(user.getNickName())
                .orElse(null);
        if(storedUser != null){
            storedUser
                    .setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }
    
    /* 사용자 목록 */
    public List<User> findConnectedUsers(){

        return repository.findAllByStatus(Status.ONLINE);
    }

}