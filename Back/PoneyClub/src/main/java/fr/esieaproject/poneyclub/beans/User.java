package fr.esieaproject.poneyclub.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idUser;
	private String name;
	private String lastName;
	@Column(unique = true)
	private String mail;
	private String password;
	@Column(unique = true)
	private String mobile;
	@Column(unique = true)
	private String licenceNum;
	private String role;
	private String statut = "User";
	
	public User() {
	}

	public User(String name, String lastName, String mail, String password, String mobile) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
		this.password = password;
		this.mobile = mobile;
	}

	public User(String name, String lastName, String mail, String password, String mobile, String licenceNum) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
		this.password = password;
		this.mobile = mobile;
		this.licenceNum = licenceNum;
	}

	public User(String mail, String password) {
		super();
		this.mail = mail;
		this.password = password;
	}

	public Long getId() {
		return idUser;
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

	public String getLicenceNum() {
		return licenceNum;
	}

	public String getRole() {
		return role;
	}

	public String getStatut() {
		return statut;
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

	public void setLicenceNum(String licenceNum) {
		this.licenceNum = licenceNum;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	@Override
	public String toString() {
		return "User [id=" + idUser + ", name=" + name + ", lastName=" + lastName + ", mail=" + mail + ", password="
				+ password + ", mobile=" + mobile + ", licenceNum=" + licenceNum + ", role=" + role + ", statut="
				+ statut + "]";
	}

}
