package fr.cytech.restaurant_management;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Child {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String lastName;
	private String name;
	private int age;
	
	@Override
	public String toString() {
		return "Nom : " + lastName + " Prénom : " + name + " Âge : " + age;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Child other = (Child) obj;
		return Objects.equals(id, other.id);
	}

}
