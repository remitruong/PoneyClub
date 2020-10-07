package fr.esieaproject.poneyclub.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.EmailNotAvailableException;
import fr.esieaproject.poneyclub.exception.ExceptionResponse;
import fr.esieaproject.poneyclub.exception.MobileNotAvailableException;
import fr.esieaproject.poneyclub.exception.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.WrongMobileOrEmailFormat;
import fr.esieaproject.poneyclub.exception.WrongPasswordException;
import fr.esieaproject.poneyclub.exception.MaxTrialConnectionAttempException;
import fr.esieaproject.poneyclub.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	private final Logger logger = LogManager.getLogger(UserController.class);

	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createRider(@RequestBody User user) {
		try {
			userService.createUser(user);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (MobileNotAvailableException | EmailNotAvailableException | WrongMobileOrEmailFormat e) {
			logger.error("" + e);
			return new ResponseEntity(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/update-user/{idUser}", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateRider(@PathVariable long idUser, @RequestBody User user) {
		System.out.println(user.toString());
		boolean bool = userService.updateUser(idUser, user);
		if (bool) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping(value = "/connect", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> connectUser(@RequestBody User user) {
		try {
			user = userService.connect(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (NoUserFoundException | WrongPasswordException | MaxTrialConnectionAttempException e) {
			logger.error("" + e);
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/get-user/{mailOrNumber}/{adminMail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRiderByMail(@PathVariable String mailOrNumber, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.getRiderByMail(mailOrNumber, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException | UnauthorizeAccessException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value ="/get-users/{adminMail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUsers(@PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.getUsers(adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value ="/create-teacher/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTeacher(@RequestBody User teacher, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.createTeacher(teacher, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value ="/convert-to-admin/{idUser}/{adminMail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertToAdmin(@PathVariable long idUser, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.changeUserToAdmin(idUser, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
