package fr.esieaproject.poneyclub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.entity.User;

@Repository
public interface CoursePlaceRepository extends CrudRepository<CoursePlace, Long> {
	
	List<CoursePlace> findByRider(User rider);
	List<CoursePlace> findByHorse(Horse horse);
	
	@Query("select cp from CoursePlace cp, Course c where cp.course = c and c.teacher = :teacher and cp.course= :course and cp.rider IS NOT NULL")
	List<CoursePlace> findByTeacher(@Param("teacher") User teacher, @Param("course") Course course);

}
