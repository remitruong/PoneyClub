package fr.esieaproject.poneyclub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fr.esieaproject.poneyclub.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
	Optional<User> findByMobile(String mobile);
	List<User> findByRole(String role);

	@Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:mail) and u.password = :password")
    Optional<User> connect(@Param("mail") String lastName, @Param("password") String password);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:username) OR u.mobile = :username")
	User findByUsername(@Param("username") String username);
	
	@Transactional
	@Modifying
	@Query("Update User u SET u.email = LOWER(:email), u.mobile = :mobile, u.licenceNum = :licenceNum where u.id = :id")
	void updateUserInformations(@Param("email") String email, @Param("mobile") String mobile, @Param("licenceNum") String licenceNum ,@Param("id") long id);
	
	List<User> findByStatut(String string);
	
}
