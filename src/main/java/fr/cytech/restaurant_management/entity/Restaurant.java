package fr.cytech.restaurant_management.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String address;

	@OneToMany(mappedBy = "restaurant")
	private List<Animatronic> animatronics;
	
	@OneToMany(mappedBy = "restaurant")
	private List<Birthday> birthdays;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Animatronic> getAnimatronics() {
		return animatronics;
	}

	public void setAnimatronics(List<Animatronic> animatronics) {
		this.animatronics = animatronics;
	}

	@Override
	public String toString() {
		return "Restaurant " + name + ", " + address + ", animatronics=" + animatronics;
	}

	public List<Birthday> getBirthdays() {
		return birthdays;
	}

	public void setBirthdays(List<Birthday> birthdays) {
		this.birthdays = birthdays;
	}
	
	

}
