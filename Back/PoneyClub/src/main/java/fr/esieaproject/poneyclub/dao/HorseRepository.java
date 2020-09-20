package fr.esieaproject.poneyclub.dao;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;

import fr.esieaproject.poneyclub.beans.Horse;

public interface HorseRepository extends CrudRepository<Horse, Long>{
	
	Optional<Horse> findByName(String name);
	
}
