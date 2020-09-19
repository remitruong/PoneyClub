package fr.esieaproject.poneyclub.dao;

import fr.esieaproject.poneyclub.beans.User;

public interface UserDao {
	
	public User createUser(String name, String lastName, String mail, String password, String mobile, String role);

}
