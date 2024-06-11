package hu.remzso.tarantulaForum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.remzso.tarantulaForum.entities.User;
import hu.remzso.tarantulaForum.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public String saveUser(User user) {
		return userRepository.save(user).getFirstName();
		
	}

}
