package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.User;

public interface UserService {

	String saveUser(User user);
	User getSender(String username);
}
