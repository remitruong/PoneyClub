package fr.esieaproject.poneyclub.services;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

import fr.esieaproject.poneyclub.dao.PasswordTokenRepository;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.PasswordResetToken;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.ExpiredTokenException;
import fr.esieaproject.poneyclub.exception.InvalidTokenException;
import fr.esieaproject.poneyclub.exception.userexceptions.EmailNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.MaxTrialConnectionAttempException;
import fr.esieaproject.poneyclub.exception.userexceptions.MobileNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.userexceptions.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongMobileOrEmailFormat;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongPasswordException;
import fr.esieaproject.poneyclub.security.JwtAuthenticationFilter;
import fr.esieaproject.poneyclub.security.JwtProperties;
import fr.esieaproject.poneyclub.security.SecurityConfiguration;
import fr.esieaproject.poneyclub.security.UserPrincipal;

@Service
public class UserService {

	private Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private PasswordTokenRepository passwordTokenRepository;
	
	@Autowired
	SecurityConfiguration securityConfiguration;

	public boolean createUser(User user)
			throws MobileNotAvailableException, EmailNotAvailableException, WrongMobileOrEmailFormat {
		if (emailAvailable(user.getEmail())) {
			if (mobileAvailable(user.getMobile())) {
				if (isEmailValid(user.getEmail()) && isMobileValid(user.getMobile())) {
					user.setRole("Rider");
					user.setStatut("User");
					userRepo.save(user);
					return true;
				} else {
					throw new WrongMobileOrEmailFormat("Email or Mobile is wrong");
				}
			} else {
				throw new MobileNotAvailableException("This mobile number is already used");
			}
		} else {
			throw new EmailNotAvailableException("This email is already used");
		}
	}

	public String updateUser(long idUser, User user) {
		
		String newToken = null;
		Optional<User> optionalUserToUpdate = userRepo.findById(idUser);
		User userToUpdate = null;
		if (optionalUserToUpdate.isPresent()) {
			userToUpdate = optionalUserToUpdate.get();
		}
	
		JwtAuthenticationFilter authFilter = securityConfiguration.getAuthFilter();
		if (authFilter.updatePrincipal(userToUpdate, user)) {
		
		UsernamePasswordAuthenticationToken userPrincipal = 
				(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String username = (String) userPrincipal.getPrincipal();
		
		newToken = JWT.create().withSubject(username)
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(HMAC512(JwtProperties.SECRET.getBytes())); 
		}
		
		userRepo.save(user);
		return newToken;
	}
	
	//TODO Gérer l'exception No value present (quand aucun champs n'est rempli)
	public User connect(User user) throws NoUserFoundException, WrongPasswordException, MaxTrialConnectionAttempException {
		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

		if (existingUser.isEmpty()) {
			existingUser = userRepo.findByMobile(user.getMobile());
			if (existingUser.isEmpty()) {
				throw new NoUserFoundException("No user found");
			}
		}

		int trialConnection = existingUser.get().getTrialConnection();
		if (trialConnection < 4) {
			if (existingUser.get().getPassword().equals(user.getPassword())) {
				return existingUser.get();
			} else {
				logger.error("Wrong password");
				trialConnection++;
				existingUser.get().setTrialConnection(trialConnection);
				userRepo.save(existingUser.get());
				throw new WrongPasswordException("Wrong password");
			}
		} else {
			throw new MaxTrialConnectionAttempException("You already had fail 3 attempt to connect");
		}
	}

	public User getRiderByMail(String emailOrNumber, String adminEmail)
			throws NoUserFoundException, UnauthorizeAccessException {

		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty()) {
			logger.error("Admin not found");
			throw new NoUserFoundException("No user found");
		}
		if (admin.get().getStatut().equals("User")) {
			logger.error("Only Admin access");
			throw new UnauthorizeAccessException("Access denied");
		}

		Optional<User> user = userRepo.findByEmail(emailOrNumber);
		if (user.isEmpty()) {
			user = userRepo.findByMobile(emailOrNumber);
			if (user.isEmpty()) {
				logger.error("No user found");
				throw new NoUserFoundException("No user found");
			}
		}
		return user.get();
	}

	public Iterable<User> getUsers(String adminEmail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		Iterable<User> userList = userRepo.findAll();
		for (User user : userList) {
//			user.setPassword("");
			logger.info("user list :" + user.toString());
		}
		return userList;
	}

	public User createTeacher(User teacher, String adminEmail) throws NoUserFoundException, MobileNotAvailableException, EmailNotAvailableException, WrongMobileOrEmailFormat {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		if (emailAvailable(teacher.getEmail())) {
			if (mobileAvailable(teacher.getMobile())) {
				if (isEmailValid(teacher.getEmail()) && isMobileValid(teacher.getMobile())) {
					teacher.setStatut("User");
					teacher.setRole("Teacher");
					teacher.setTrialConnection(0);
					teacher = userRepo.save(teacher);
					return teacher;
				} else {
					throw new WrongMobileOrEmailFormat("Email or Mobile is wrong");
				}
			} else {
				throw new MobileNotAvailableException("This mobile number is already used");
			}
		} else {
			throw new EmailNotAvailableException("This email is already used");
		}
	}
	
	public List<User> getTeachers() {
		return userRepo.findByRole("Teacher");
	}

	public boolean changeUserToAdmin(long idUser, String adminEmail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		Optional<User> user = userRepo.findById(idUser);
		if (user.isEmpty() || user.get().getStatut().equals("Admin")) {
			throw new NoUserFoundException("admin not found");
		}
		user.get().setStatut("Admin");
//		user.get().setRole("Admin");
		userRepo.save(user.get());
		return true;
	}
	
	public boolean createAdmin(User user) throws WrongMobileOrEmailFormat, MobileNotAvailableException, EmailNotAvailableException {
		if (emailAvailable(user.getEmail())) {
			if (mobileAvailable(user.getMobile())) {
				if (isEmailValid(user.getEmail()) && isMobileValid(user.getMobile())) {
					user.setRole("Admin");
					user.setStatut("Admin");
					userRepo.save(user);
					return true;
				} else {
					throw new WrongMobileOrEmailFormat("Email or Mobile is wrong");
				}
			} else {
				throw new MobileNotAvailableException("This mobile number is already used");
			}
		} else {
			throw new EmailNotAvailableException("This email is already used");
		}
	}
	
	public boolean forgotPassword(String email) throws NoUserFoundException, MessagingException {
		
		Optional<User> user = this.userRepo.findByEmail(email);
		
		if (user.isEmpty()) throw new NoUserFoundException("No user was found with this email, please retry or subscribe");
		
		MimeMessage message = this.emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
		
		helper.setTo(email);
		helper.setSubject("You have forgotten your password !");
		
		String token = UUID.randomUUID().toString();
		this.createPasswordResetTokenForUser(user.get(), token);
		String htmlResponse = "Hello " + user.get().getFirstName() + " " + user.get().getLastName() + "\n"
				+ "\n\n"
				+ "You have forgotten your password ! You can set a new one by clicking the below link ! \n"
				+ "http://localhost:4200/reset-password?token=" + token + "\n"
				+ "Please do not share your ids with someone else \n"
				+ "PoneyClub technical team";
		helper.setText(htmlResponse);
		
		this.emailSender.send(message);
		return true;
	}	
	
	public boolean setNewPassword(String token, String password) throws InvalidTokenException, ExpiredTokenException {
		
		Optional<PasswordResetToken> passwordResetToken = this.passwordTokenRepository.findByToken(token);
		
		if(passwordResetToken.isEmpty()) throw new InvalidTokenException("Invalid token !");
		if(tokenIsExpired(passwordResetToken.get())) throw new ExpiredTokenException("Your token is expired, please retry !");
		
		User user = passwordResetToken.get().getUser();
		user.setPassword(password);
		this.userRepo.save(user);
		return true;

	}
	
	private boolean tokenIsExpired(PasswordResetToken passwordResetToken) {
		final Calendar cal = Calendar.getInstance();
	    return passwordResetToken.getExpiryDate().before(cal.getTime());
	}

	private void createPasswordResetTokenForUser(User user, String token) {
	    PasswordResetToken myToken = new PasswordResetToken(token, user);
	    passwordTokenRepository.save(myToken);
	}
	

	private boolean isEmailValid(String email) {
		Pattern pattern = Pattern.compile(
				"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
				Pattern.CASE_INSENSITIVE);
		return pattern.matcher(email).matches();
	}

	private boolean isMobileValid(String mobile) {
		Pattern pattern = Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
		return pattern.matcher(mobile).matches();
	}

	private boolean emailAvailable(String email) {
		Optional<User> existingUser = userRepo.findByEmail(email);
		if (existingUser.isEmpty())
			return true;
		else
			return false;
	}

	private boolean mobileAvailable(String mobile) {
		Optional<User> existingUser = userRepo.findByEmail(mobile);
		if (existingUser.isEmpty())
			return true;
		else
			return false;
	}

}
