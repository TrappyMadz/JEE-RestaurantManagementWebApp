package fr.cytech.restaurant_management.entity;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.Temporal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity
public class Birthday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date date;

	@OneToOne
	private Child birthdayBoy;

	@OneToMany
	private ArrayList<Child> children;

	@Transient
	private Pizza pizza;

	private int nbPizza;

	public Child getBirthdayBoy() {
		return birthdayBoy;
	}

	public void setBirthdayBoy(Child birthdayBoy) {
		this.birthdayBoy = birthdayBoy;
	}

	public ArrayList<Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Child> children) {
		this.children = children;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNbPizza() {
		return nbPizza;
	}

	public void setNbPizza(int nbPizza) {
		this.nbPizza = nbPizza;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

}
