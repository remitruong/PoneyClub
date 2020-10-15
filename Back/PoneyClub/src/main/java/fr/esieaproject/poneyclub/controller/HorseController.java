package fr.esieaproject.poneyclub.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/horse")
public class HorseController {
	

	Logger logger = LoggerFactory.getLogger(HorseController.class);

	@Autowired
	private HorseRepository horseRepo;

	@Autowired
	private UserRepository userRepo;

	
	
	@GetMapping(value = "/get-horse/{name}")
	public Horse getHorseByName(@PathVariable String name) {

		Optional<Horse> horse = horseRepo.findByName(name);

		if (horse.isEmpty()) {
			logger.error(name + " does not exists");
		}

		return horse.get();
	} 

	@PostMapping(value = "/create-horse/{name}/{idAdmin}")
	public Horse createHorse(@PathVariable String name, @PathVariable Long idAdmin) throws NoUserFoundException {

		Optional<User> admin = userRepo.findById(idAdmin);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			throw new NoUserFoundException("admin not found");
		}
		
		try {
			Horse horse = horseRepo.save(new Horse(name));
			return horse;
		} catch (Exception e) {
			logger.error("" + e);
			return null;
		}

	}

	@PostMapping(value = "/update-horse/{idHorse}/{name}/{idAdmin}")
	public boolean updateHorse(@PathVariable Long idHorse, @PathVariable String name, @PathVariable Long idAdmin) {

		Optional<User> admin = userRepo.findById(idAdmin);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			return false;
		}
		

		Optional<Horse> horse = horseRepo.findById(idHorse);

		if (horse.isEmpty()) {
			logger.error("Horse not found");
			return false;
		}

		try {
			horse.get().setName(name);
			horseRepo.save(horse.get());
			return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}

	}

	@DeleteMapping(value = "/delete-horse/{idHorse}/{idAdmin}")
	public boolean deleteHorse(@PathVariable Long idHorse, @PathVariable Long idAdmin) {

		Optional<User> admin = userRepo.findById(idAdmin);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			logger.error("Error while retrieving admin");
			return false;
		}
		
		try {
			horseRepo.deleteById(idHorse);
			return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}

	}
	
	@GetMapping(value = "/list-horse")
	public Iterable<Horse> getListHorse() {
		
		try {
		Iterable<Horse> listHorse = horseRepo.findAll();
		return listHorse;
		} catch (Exception e) {
			logger.error("Error while retrieving list horse");
			return null;
		}
		
	}

}
