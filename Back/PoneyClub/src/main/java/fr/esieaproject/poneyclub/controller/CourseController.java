package fr.esieaproject.poneyclub.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.dao.CoursePlaceRepository;
import fr.esieaproject.poneyclub.dao.CourseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.User;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value="/course")
public class CourseController {
	
	Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CourseRepository courseRepo; 
	
	@Autowired
	private CoursePlaceRepository coursePlaceRepo;
	
	@GetMapping(value = "/")
	public Iterable<Course> findAll() {
		return courseRepo.findAll();
	}
	
	@GetMapping(value = "/{startDateTime}/{endDateTime}")
	public List<Course> findByDateTime(@PathVariable String startDateTime, @PathVariable String endDateTime){
		
		
		List<Course> courseList = courseRepo.findByDateTime(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
		
		return courseList;
	}
	
	@PostMapping(value = "/plan/{idTeacher}")
	public boolean addCourse(@RequestBody Course course, @PathVariable Long idTeacher) {
		
		Optional<User> teacher = userRepo.findById(idTeacher);
		if (teacher.isEmpty() || !teacher.get().getRole().equals("Teacher")) {
			logger.error("Issue while retrieving teacher");
			return false;
		}
		course.setTeacher(teacher.get());
		Course course1 = courseRepo.save(course);

		int places = course1.getMaxStudent();
		for (int i=0; i<places; i++) {
			CoursePlace coursePlace = new CoursePlace();
			coursePlace.setCourse(course1);
			coursePlaceRepo.save(coursePlace);
		}
		
		return true;
	}
	
	@PostMapping(value = "/register/{idCourse}")
	public boolean register(@RequestBody User user, @PathVariable Long idCourse) {
		
		Optional<Course> course = courseRepo.findById(idCourse);
		if(course.isEmpty()) {
			logger.error("Unable to retrieve course");
			return false;
		}
		Optional<User> rider = userRepo.findByEmail(user.getEmail());
		CoursePlace planning = new CoursePlace(rider.get());
		coursePlaceRepo.save(planning);
		return true;
	}
	
	
}
