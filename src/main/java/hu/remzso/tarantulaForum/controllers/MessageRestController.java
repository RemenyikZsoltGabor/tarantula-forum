package hu.remzso.tarantulaForum.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.remzso.tarantulaForum.entities.Message;
import hu.remzso.tarantulaForum.repositories.MessageRepository;
import hu.remzso.tarantulaForum.repositories.UserRepository;

@RestController
@Transactional
public class MessageRestController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MessageRepository messageRepository;
	

	@GetMapping("/messages/unread-count")
	public ResponseEntity<Integer> getunreadMessageCount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long id = userRepository.findByUsername(authentication.getName()).getId();
		List<Message> unreadMessages = messageRepository.findUnreadMessagesByUserId(id);

		return ResponseEntity.ok(unreadMessages.size());
	}
	
	@GetMapping("/messages")
	public ResponseEntity<List<Message>> getMessages() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long id = userRepository.findByUsername(authentication.getName()).getId();
		List<Message> messages = messageRepository.findMessagesByUserId(id);
	
		return ResponseEntity.ok(messages);
	}
}
