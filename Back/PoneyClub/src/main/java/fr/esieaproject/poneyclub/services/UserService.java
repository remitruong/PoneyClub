package fr.esieaproject.poneyclub.services;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esieaproject.poneyclub.dao.UserRepository;
import fr.esieaproject.poneyclub.entity.User;
import fr.esieaproject.poneyclub.exception.userexceptions.EmailNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.MaxTrialConnectionAttempException;
import fr.esieaproject.poneyclub.exception.userexceptions.MobileNotAvailableException;
import fr.esieaproject.poneyclub.exception.userexceptions.NoUserFoundException;
import fr.esieaproject.poneyclub.exception.userexceptions.UnauthorizeAccessException;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongMobileOrEmailFormat;
import fr.esieaproject.poneyclub.exception.userexceptions.WrongPasswordException;

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

	public boolean updateUser(long idUser, User user) {
		Optional<User> userToUpdate = userRepo.findById(idUser);
		user.setIdUser(idUser);
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
//			user.setPassword("");
			logger.info("user list :" + user.toString());
		}
		return userList;
	}

	public User createTeacher(User teacher, String adminEmail) throws NoUserFoundException {
		Optional<User> admin = userRepo.findByEmail(adminEmail);
		if (admin.isEmpty() || admin.get().getStatut().equals("User")) {
			throw new NoUserFoundException("admin not found");
		}
		teacher.setStatut("User");
		teacher.setRole("Teacher");
		teacher.setTrialConnection(0);
		teacher = userRepo.save(teacher);
		return teacher;
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
