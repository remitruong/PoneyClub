package fr.esieaproject.poneyclub.beans;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity
public class Planning {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String startDateTime;
	private String endDateTime;

    @ManyToOne
    @JoinColumn(name = "idHorse")
	private List<Horse> horse;
    
    @ManyToOne
    @JoinColumn(name = "idRider")
	private List<User> rider;
    @Min(value=1) @Max(value=8)
    private int levelStudying;
    private int maxStudent = 4;
	
	public Planning() {}
	
	public Planning(String title, String startDateTime, String endDateTime, int levelStudying ,int maxStudent) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.levelStudying = levelStudying;
		this.maxStudent = maxStudent;
	}
	
	public Planning(String startDateTime, String endDateTime, int levelStudying ,int maxStudent) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.levelStudying = levelStudying;
		this.maxStudent = maxStudent;
	}

	public Long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public List<Horse> getHorse() {
		return horse;
	}

	public List<User> getRider() {
		return rider;
	}
	
	public int getLevelStudying() {
		return levelStudying;
	}
	
	public int getMaxStudent() {
		return maxStudent;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public void addHorse(Horse horse) {
		this.horse.add(horse);
	}

	public void addRider(User rider) {
		this.rider.add(rider);
	}
	
	public void setLevelStudying(int levelStudying) {
		this.levelStudying = levelStudying;
	}
	
	public void setMaxStudent(int maxStudent) {
		this.maxStudent = maxStudent;
	}

	@Override
	public String toString() {
		return "Planning [id=" + id + ", title=" + title + ", startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", horse=" + horse + ", rider=" + rider + ", levelStudying=" + levelStudying
				+ ", maxStudent=" + maxStudent + "]";
	}

}
