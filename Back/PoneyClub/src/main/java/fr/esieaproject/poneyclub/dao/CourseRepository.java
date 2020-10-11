package fr.esieaproject.poneyclub.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.esieaproject.poneyclub.entity.Course;
import fr.esieaproject.poneyclub.entity.CoursePlace;

public interface CourseRepository extends CrudRepository<Course,Long> {

	@Query("SELECT c FROM Course c WHERE c.startDateTime BETWEEN :startDateTime and  :endDateTime")
	List<Course> findByDateTime( @Param("startDateTime") Timestamp startDateTime, @Param("endDateTime") Timestamp endDateTime);
	
	@Query("SELECT count(*) FROM CoursePlace cp where cp.course= :course AND cp.rider IS NULL")
	Integer getCountAvailablePlaces(@Param("course") Course course);
	
	@Query("SELECT cp FROM CoursePlace cp where cp.course= :course AND cp.rider IS NULL")
	List<CoursePlace> findFirstAvailablePlace(@Param("course") Course course);

}
