package fr.esieaproject.poneyclub.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.esieaproject.poneyclub.beans.Course;

public interface CourseRepository extends CrudRepository<Course,Long> {

	@Query("SELECT c FROM Course c WHERE c.startDateTime BETWEEN :startDateTime and  :endDateTime")
	List<Course> findByDateTime( @Param("startDateTime") Timestamp startDateTime, @Param("endDateTime") Timestamp endDateTime);

}
