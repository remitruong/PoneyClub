package fr.esieaproject.poneyclub.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.esieaproject.poneyclub.beans.Horse;
import fr.esieaproject.poneyclub.beans.Planning;
import fr.esieaproject.poneyclub.beans.User;

public interface PlanningRepository extends CrudRepository<Planning,Long> {

	List<Planning> findByHorse(Horse horse);
	List<Planning> findByRider(User user);

}
