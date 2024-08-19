package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.Message;
import hu.remzso.tarantulaForum.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> findUnreadMessagesByUserId(Long userId) {
        return messageRepository.findUnreadMessagesByUserId(userId);

    }

    @Override
    public List<Message> findMessagesByUserId(Long userId) {
        return messageRepository.findMessagesByUserId(userId);

    }
}
