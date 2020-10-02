package fr.esieaproject.poneyclub.services;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.exception.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.WrongPasswordException;


@Service
public class UserService {
	
	private Logger logger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	public boolean createUser(User user) {
		user.setRole("Rider");
		try {
	    	userRepo.save(user);
	    	return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}
	}
	
	public boolean updateUser(User user) {
		try {
	    	userRepo.save(user);
	    	return true;
    	} catch(Exception e) {
    		logger.error("" + e);
    		return false;
    	}
	}
	
	public User connect(User user) throws NoUserFoundException, WrongPasswordException {
		Optional<User> existingUser = userRepo.findByMail(user.getMail());
		
		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(user.getMobile());
			if (existingUser.isEmpty()) {
				throw new NoUserFoundException("No user found");
			}
		}
		
		if (existingUser.get().getPassword().equals(user.getPassword())) {
			return existingUser.get();
		} else {
			logger.error("Wrong password");
			throw new WrongPasswordException("Wrong password");
		}
	}
	
	public User getRiderByMail(String mailOrNumber, String adminMail) throws NoUserFoundException, UnauthorizeAccessException {

		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty()) {
			logger.error("Admin not found");
			throw new NoUserFoundException("No user found");
		}
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			throw new UnauthorizeAccessException("Access denied");
		}
		
		Optional<User> user = userRepo.findByMail(mailOrNumber);
		if (user.isEmpty()) {
			user = userRepo.findByMobile(mailOrNumber);
			if (user.isEmpty()) {
				logger.error("No user found");
				throw new NoUserFoundException("No user found");
			}
		}
		return user.get();
	}
	
	public Iterable<User> getRiders(String adminMail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		Iterable<User> userList = userRepo.findAll();
		return userList;
	}
	
	public boolean createTeacher(User teacher, String adminMail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		teacher.setRole("Teacher");
		userRepo.save(teacher);
		return true;
	}
	
	public boolean changeUserToAdmin(User user, String adminMail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByMail(adminMail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		user.setStatut("Admin");
		userRepo.save(user);
		return true;
	}

}
