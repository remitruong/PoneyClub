package fr.esieaproject.poneyclub.beans;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Planning {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String startDateTime;
	private String endDateTime;

    @ManyToOne
    @JoinColumn(name = "idHorse")
	private Horse horse;
    
    @ManyToOne
    @JoinColumn(name = "idRider")
	private User rider;
	
	public Planning() {}
	
	public Planning(String startDateTime, String endDateTime, Horse horse, User rider) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.horse = horse;
		this.rider = rider;		
	}

	public Long getId() {
		return id;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public Horse getHorse() {
		return horse;
	}

	public User getRider() {
		return rider;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	public void setRider(User rider) {
		this.rider = rider;
	}

	@Override
	public String toString() {
		return "Planning [id=" + id + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", horse="
				+ horse + ", rider=" + rider + "]";
	}

}
