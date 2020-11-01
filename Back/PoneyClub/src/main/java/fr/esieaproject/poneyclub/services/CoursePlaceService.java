package fr.esieaproject.poneyclub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esieaproject.poneyclub.dao.CoursePlaceRepository;
import fr.esieaproject.poneyclub.dao.CourseRepository;
import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.courseexception.CourseNotExistException;
import fr.esieaproject.poneyclub.exception.courseplaceexceptions.CoursePlaceNotFoundException;
import fr.esieaproject.poneyclub.exception.horseexceptions.HorseNotExistException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;

@Service
public class CoursePlaceService {

	@Autowired
	private HorseRepository horseRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	private CoursePlaceRepository coursePlaceRepo;

	public Iterable<CoursePlace> getCoursePlaces() {
		return coursePlaceRepo.findAll();
	}

	public List<CoursePlace> getHorsePlanning(String horseName) throws HorseNotExistException {
		Optional<Horse> horse = horseRepo.findByName(horseName);
		if (horse.isEmpty())
			throw new HorseNotExistException(horseName + " seems not existing");

		List<CoursePlace> horsePlanning = coursePlaceRepo.findByHorse(horse.get());
		return horsePlanning;
	}

	public List<CoursePlace> getUserPlanning(String mailOrNumber) throws NoUserFoundException {
		Optional<User> existingUser = userRepo.findByEmail(mailOrNumber);
		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(mailOrNumber);
			if (existingUser.isEmpty()) {
				throw new NoUserFoundException(mailOrNumber + " seems not existing");
			}
		}

		List<CoursePlace> userPlanning = coursePlaceRepo.findByRider(existingUser.get());
		return userPlanning;
	}
	
	public List<CoursePlace> getTeacherCoursePlaces(long teacherId, long courseId) throws NoUserFoundException, CourseNotExistException {
		Optional<User> existingTeacher = userRepo.findById(teacherId);
		Optional<Course> existingCourse = courseRepo.findById(courseId);
		
		if (existingTeacher.isEmpty()) throw new NoUserFoundException("Teacher not found");
		if (existingCourse.isEmpty()) throw new CourseNotExistException("Course not found");

		List<CoursePlace> teacherPlanning = coursePlaceRepo.findByTeacher(existingTeacher.get(), existingCourse.get());
		return teacherPlanning;
	}
	
	
	public boolean unsubscribe(long idCoursePlace) throws CoursePlaceNotFoundException {
		Optional<CoursePlace> isCoursePlace = coursePlaceRepo.findById(idCoursePlace);
		
		if (isCoursePlace.isEmpty()) throw new CoursePlaceNotFoundException("Course place was not found");
		isCoursePlace.get().setRider(null);
		isCoursePlace.get().setHorse(null);
		coursePlaceRepo.save(isCoursePlace.get());
		return true;
	}

	public boolean mapHorse(String horseName, long idTeacher, long idCoursePlace)
			throws HorseNotExistException, NoUserFoundException, CourseNotExistException {
		
		Optional<CoursePlace> coursePlace = coursePlaceRepo.findById(idCoursePlace);
		Optional<User> user = userRepo.findById(idTeacher);
		Optional<Horse> horse = horseRepo.findByName(horseName);
		
		if (coursePlace.isEmpty()) throw new CourseNotExistException("Unable retrieving course place");
		if (user.isEmpty() || !user.get().getRole().equals("Teacher")) throw new NoUserFoundException("Error while retrieving teacher");
		if (horse.isEmpty()) throw new HorseNotExistException("Error while retrieving horse");
		
		coursePlace.get().setHorse(horse.get());
		coursePlaceRepo.save(coursePlace.get());

		return true;
	}

}
