package fr.cytech.restaurant_management.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

/**
 * Table enfants
 */
@Entity
public class Child {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Un enfant n'a qu'un anniversaire
	@OneToOne
	@JoinColumn(name = "birthday_id")
	private Birthday birthday;

	private String lastName;
	private String name;
	private int age;

	// Un enfant peut être invité à plusieurs anniversaires
	@ManyToMany
	@JoinTable(name = "child_birthday", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "birthday_id"))
	private Set<Birthday> birthdays = new HashSet<>();

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

	public Birthday getBirthday() {
		return birthday;
	}

	public void setBirthday(Birthday birthday) {
		this.birthday = birthday;
	}

	public Set<Birthday> getBirthdays() {
		return birthdays;
	}

	public void setBirthdays(Set<Birthday> birthdays) {
		this.birthdays = birthdays;
	}

}
