package fr.cytech.restaurant_management;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Animatronic {
	
	private Long id;
	
	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animatronic other = (Animatronic) obj;
		return Objects.equals(name, other.name) && type == other.type;
	}

	private String name;
	
	private AnimatronicType type;
	
	private Restaurant restaurant;
	
	
	
	
}
