package fr.esieaproject.poneyclub.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String lastName;
	@Column(unique = true)
	private String mail;
	private String password;
	@Column(unique = true)
	private String mobile;
	private String role;
	private String statut = "User";
	
	public User() {}
	
	public User(String name, String lastName, String mail, String password, String mobile) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
		this.password = password;
		this.mobile = mobile;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public String getMobile() {
		return mobile;
	}
	
	public String getRole() {
		return role;
	}
	
	public String getStatut() {
		return statut;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public void setRole(String role) {
		this.role=role;
	}
	
	public void setStatut(String statut) {
		this.statut = statut;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastName=" + lastName + ", mail=" + mail + ", password="
				+ password + ", mobile=" + mobile + ", role=" + role + ", statut=" + statut+ "]";
	}
	
}
