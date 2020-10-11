package fr.esieaproject.poneyclub.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esieaproject.poneyclub.dao.CoursePlaceRepository;
import fr.esieaproject.poneyclub.dao.CourseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.courseexception.CourseNotExistException;
import fr.esieaproject.poneyclub.exception.courseexception.StartShouldBeBeforeEndException;
import fr.esieaproject.poneyclub.exception.courseplaceexceptions.NoPlacesAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;

@Service
public class CourseService {
	
	private Logger logger = LogManager.getLogger(CourseService.class);

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CourseRepository courseRepo; 
	
	@Autowired
	private CoursePlaceRepository coursePlaceRepo;
	
	
	public Iterable<Course> findAll() {
		return courseRepo.findAll();
	}
	
	public List<Course> findByDateTime(String startDateTime, String endDateTime) throws StartShouldBeBeforeEndException {
		
		if (!this.isStartBeforeEnd(startDateTime, endDateTime)) throw new StartShouldBeBeforeEndException("Provided start time should be before end time");

		return courseRepo.findByDateTime(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
	}
	
	
	public Course addCourse(Course course, long idTeacher) throws NoUserFoundException, StartShouldBeBeforeEndException {

		Optional<User> teacher = userRepo.findById(idTeacher);
		if (teacher.isEmpty() || !teacher.get().getRole().equals("Teacher")) throw new NoUserFoundException("Teacher not found");
		if (!this.isStartBeforeEnd(course.getStartDateTime(), course.getEndDateTime())) throw new StartShouldBeBeforeEndException("Provided start time should be before end time");
		
		course.setTeacher(teacher.get());
		Course course1 = courseRepo.save(course);

		int places = course1.getMaxStudent();
		for (int i=0; i<places; i++) {
			CoursePlace coursePlace = new CoursePlace();
			coursePlace.setCourse(course1);
			coursePlaceRepo.save(coursePlace);
		}
		
		return course1;
	}
	
	public CoursePlace registerToCourse(User user, long idCourse) throws CourseNotExistException, NoUserFoundException, NoPlacesAvailableException {
		Optional<Course> course = courseRepo.findById(idCourse);
		Optional<User> rider = userRepo.findByEmail(user.getEmail());
		
		if (course.isEmpty()) throw new CourseNotExistException("Course does not exist, try later");
		if (rider.isEmpty() || !rider.get().getRole().equals("Rider")) throw new NoUserFoundException("Error while retrieving user");
		
		List<CoursePlace> coursePlace = courseRepo.findFirstAvailablePlace(course.get());
		
		if (coursePlace.isEmpty()) throw new NoPlacesAvailableException("There is no place for this course");
		
		coursePlace.get(0).setRider(rider.get());
		CoursePlace newCoursePlace = coursePlaceRepo.save(coursePlace.get(0));
		return newCoursePlace;
	}
	
	public Integer availablePlaces(long idCourse) throws CourseNotExistException {
		Optional<Course> isCourse = courseRepo.findById(idCourse);
		
		if(isCourse.isEmpty()) throw new CourseNotExistException("Course not found");
		
		return courseRepo.getCountAvailablePlaces(isCourse.get());
	}
		
	private boolean isStartBeforeEnd(String startDateTime, String endDateTime) {
		Timestamp start = Timestamp.valueOf(startDateTime);
		Timestamp end = Timestamp.valueOf(endDateTime);
		
		if (end.before(start)) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isAnyPlaceInCourse() {
		return true;
	}
	
	

}
