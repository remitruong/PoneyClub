package fr.esieaproject.poneyclub.entity;

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
public class Horse {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idHorse;
	@Column(unique = true)
	private String name;
	
	
	public Horse() {}
	
	public Horse(String name) {
		this.name = name;
	}

	public Long getId() {
		return idHorse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Horse [id=" + idHorse + ", name=" + name +"]";
	}	
}
