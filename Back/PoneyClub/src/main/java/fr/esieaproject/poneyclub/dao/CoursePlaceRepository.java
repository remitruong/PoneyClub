package fr.esieaproject.poneyclub.dao;

import java.util.List;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.esieaproject.poneyclub.entity.CoursePlace;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.entity.User;

@Repository
public interface CoursePlaceRepository extends CrudRepository<CoursePlace, Long> {
	
	List<CoursePlace> findByRider(User rider);
	List<CoursePlace> findByHorse(Horse horse);

}
