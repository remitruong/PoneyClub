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
	
	@GetMapping(value = "/horse-planning/{name}")
	public List<Planning> getHorsePlanning(@PathVariable String name) {
		
		Optional<Horse> horse = horseRepo.findByName(name);
		
		List<Planning> horsePlanning = planningRepo.findByHorse(horse.get());
		return horsePlanning;
		
	}
	
	@GetMapping(value = "/user-planning/{mailOrNumber}")
	public List<Planning> getUserPlanning(@PathVariable String mailOrNumber) {
		
		Optional<User> existingUser = userRepo.findByMail(mailOrNumber);
		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(mailOrNumber);
			if (existingUser.isEmpty()) {
				logger.error("No user found");
				return null;
			}
		}
		
		
		List<Planning> userPlanning = planningRepo.findByRider(existingUser.get());
		return userPlanning;
		
	}
	
	
	@PostMapping(value = "/plan/{idTeacher}")
	public boolean plan(@RequestBody Planning planning, @PathVariable Long idTeacher) {
		
		Optional<User> user = userRepo.findById(idTeacher);

		if (user.isEmpty() || !user.get().getRole().equals("Teacher")) {
			logger.error("Issue while retrieving teacher");
			return false;
		}
		
		planningRepo.save(planning);
		return true;
		
	}
}
