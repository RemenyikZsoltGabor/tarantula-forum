package hu.remzso.tarantulaForum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.remzso.tarantulaForum.entities.User;
import hu.remzso.tarantulaForum.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			System.out.println("ex");
			throw new UsernameNotFoundException("User not found");
		}
		System.out.println("jó");
		return new UserDetailsImpl(user);
	}

}
