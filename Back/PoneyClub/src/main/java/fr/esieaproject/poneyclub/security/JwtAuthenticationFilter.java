package fr.esieaproject.poneyclub.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.model.LoginViewModel;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private JwtAuthenticationFailureHandler authenticationFailureHandler = null;

	private final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.setFilterProcessesUrl("/login");
		this .authenticationFailureHandler = new JwtAuthenticationFailureHandler(userRepository);
		super.setAuthenticationFailureHandler(authenticationFailureHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		HttpServletRequest cloneRequest = request;

		LoginViewModel credentials = null;
		try {
			credentials = new ObjectMapper().readValue(cloneRequest.getInputStream(), LoginViewModel.class);
			this.authenticationFailureHandler.setUsername(credentials.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Optional<User> isUserExist = userRepository.findByEmail(credentials.getUsername());
		if (isUserExist.isEmpty())
			isUserExist = userRepository.findByMobile(credentials.getUsername());
		int trialConnections = 0;
		if (isUserExist.isPresent())
			trialConnections = isUserExist.get().getTrialConnection();

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				credentials.getUsername(), credentials.getPassword(), new ArrayList<>());

		Authentication auth = null;

		if (trialConnections >=  3) {
			auth = authenticationManager.authenticate(authenticationToken);
		} else {
			response.addHeader("access-control-expose-headers", "MaxTrialConnection");
			response.addHeader("MaxTrialConnection", "true");
			try {
				response.sendError(401);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		


		if (isUserExist.isPresent()) {
			trialConnections = isUserExist.get().getTrialConnection();
			trialConnections = 0;
			isUserExist.get().setTrialConnection(trialConnections);
			userRepository.save(isUserExist.get());
		}
		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

		String token = JWT.create().withSubject(principal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(HMAC512(JwtProperties.SECRET.getBytes()));

		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
		ObjectMapper mapper = new ObjectMapper();
		String userJson = mapper.writeValueAsString(principal.getUser());
		response.setContentType("application/json");
		response.setHeader("Access-Control-Expose-Headers", "Authorization");
		response.getWriter().write(userJson);
	}

	public boolean updatePrincipal(User userToUpdate, User updatedUser) {

		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username.equals(userToUpdate.getEmail())) {
			if (!updatedUser.getEmail().equals(userToUpdate.getEmail())) {
				Authentication authenticationToken = new UsernamePasswordAuthenticationToken(updatedUser.getEmail(),
						updatedUser.getPassword(), new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			return true;
		} else if (username.equals(userToUpdate.getMobile())) {
			if (!updatedUser.getMobile().equals(userToUpdate.getEmail())) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						updatedUser.getMobile(), updatedUser.getPassword(), new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			return true;
		} else {
			return false;
		}
	}

}
