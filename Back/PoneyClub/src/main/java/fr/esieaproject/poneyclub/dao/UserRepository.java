package fr.esieaproject.poneyclub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.esieaproject.poneyclub.beans.User;


public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByMail(String mail);
	Optional<User> findByMobile(String mobile);
	List<User> findByRole(String role);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.mail) = LOWER(:mail) and u.password = :password")
    public Optional<User> connect(@Param("mail") String lastName, @Param("password") String password);
	
}
