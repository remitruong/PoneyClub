package fr.esieaproject.poneyclub.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.courseexception.CourseNotExistException;
import fr.esieaproject.poneyclub.exception.courseexception.RecurrenceNotKnownException;
import fr.esieaproject.poneyclub.exception.courseexception.StartShouldBeBeforeEndException;
import fr.esieaproject.poneyclub.exception.courseexception.UserAlreadyRegisteredException;
import fr.esieaproject.poneyclub.exception.courseplaceexceptions.NoPlacesAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.userexceptions.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.services.CourseService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/course")
public class CourseController {

	Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService courseService;

	@GetMapping(value = "/get-courses")
	public ResponseEntity<?> findAll() {
		try {
			return new ResponseEntity<Iterable<Course>>(courseService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error has occured while retrieving courses", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/{startDateTime}/{endDateTime}")
	public ResponseEntity<?> findByDateTime(@PathVariable String startDateTime, @PathVariable String endDateTime) {
		try {
			return new ResponseEntity<List<Course>>(courseService.findByDateTime(startDateTime, endDateTime),
					HttpStatus.OK);
		} catch (StartShouldBeBeforeEndException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/find-course-by-teacher/{idTeacher}")
	public ResponseEntity<?> findByTeacher(@PathVariable long idTeacher) {
		try {
			return new ResponseEntity<List<Course>>(courseService.findByTeacher(idTeacher), HttpStatus.OK);
		} catch (NoUserFoundException | UnauthorizeAccessException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/plan/{idTeacher}")
	public ResponseEntity<?> addCourse(@RequestBody Course course, @PathVariable long idTeacher)
			throws NoUserFoundException {
		try {
			return new ResponseEntity<Course>(courseService.addCourse(course, idTeacher), HttpStatus.OK);
		} catch (StartShouldBeBeforeEndException | NoUserFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/plan/recurrent-course/{recurrence}")
	public ResponseEntity<?> addRecurrentCourse(@RequestBody Course course, @PathVariable String recurrence)
			throws NoUserFoundException {
		try {
			return new ResponseEntity<List<Course>>(courseService.addRecurrentCourse(course, recurrence),
					HttpStatus.OK);
		} catch (StartShouldBeBeforeEndException | RecurrenceNotKnownException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/register/{idCourse}")
	public ResponseEntity<?> register(@RequestBody User user, @PathVariable long idCourse) {
		try {
			return new ResponseEntity<CoursePlace>(courseService.registerToCourse(user, idCourse), HttpStatus.OK);
		} catch (CourseNotExistException | NoUserFoundException | NoPlacesAvailableException
				| UserAlreadyRegisteredException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/available-places/{idCourse}")
	public ResponseEntity<?> getAvailablePlaces(@PathVariable long idCourse) {
		try {
			return new ResponseEntity<Integer>(courseService.availablePlaces(idCourse), HttpStatus.OK);
		} catch (CourseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/update-course/{idCourse}")
	public ResponseEntity<?> update_course(@RequestBody Course course, @PathVariable long idCourse) {
		try {
			return new ResponseEntity<Course>(courseService.updateCourse(course, idCourse), HttpStatus.OK);
		} catch (CourseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/cancel-course/{idCourse}")
	public ResponseEntity<?> cancel_course(@RequestBody Course course, @PathVariable long idCourse) {
		try {
			return new ResponseEntity<Course>(courseService.cancelCourse(course, idCourse), HttpStatus.OK);
		} catch (CourseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
