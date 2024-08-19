package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.Message;

import java.util.List;

public interface MessageService {
    
    List<Message> findUnreadMessagesByUserId(Long userId);

    List<Message> findMessagesByUserId(Long userId);


}
