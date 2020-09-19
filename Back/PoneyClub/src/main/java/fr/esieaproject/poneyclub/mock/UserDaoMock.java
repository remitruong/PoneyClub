package fr.esieaproject.poneyclub.mock;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserDao;

@Repository
public class UserDaoMock implements UserDao {
	
	public UserDaoMock() {}

	@Override
	public User createUser(String name, String lastName, String mail, String password, String mobile, String role) {
		User user = new User();
		
		user.setLastName(lastName);
		user.setName(name);
		user.setMail(mail);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setRole(role);
		
		return user;
	}
	
	

}
