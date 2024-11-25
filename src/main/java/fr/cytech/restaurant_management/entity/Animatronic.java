package fr.cytech.restaurant_management.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Animatronic {
  
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated
	private AnimatronicType type;

	private String imagePath;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Animatronic other = (Animatronic) obj;
		return Objects.equals(name, other.name) && type == other.type;
	}

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

	public AnimatronicType getType() {
		return type;
	}

	public void setType(AnimatronicType type) {
		this.type = type;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return type + " " + name;
	}
}
