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
    		User user = new User("Admin", "Root", "admin@gmail.com", "changeit", "0606060606");
    		user.setStatut("Admin");
    		user.setRole("Admin");
    		
    		repository.save(user);
    		
    		logger.info(" ---------------------------- ");
    		logger.info(" " + repository.findAll());
    		logger.info(" ---------------------------- ");
    		
    		
    	};
    	
    }

}
