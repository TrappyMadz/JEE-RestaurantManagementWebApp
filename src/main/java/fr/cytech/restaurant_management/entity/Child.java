package fr.cytech.restaurant_management.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Table enfants
 */
@Entity
public class Child {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String lastName;
	private String name;
	private int age;

	@Override
	public String toString() {
		return name + " " + " " + lastName + " " + age + "ans";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Child other = (Child) obj;
		return Objects.equals(id, other.id);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
