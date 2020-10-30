package fr.esieaproject.poneyclub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.User;


@SpringBootApplication
public class App {
	
	private Logger logger = LoggerFactory.getLogger(App.class);
	
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    CommandLineRunner init(UserRepository repository) { 
    	return args -> {
    		User admin = new User("Admin", "Root", "admin@gmail.com", "changeit", "0606060606");
    		admin.setStatut("Admin");
    		admin.setRole("Admin");
    		
    		User rider = new User("Marc", "Douglass", "rider@gmail.com", "changeit", "0658963654");
    		User teacher = new User("Jean", "Dupont ", "teacher@gmail.com", "changeit", "0658963652");
    		teacher.setRole("Teacher");
    		
    		
    		repository.save(admin);
    		repository.save(rider);
    		repository.save(teacher);
    		
    		logger.info(" ---------------------------- ");
    		logger.info(" " + repository.findAll());
    		logger.info(" ---------------------------- ");
    		
    		
    	};
    	
    }

}
