package fr.esieaproject.poneyclub.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.User;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private UserRepository userRepository;
	private String username;

	public JwtAuthenticationFailureHandler(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		Optional<User> isUserExist = userRepository.findByEmail(this.username);
		if (isUserExist.isEmpty())
			isUserExist = userRepository.findByMobile(this.username);
		int trialConnections = 0;
		if (isUserExist.isPresent())
			trialConnections = isUserExist.get().getTrialConnection();

		if (isUserExist.isPresent()) {
			trialConnections++;
			isUserExist.get().setTrialConnection(trialConnections);
			userRepository.save(isUserExist.get());
		}

		response.setStatus(401);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

}
