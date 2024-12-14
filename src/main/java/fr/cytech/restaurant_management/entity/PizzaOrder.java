package fr.cytech.restaurant_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PizzaOrder {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne @JoinColumn(name = "birthday_id")
	private Birthday birthday;
	
	@ManyToOne @JoinColumn(name = "pizza_id")
	private Pizza pizza;
	
	private int nbPizza;
	
	public PizzaOrder() {}
	
	public PizzaOrder(Birthday birthday, Pizza pizza, int nbPizza) {
		this.birthday = birthday;
		this.pizza = pizza;
		this.nbPizza = nbPizza;
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

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	public int getNbPizza() {
		return nbPizza;
	}

	public void setNbPizza(int nbPizza) {
		this.nbPizza = nbPizza;
	}

}
