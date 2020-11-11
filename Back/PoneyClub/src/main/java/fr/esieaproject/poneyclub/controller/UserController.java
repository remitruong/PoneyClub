package fr.esieaproject.poneyclub.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.ExceptionResponse;
import fr.esieaproject.poneyclub.exception.ExpiredTokenException;
import fr.esieaproject.poneyclub.exception.InvalidTokenException;
import fr.esieaproject.poneyclub.exception.userexceptions.EmailNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.MaxTrialConnectionAttempException;
import fr.esieaproject.poneyclub.exception.userexceptions.MobileNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.userexceptions.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongMobileOrEmailFormat;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongPasswordException;
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
	public ResponseEntity<?> updateUser(@PathVariable long idUser, @RequestBody User user) {
		MultiValueMap<String, String> headers = new HttpHeaders();
		user.setIdUser(idUser);
		String newToken = userService.updateUser(idUser, user);
		headers.add("Access-Control-Expose-Headers", "Authorization");
		headers.add("Access-Control-Allow-Headers", "Authorization");
		headers.add("Authorization", "Bearer " + newToken);
		return new ResponseEntity<>((MultiValueMap) headers, HttpStatus.OK);
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
		} catch (NoUserFoundException | MobileNotAvailableException | EmailNotAvailableException | WrongMobileOrEmailFormat e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value ="/get-teachers")
	public ResponseEntity<?> getTeachers() {
			return new ResponseEntity<>(userService.getTeachers(), HttpStatus.OK);
	}	
	
	@GetMapping(value ="/get-admins")
	public ResponseEntity<?> getAdmins() {
			return new ResponseEntity<>(userService.getAdmin(), HttpStatus.OK);
	}	

	@PostMapping(value ="/convert-to-admin/{idUser}/{adminMail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertToAdmin(@PathVariable long idUser, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.changeUserToAdmin(idUser, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="/forgot-password/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgot_password(@PathVariable String email) {
		try {
			return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
		} catch (NoUserFoundException | MessagingException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="/reset-password")
	public ResponseEntity<?> reset_password(@RequestParam(value="token") String token, @RequestBody String password) {
		try {
			return new ResponseEntity<>(userService.setNewPassword(token, password), HttpStatus.OK);
		} catch (InvalidTokenException | ExpiredTokenException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="/create-admin")
	public ResponseEntity<?> create_admin(@RequestBody User user) {
		try {
			return new ResponseEntity<>(userService.createAdmin(user), HttpStatus.OK);
		} catch (WrongMobileOrEmailFormat | MobileNotAvailableException | EmailNotAvailableException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
}
