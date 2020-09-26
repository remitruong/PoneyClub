package fr.esieaproject.poneyclub.controller;

import java.util.List;


import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserRepository;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepo;

	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createRider(@RequestBody User user) { 
		
		user.setRole("Rider");
		try {
	    	userRepo.save(user);
	    	return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}
    	
    }
	
	@PostMapping(value = "/update-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateRider(@PathVariable Long id, @RequestBody User user) {  
    	
    	try {
	    	userRepo.save(user);
	    	return true;
    	} catch(Exception e) {
    		logger.error(" --- " + e);
    		return false;
    	}
    }
	

	@PostMapping(value = "/connect", consumes = MediaType.APPLICATION_JSON_VALUE)
	public User connectUser(@RequestBody User user) {
		Optional<User> existingUser = userRepo.findByMail(user.getMail());
		
		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(user.getMobile());
			if (existingUser.isEmpty()) {
				logger.error("No user found");
				return null;
			}
		}
		
		if (existingUser.get().getPassword().equals(user.getPassword())) {
			return existingUser.get();
		} else {
			logger.error(" ---   Wrong password");
			return null;
		}
	}
	
	@GetMapping(value = "/get-user/{mailOrNumber}/{adminMail}")
	public User getRiderByMail(@PathVariable String mailOrNumber, @PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("User not found");
			return null;
		}
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			return null;
		}
		
		Optional<User> user = userRepo.findByMail(mailOrNumber);
		if (user.isEmpty()) {
			user = userRepo.findByMobile(mailOrNumber);
			if (user.isEmpty()) {
				logger.error("No user found");
				return null;
			}
		}
		
		return user.get();
	}
	
	@GetMapping(value ="/get-users/{adminMail}")
	public Iterable<User> getRiders(@PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			return null;
		}
		
		
		Iterable<User> userList = userRepo.findAll();
		
		return userList;
	}
	
	@PostMapping(value ="/create-teacher/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean createTeacher(@RequestBody User user, @PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			return false;
		}
		
		
		user.setRole("Teacher");
		userRepo.save(user);
		return true;
		
	}
	
	@PostMapping(value ="/create-admin/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean createAdmin(@RequestBody User user, @PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			return false;
		}
		
		
		user.setRole("Teacher");
		userRepo.save(user);
		return true;
		
	}
}
