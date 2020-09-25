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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.beans.Horse;
import fr.esieaproject.poneyclub.beans.Planning;
import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.dao.PlanningRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;


@RestController
@RequestMapping(value="/planning")
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
	
	
	@PostMapping(value = "/register/{idPlanning}")
	public boolean register(@RequestBody User rider, @PathVariable Long idPlanning) {
		
		Optional<Planning> planning = planningRepo.findById(idPlanning);
		if(planning.isEmpty()) {
			logger.error("Unable to retrieve planning");
			return false;
		}
		
		planning.get().addRider(rider);
		planningRepo.save(planning.get());
		return true;
	}
	
	
	//TODO LONK HORSES WITH RIDERS
	@PostMapping(value = "/addhorse/{horseName}/{idTeacher}/{idPlanning}")
	public boolean addHorse(@PathVariable String horseName, @PathVariable Long idTeacher, @PathVariable Long idPlanning) {
		Optional<Planning> planning = planningRepo.findById(idPlanning);
		if(planning.isEmpty()) {
			logger.error("Unable to retrieve planning");
			return false;
		}
		
		Optional<User> user = userRepo.findById(idTeacher);
		if (user.isEmpty() || !user.get().getRole().equals("Teacher")) {
			logger.error("Issue while retrieving teacher");
			return false;
		}
		
		Optional<Horse> horse = horseRepo.findByName(horseName);
		if (horse.isEmpty()) {
			logger.error("This horse seems to not exists");
			return false;
		}
		
		
		planning.get().addHorse(horse.get());
		planningRepo.save(planning.get());
		return true;
		
	}
}
