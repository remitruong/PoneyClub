package fr.esieaproject.poneyclub.controller;

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

	@PostMapping(value = "/create-rider", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> createRider(@RequestBody User user) {
		boolean bool = userService.createUser(user);
		if (bool) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/update-user", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> updateRider(@RequestBody User user) {
		boolean bool = userService.updateUser(user);
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
		} catch (NoUserFoundException | WrongPasswordException e) {
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
	public ResponseEntity<?> getRiders(@PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.getRiders(adminMail), HttpStatus.OK);
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

	@PostMapping(value ="/create-admin/{adminMail}", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAdmin(@RequestBody User user, @PathVariable String adminMail) {
		try {
			return new ResponseEntity<>(userService.changeUserToAdmin(user, adminMail), HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
