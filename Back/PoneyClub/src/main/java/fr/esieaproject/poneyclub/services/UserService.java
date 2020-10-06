package fr.esieaproject.poneyclub.services;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esieaproject.poneyclub.beans.User;
import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.exception.EmailNotAvailableException;
import fr.esieaproject.poneyclub.exception.MobileNotAvailableException;
import fr.esieaproject.poneyclub.exception.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.WrongMobileOrEmailFormat;
import fr.esieaproject.poneyclub.exception.WrongPasswordException;
import fr.esieaproject.poneyclub.exception.MaxTrialConnectionAttempException;

@Service
public class UserService {

	private Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepo;

	public boolean createUser(User user)
			throws MobileNotAvailableException, EmailNotAvailableException, WrongMobileOrEmailFormat {
		if (emailAvailable(user.getEmail())) {
			if (mobileAvailable(user.getMobile())) {
				if (isEmailValid(user.getEmail()) && isMobileValid(user.getMobile())) {
					user.setRole("Rider");
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

	public boolean updateUser(User user) {
		logger.info("" + user.toString());
		try {
			userRepo.save(user);
			return true;
		} catch (Exception e) {
			logger.error("" + e);
			return false;
		}
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
			user.setPassword("");
		}
		return userList;
	}

	public boolean createTeacher(User teacher, String adminEmail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		teacher.setRole("Teacher");
		userRepo.save(teacher);
		return true;
	}

	public boolean changeUserToAdmin(User user, String adminEmail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		user.setStatut("Admin");
		userRepo.save(user);
		return true;
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
