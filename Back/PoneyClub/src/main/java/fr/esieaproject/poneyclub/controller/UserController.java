package fr.esieaproject.poneyclub.controller;

import java.util.List;
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

	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createRider(@RequestBody User user) { 
		
		user.setRole("rider");
    	
		try {
	    	userRepo.save(user);
	    	return true;
		} catch (Exception e) {
			logger.error("" + e);
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
		
		if (!user.getRole().equals("Rider")) {
			logger.error("Only rider can update his own profile");
			return false;
		}
		
    	try {
	    	userRepo.save(user);
	    	return true;
    	} catch(Exception e) {
    		logger.error(" --- " + e);
    		return false;
    	}
    }
	

	@PostMapping(value = "/connect/{mail}/{password}")
	public User connectUser(@RequestBody String mail, @RequestBody String password) {
		Optional<User> user = userRepo.findByMail(mail);
		
		if (user.isEmpty()) {
			logger.error(" --- " + mail + "Does not exist");
			return null;
		}
		
		user = userRepo.connect(mail, password);
		
		if (user.isEmpty()) {
			logger.error(" --- " + mail + "Wrong password");
			return null;
		}
		
		try {
			return user.get();
		} catch (Exception e) {
			logger.error(" --- " + e);
			return null;
		}
	}
	
	@GetMapping(value = "/get-rider/mail/{mail}/{adminMail}")
	public User getRiderByMail(@PathVariable String mail, @PathVariable String adminMail) {
		
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("User not found");
			return null;
		}
		
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			return null;
		}
		
		Optional<User> rider = userRepo.findByMail(mail);
		if (rider.isEmpty() || !rider.get().getRole().contentEquals("Rider")) {
			logger.error("No users for this mobile");
			return null;
		}
		
		return rider.get();
	}
	
	@GetMapping(value = "/get-rider/mobile/{mobile}/{adminMail}")
	public User getRiderByMobile(@PathVariable String mobile, @PathVariable String adminMail) {
		
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("User not found");
			return null;
		}
		
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			return null;
		}
		
		Optional<User> rider = userRepo.findByMail(mobile);
		
		
		if (rider.isEmpty() || !rider.get().getRole().contentEquals("Rider")) {
			logger.error("No users for this mobile");
			return null;
		}
		
		return rider.get();
	}
	
	@GetMapping(value ="/get-riders/{adminMail}")
	public List<User> getRiders(@PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("User not found");
			return null;
		}
		
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			return null;
		}
		
		List<User> userList = userRepo.findByRole("Rider");
		
		return userList;
	}
	
	@PostMapping(value ="/create-teacher/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean createTeacher(@RequestBody User user, @PathVariable String adminMail) {
		
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("User not found");
			return false;
		}
		
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			return false;
		}
		
		user.setRole("Teacher");
		userRepo.save(user);
		return true;
		
	}
	
	
	

	@GetMapping(value = "/")
	public String ipaddress() throws Exception {
		return "Reply: " + welcome;
	}

}
