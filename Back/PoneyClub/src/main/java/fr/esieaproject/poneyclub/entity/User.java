package fr.esieaproject.poneyclub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idUser;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String email;
	private String password;
	@Column(unique = true)
	private String mobile;
	private String licenceNum;
	private String role;
	private String statut = "User";
	private int trialConnection = 0;
	
	public User() {
	}

	public User(String firstName, String lastName, String email, String password, String mobile) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		setPassword(password);
		this.mobile = mobile;
	}

	public User(String firstName, String lastName, String email, String password, String mobile, String licenceNum) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		setPassword(password);
		this.mobile = mobile;
		this.licenceNum = licenceNum;
	}

	public User(String email, String password) {
		super();
		this.email = email;
		setPassword(password);
	}

	public Long getId() {
		return idUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getMobile() {
		return mobile;
	}

	public String getLicenceNum() {
		return licenceNum;
	}

	public String getRole() {
		return role;
	}

	public String getStatut() {
		return statut;
	}
	
	public int getTrialConnection() {
		return trialConnection;
	}
	
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.password = encoder.encode(password);
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setLicenceNum(String licenceNum) {
		this.licenceNum = licenceNum;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public void setTrialConnection(int trialConnection) {
		this.trialConnection = trialConnection;
	}

	@Override
	public String toString() {
		return "User [id=" + idUser + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + ", mobile=" + mobile + ", licenceNum=" + licenceNum + ", role=" + role + ", statut="
				+ statut + "]";
	}

}
