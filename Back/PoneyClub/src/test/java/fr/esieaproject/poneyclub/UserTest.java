package fr.esieaproject.poneyclub;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserRepository;

public class UserTest { 
	
	Logger logger = LoggerFactory.getLogger(UserTest.class);
	
	
	/**
	 * As a user you should subscribe to be a rider
	 */
	/*
	@Test
	public void userSubscribeAsRider() {
		User user = userDao.createUser("Toto", "Tata", "toto@gmail.com", "password", "0612345678", "Rider");
		assertNotNull(user);
	}
	
	@Test
	public void riderEditInformation() {
		User user = userDao.createUser("Toto", "Tata", "toto@gmail.com", "password", "0612345678", "Rider");
		
		try {
			user = userDao.updateUser(user, "Titi", "Tata", "toto@gmail.com", "0612345678");
		} catch (UndefinedUserIdException e) {
			e.printStackTrace();
		}
		
		assertEquals("Titi", user.getLastName());
		
	}
	*/
	

}
