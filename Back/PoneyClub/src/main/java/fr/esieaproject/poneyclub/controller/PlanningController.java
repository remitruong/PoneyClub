package fr.esieaproject.poneyclub.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.Horse;
import fr.esieaproject.poneyclub.beans.Planning;
import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.PlanningRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;


@RestController
public class PlanningController {
	
	Logger logger = LoggerFactory.getLogger(PlanningController.class);
	
	@Autowired
	private HorseRepository horseRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PlanningRepository planningRepo; 

	
	@GetMapping(value = "/planning")
	public Iterable<Planning> getPlanning() {
		
		return planningRepo.findAll();
		
	}
	
	@GetMapping(value = "/horse-planning/{id}")
	public List<Planning> getHorsePlanning(@PathVariable Long id) {
		
		Optional<Horse> horse = horseRepo.findById(id);
		
		List<Planning> horsePlanning = planningRepo.findByHorse(horse.get());
		return horsePlanning;
		
	}
	
	@GetMapping(value = "/user-planning/{id}")
	public List<Planning> getUserPlanning(@PathVariable Long id) {
		
		Optional<User> user = userRepo.findById(id);
		
		List<Planning> userPlanning = planningRepo.findByRider(user.get());
		return userPlanning;
		
	}
	
	
	@PostMapping(value = "/plan/{idTeacher}")
	public boolean plan(@RequestBody Planning planning, @PathVariable Long idTeacher) {
		
		Optional<User> user = userRepo.findById(idTeacher);

		if (user.isEmpty()) {
			logger.error("Admin not found ");
			return false;
		}
		
		if (!user.get().getRole().equals("Rider")) {
			logger.error("Only Teacher can plan anything");
			return false;
		}
		
		
		Optional<Horse> horse = horseRepo.findByName(planning.getHorse().getName());
		Optional<User> rider = userRepo.findByMail(planning.getRider().getMail());
		
		if ( horse.isEmpty() || rider.isEmpty()) {
			logger.error("You need a horse and a user to plan something");
			return false;
		}
		
		planningRepo.save(planning);
		return true;
		
	}
}
