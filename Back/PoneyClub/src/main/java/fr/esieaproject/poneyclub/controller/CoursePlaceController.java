package fr.esieaproject.poneyclub.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.Course;
import fr.esieaproject.poneyclub.beans.CoursePlace;
import fr.esieaproject.poneyclub.beans.Horse;
import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.CoursePlaceRepository;
import fr.esieaproject.poneyclub.dao.CourseRepository;
import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value ="/place")
public class CoursePlaceController {
	
	private final Logger logger = LogManager.getLogger(CoursePlaceController.class);

	@Autowired
	private HorseRepository horseRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired 
	private CourseRepository courseRepo;
	
	@Autowired
	private CoursePlaceRepository coursePlaceRepo;
	
	@GetMapping(value = "/")
	public Iterable<CoursePlace> getCoursePlaces() {
		
		return coursePlaceRepo.findAll();
		
	}
	
	@GetMapping(value = "/horse-planning/{name}")
	public List<CoursePlace> getHorsePlanning(@PathVariable String name) {
		
		Optional<Horse> horse = horseRepo.findByName(name);
		
		List<CoursePlace> horsePlanning = coursePlaceRepo.findByHorse(horse.get());
		return horsePlanning;
		
	}
	
	@GetMapping(value = "/user-planning/{mailOrNumber}")
	public List<CoursePlace> getUserPlanning(@PathVariable String mailOrNumber) {
		
		Optional<User> existingUser = userRepo.findByEmail(mailOrNumber);
		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(mailOrNumber);
			if (existingUser.isEmpty()) {
				logger.error("No user found");
				return null;
			}
		}
		
		List<CoursePlace> userPlanning = coursePlaceRepo.findByRider(existingUser.get());
		return userPlanning;
		
	}
	
	@PostMapping(value = "/addhorse/{horseName}/{idTeacher}/{idCourse}")
	public boolean addHorse(@PathVariable String horseName, @PathVariable Long idTeacher, @PathVariable Long idCourse) {
		Optional<Course> course = courseRepo.findById(idCourse);
		if(course.isEmpty()) {
			logger.error("Unable to retrieve course");
			return false;
		}
		
		Optional<User> user = userRepo.findById(idTeacher);
		if (user.isEmpty() || !user.get().getRole().equals("Teacher")) {
			logger.error("Issue while retrieving teacher");
			return false;
		}
		
		Optional<Horse> horse = horseRepo.findByName(horseName);
		if (horse.isEmpty()) {
			logger.error("This horse seems to not exists");
			return false;
		}
		
		// Optional<User> user = planningRepo.find
		
		return true;
		
	}

}
