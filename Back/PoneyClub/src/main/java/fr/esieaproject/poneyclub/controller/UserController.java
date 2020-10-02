package fr.esieaproject.poneyclub.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.exception.ExceptionResponse;
import fr.esieaproject.poneyclub.exception.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.WrongPasswordException;
import fr.esieaproject.poneyclub.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createRider(@RequestBody User user) {
		Iterable<User> userList = userRepo.findAll();
		try{
			for (User u: userList) {
				if(u.getName().equals(user.getName())){
					return true;
					//TODO A faire avec la requete HTTP pour envoyer l'erreur

					//					return error('Username or password is incorrect');

				}else
				{
					user.setRole("Rider");
					userRepo.save(user);
				}
			}
			return false;
		}catch(Exception e){
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
	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createRider(@RequestBody User user) { 
		boolean bool = userService.createUser(user);
		if (bool) {
			return new ResponseEntity<Boolean>(bool, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(bool, HttpStatus.BAD_REQUEST);
		}
    }
	
	@PostMapping(value = "/update-user", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateRider(@RequestBody User user) {  
		boolean bool = userService.updateUser(user);
		if (bool) {
			return new ResponseEntity<Boolean>(bool, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(bool, HttpStatus.BAD_REQUEST);
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
	@PostMapping(value = "/connect", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> connectUser(@RequestBody User user) {
		try {
			user = userService.connect(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoUserFoundException | WrongPasswordException e) {
			return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/get-user/{mailOrNumber}/{adminMail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRiderByMail(@PathVariable String mailOrNumber, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<User>(userService.getRiderByMail(mailOrNumber, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException | UnauthorizeAccessException e) {
			return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value ="/get-users/{adminMail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRiders(@PathVariable String adminMail) {
		try {
			return new ResponseEntity<Iterable<User>>(userService.getRiders(adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="/create-teacher/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTeacher(@RequestBody User teacher, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<Boolean>(userService.createTeacher(teacher, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="/create-admin/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAdmin(@RequestBody User user, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<Boolean>(userService.changeUserToAdmin(user, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

}
