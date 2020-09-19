package fr.esieaproject.poneyclub.controller;

import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.exception.UndefinedUserIdException;

@RestController
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${welcome}")
	private String welcome;

	
	private UserRepository userRepo;
	public UserController(final UserRepository userRepository) {
		this.userRepo = userRepository;
	}

	@PostMapping(value = "/create-user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createRider(@RequestBody User user) {  
    	
    	try {
	    	userRepo.save(user);
	    	return true;
    	} catch(Exception e) {
    		logger.error(" --- " + e);
    		return false;
    	}
    }
	
	@PostMapping(value = "/update-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateRider(@PathVariable Long id, @RequestBody User user) throws UndefinedUserIdException {  
    	
		Optional<User> gUser = userRepo.findById(id);
		if (gUser.isEmpty()) {
			logger.error(" User not find ");
			throw new UndefinedUserIdException("User not find");
		} 
		
		user.setId(gUser.get().getId());
		
    	try {
	    	userRepo.save(user);
	    	return true;
    	} catch(Exception e) {
    		logger.error(" --- " + e);
    		return false;
    	}
    }
	

	@GetMapping(value = "/connect/{mail}/{password}")
	public User getUserByMail(@PathVariable String mail, @PathVariable String password) {
		
		Optional<User> user = userRepo.findByMail(mail);
		
		if (user.isEmpty()) {
			logger.error(" --- " + mail + "Does not exist");
			return null;
		}
		
		user = userRepo.connect(mail, password);
		
		if (user.isEmpty()) {
			logger.error(" --- " + mail + "Does not exist");
			return null;
		}
		
		try {
			return user.get();
		} catch (Exception e) {
			logger.error(" --- " + e);
			return null;
		}

	}
	
	
	

	@GetMapping(value = "/")
	public String ipaddress() throws Exception {
		return "Reply: " + welcome;
	}

}
