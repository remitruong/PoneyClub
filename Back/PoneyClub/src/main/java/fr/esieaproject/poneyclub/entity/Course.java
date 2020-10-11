package fr.esieaproject.poneyclub.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity
public class Course {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String title;
	private Timestamp startDateTime;
	private Timestamp endDateTime;    
    @Min(value=1) @Max(value=8)
    private int levelStudying;
    private int maxStudent = 4;
    
    @ManyToOne @JoinColumn(name ="idUser")
    private User teacher;
    
	public Course() {}
	
	public Course(String title, String startDateTime, String endDateTime, int levelStudying ,int maxStudent) {
		this.title = title;
		this.startDateTime = Timestamp.valueOf(startDateTime);
		this.endDateTime = Timestamp.valueOf(endDateTime);
		this.levelStudying = levelStudying;
		this.maxStudent = maxStudent;
	}
	
	public Course(String startDateTime, String endDateTime, int levelStudying ,int maxStudent) {
		this.startDateTime = Timestamp.valueOf(startDateTime);
		this.endDateTime = Timestamp.valueOf(endDateTime);
		this.levelStudying = levelStudying;
		this.maxStudent = maxStudent;
	}

	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getStartDateTime() {
		return startDateTime.toString();
	}

	public String getEndDateTime() {
		return endDateTime.toString();
	}
	
	public int getLevelStudying() {
		return levelStudying;
	}
	
	public int getMaxStudent() {
		return maxStudent;
	}
	
	public User getTeacher() {
		return teacher;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setStartDateTime(String startDateTime) {
		Timestamp startTime = Timestamp.valueOf(startDateTime);
		this.startDateTime = startTime;
	}

	public void setEndDateTime(String endDateTime) {
		Timestamp endTime = Timestamp.valueOf(endDateTime);
		this.endDateTime = endTime;
	}
	
	public void setLevelStudying(int levelStudying) {
		this.levelStudying = levelStudying;
	}
	
	public void setMaxStudent(int maxStudent) {
		this.maxStudent = maxStudent;
	}
	
	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
	

	@Override
	public String toString() {
		return "Planning [id=" + id + ", title=" + title + ", startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", levelStudying=" + levelStudying + ", maxStudent=" + maxStudent +"]";
	}

}
