package fr.esieaproject.poneyclub.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import fr.esieaproject.poneyclub.dao.HorseRepository;
import fr.esieaproject.poneyclub.entity.Horse;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.horseexceptions.HorseNotExistException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;

@Service
public class HorseService {
	
	@Autowired
	HorseRepository horseRepo;
	
	public Horse getHorseByName(String name) throws HorseNotExistException {

		Optional<Horse> horse = horseRepo.findByName(name);

		if (horse.isEmpty()) {
			throw new HorseNotExistException("Horse " + name + "not found");
		}
		return horse.get();
	}
	
	public Horse createHorse(String name) throws NoUserFoundException {
		Horse horse = horseRepo.save(new Horse(name));
		return horse;
	}
	
	
	public boolean updateHorse(Long idHorse, String name) throws HorseNotExistException {
		Optional<Horse> horse = horseRepo.findById(idHorse);
		if (horse.isEmpty()) {
			throw new HorseNotExistException("Horse " + name + "not found");
		}
		horse.get().setName(name);
		horseRepo.save(horse.get());
		return true;
	}
	
	public boolean deleteHorse(Long idHorse) throws HorseNotExistException {
		Optional<Horse> horseExist = this.horseRepo.findById(idHorse);
		if (horseExist.isEmpty()) throw new HorseNotExistException("Horse not found");
		
		horseRepo.deleteById(idHorse);
		return true;
	}
	
	public Iterable<Horse> getListHorse() {
		Iterable<Horse> listHorse = horseRepo.findAll();
		return listHorse;
	}
	
	
}
