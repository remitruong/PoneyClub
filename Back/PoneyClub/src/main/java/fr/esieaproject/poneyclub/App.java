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
    		User root = new User("Root", "Root", "root@gmail.com", "changeit", "0606060605");
    		root.setStatut("Root");
    		root.setRole("Root");
    		
    		User admin = new User("Admin", "Admin", "admin@gmail.com", "changeit", "0606060606");
    		admin.setStatut("Admin");
    		admin.setRole("Admin");
    		
    		User rider = new User("Marc", "Douglass", "rider@gmail.com", "changeit", "0658963654");
    		User rider2 = new User("Jean", "Kent", "rider2@gmail.com", "changeit", "0635847698");
    		User rider3 = new User("Paul", "Dumont", "rider3@gmail.com", "changeit", "0678469852");
    		User teacher = new User("Jean", "Dupont ", "teacher@gmail.com", "changeit", "0658963652");
    		teacher.setRole("Teacher");
    		rider.setRole("Rider");
    		rider2.setRole("Rider");
    		rider3.setRole("Rider");

    		repository.save(root);
    		repository.save(admin);
    		repository.save(rider);
    		repository.save(rider2);
    		repository.save(rider3);
    		repository.save(teacher);
    		
    		logger.info(" ---------------------------- ");
    		logger.info(" " + repository.findAll());
    		logger.info(" ---------------------------- ");
    		
    		
    	};
    	
    }

}
