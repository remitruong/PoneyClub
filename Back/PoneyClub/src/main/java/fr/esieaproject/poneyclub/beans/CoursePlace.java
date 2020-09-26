package fr.esieaproject.poneyclub.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CoursePlace {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCoursePlace;
	
	@ManyToOne @JoinColumn(name ="idCourse")
	private Course course;

	@ManyToOne @JoinColumn(name ="idRider")
	private User rider;
	
	@ManyToOne @JoinColumn(name ="idHorse")
	private Horse horse;
	
	public CoursePlace() {}
	
	public CoursePlace(User rider) {
		this.rider = rider;
	}
	
	public CoursePlace(Horse horse) {
		this.horse = horse;
	}
	
	public Long getId() {
		return idCoursePlace;
	}
	
	public User getRider() {
		return this.rider;
	}
	
	public Horse getHorse() {
		return horse;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public void setRider(User rider) {
		this.rider = rider;
	}
	
	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	@Override
	public String toString() {
		return "CoursePlace [idCoursePlace=" + idCoursePlace + ", course=" + course + ", rider=" + rider + ", horse="
				+ horse + "]";
	}

}
