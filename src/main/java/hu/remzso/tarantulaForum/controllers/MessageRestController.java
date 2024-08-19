package hu.remzso.tarantulaForum.controllers;

import java.util.List;

import javax.transaction.Transactional;

import hu.remzso.tarantulaForum.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.remzso.tarantulaForum.entities.Message;
import hu.remzso.tarantulaForum.repositories.UserRepository;

@RestController
@Transactional
public class MessageRestController {
	private final UserRepository userRepository;
	private final MessageService messageService;

	public MessageRestController(UserRepository userRepository, MessageService messageService) {
		this.userRepository = userRepository;
        this.messageService = messageService;
    }


	@GetMapping("/messages/unread-count")
	public ResponseEntity<Integer> getUnreadMessageCount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long userId = userRepository.findByUsername(authentication.getName()).getId();
		return ResponseEntity.ok(messageService.findUnreadMessagesByUserId(userId).size());

	}
	
	@GetMapping("/messages")
	public ResponseEntity<List<Message>> getMessages() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long userId = userRepository.findByUsername(authentication.getName()).getId();
		return ResponseEntity.ok(messageService.findMessagesByUserId(userId));
	}
}
