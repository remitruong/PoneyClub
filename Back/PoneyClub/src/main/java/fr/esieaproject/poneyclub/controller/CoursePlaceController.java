package fr.esieaproject.poneyclub.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.exception.courseexception.CourseNotExistException;
import fr.esieaproject.poneyclub.exception.courseplaceexceptions.CoursePlaceNotFoundException;
import fr.esieaproject.poneyclub.exception.horseexceptions.HorseNotExistException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.services.CoursePlaceService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/place")
public class CoursePlaceController {

	private final Logger logger = LogManager.getLogger(CoursePlaceController.class);

	@Autowired
	CoursePlaceService coursePlaceService;

	@GetMapping(value = "/")
	public ResponseEntity getCoursePlaces() {
		try {
			return new ResponseEntity<Iterable<CoursePlace>>(coursePlaceService.getCoursePlaces(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/horse-planning/{horseName}")
	public ResponseEntity getHorsePlanning(@PathVariable String horseName) {
		try {
			return new ResponseEntity<List<CoursePlace>>(coursePlaceService.getHorsePlanning(horseName), HttpStatus.OK);
		} catch (HorseNotExistException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/user-planning/{mailOrNumber}")
	public ResponseEntity getUserPlanning(@PathVariable String mailOrNumber) {
		try {
			return new ResponseEntity<List<CoursePlace>>(coursePlaceService.getUserPlanning(mailOrNumber),
					HttpStatus.OK);
		} catch (NoUserFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/teacher-course-places/{teacherId}/{courseId}")
	public ResponseEntity getTeacherCourse(@PathVariable long teacherId, @PathVariable long courseId) {
		try {
			return new ResponseEntity<List<CoursePlace>>(coursePlaceService.getTeacherCoursePlaces(teacherId, courseId),
					HttpStatus.OK);
		} catch (NoUserFoundException | CourseNotExistException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/user-planning/unsubscribe/{idCoursePlace}")
	public ResponseEntity unsubscribe(@PathVariable long idCoursePlace) {
		try {
			return new ResponseEntity(coursePlaceService.unsubscribe(idCoursePlace), HttpStatus.OK);
		} catch (CoursePlaceNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/addhorse/{horseName}/{idTeacher}/{idCoursePlace}")
	public ResponseEntity mapHorse(@PathVariable String horseName, @PathVariable long idTeacher,
			@PathVariable long idCoursePlace) {
		try {
			return new ResponseEntity(coursePlaceService.mapHorse(horseName, idTeacher, idCoursePlace), HttpStatus.OK);
		} catch (NoUserFoundException | HorseNotExistException | CourseNotExistException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
