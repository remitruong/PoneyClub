package fr.esieaproject.poneyclub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.exception.horseexceptions.HorseNotExistException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.services.HorseService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/horse")
public class HorseController {
	

	Logger logger = LoggerFactory.getLogger(HorseController.class);

	@Autowired
	private HorseService horseService;

	@Autowired
	private UserRepository userRepo;

	
	
	@GetMapping(value = "/get-horse/{name}")
	public ResponseEntity<?> getHorseByName(@PathVariable String name) {
		try {
			return new ResponseEntity<Horse>(horseService.getHorseByName(name), HttpStatus.OK);
		} catch (HorseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	} 

	@PostMapping(value = "/create-horse/{name}/{idAdmin}")
	public ResponseEntity<?> createHorse(@PathVariable String name, @PathVariable long idAdmin) throws NoUserFoundException {
		return new ResponseEntity<Horse>(this.horseService.createHorse(name), HttpStatus.OK);
	}

	@PostMapping(value = "/update-horse/{idHorse}/{name}/{idAdmin}")
	public ResponseEntity<?> updateHorse(@PathVariable Long idHorse, @PathVariable String name, @PathVariable Long idAdmin) {
		try {
			return new ResponseEntity<>(horseService.updateHorse(idHorse, name), HttpStatus.OK);
		} catch (HorseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/delete-horse/{idHorse}/{idAdmin}")
	public ResponseEntity<?> deleteHorse(@PathVariable Long idHorse, @PathVariable Long idAdmin) {
		try {
			return new ResponseEntity<>(this.horseService.deleteHorse(idHorse), HttpStatus.OK);
		} catch (HorseNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/list-horse")
	public ResponseEntity<?> getListHorse() {
		return new ResponseEntity<Iterable<Horse>>(this.horseService.getListHorse(), HttpStatus.OK);
	}

}
