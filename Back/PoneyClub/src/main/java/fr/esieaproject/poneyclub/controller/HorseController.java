package fr.esieaproject.poneyclub.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.Horse;
import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;

@RestController
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
	public boolean createHorse(@PathVariable String name, @PathVariable Long idAdmin) {

		Optional<User> user = userRepo.findById(idAdmin);

		if (user.isEmpty()) {
			logger.error("Admin not found ");
			return false;
		}
		
		if (!user.get().getStatut().equals("Admin")) {
			logger.error("Only Admin can create horse");
			return false;
		}
			

		try {
			horseRepo.save(new Horse(name));
			return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}

	}

	@PostMapping(value = "/update-horse/{idHorse}/{name}/{idAdmin}")
	public boolean updateHorse(@PathVariable Long idHorse, @PathVariable String name, @PathVariable Long idAdmin) {

		Optional<User> user = userRepo.findById(idAdmin);

		if (user.isEmpty() || !user.get().getStatut().equals("User")) {
			logger.error("Only Admin can update horse");
			return false;
		}

		Optional<Horse> horse = horseRepo.findById(idHorse);

		if (horse.isEmpty()) {
			logger.error("Horse not found");
			return false;
		}

		try {
			horseRepo.save(horse.get());
			return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}

	}

	@DeleteMapping(value = "/delete-horse/{idHorse}/{idAdmin}")
	public boolean deleteHorse(@PathVariable Long idHorse, @PathVariable Long idAdmin) {

		Optional<User> user = userRepo.findById(idAdmin);

		if (user.isEmpty() || !user.get().getStatut().equals("User")) {
			logger.error("Only Admin can delete horse");
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
	
	@GetMapping(value = "/list-horse/{idAdmin}")
	public Iterable<Horse> getListHorse(@PathVariable Long idAdmin) {

		Optional<User> user = userRepo.findById(idAdmin);

		if (user.isEmpty() || !user.get().getStatut().equals("User")) {
			logger.error("Only Admin can get list horses");
			return null;
		}
		
		try {
		Iterable<Horse> listHorse = horseRepo.findAll();
		return listHorse;
		} catch (Exception e) {
			logger.error("Error while retrieving list horse");
			return null;
		}
		
	}

}
