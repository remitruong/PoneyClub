package fr.esieaproject.poneyclub.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fr.esieaproject.poneyclub.entity.PasswordResetToken;

public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long >{

	Optional<PasswordResetToken> findByToken(String token);

}
