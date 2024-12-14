package fr.cytech.restaurant_management.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Birthday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	@OneToOne
	private Child birthdayBoy;

	@ManyToMany
	private ArrayList<Child> children;

	@OneToMany(mappedBy = "birthday", cascade = CascadeType.ALL)
	private List<PizzaOrder> pizzaOrders;
	
	@ManyToOne @JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	@ManyToOne
	private Animatronic animatronic1;
	
	@ManyToOne
	private Animatronic animatronic2;

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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PizzaOrder> getPizzaOrders() {
		return pizzaOrders;
	}

	public void setPizzaOrders(List<PizzaOrder> pizzaOrders) {
		this.pizzaOrders = pizzaOrders;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Animatronic getAnimatronic1() {
		return animatronic1;
	}

	public void setAnimatronic1(Animatronic animatronic1) {
		this.animatronic1 = animatronic1;
	}

	public Animatronic getAnimatronic2() {
		return animatronic2;
	}

	public void setAnimatronic2(Animatronic animatronic2) {
		this.animatronic2 = animatronic2;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}



}
