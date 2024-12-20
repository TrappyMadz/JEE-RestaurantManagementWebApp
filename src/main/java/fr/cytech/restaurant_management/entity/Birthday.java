package fr.cytech.restaurant_management.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

/**
 * Anniversaires, organisé pour un enfant, avec des pizzas et 2 animatroniques
 * animants l'aniversaire
 */
@Entity
public class Birthday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	// L'enfant star de l'aniversaire, celui dont on fête l'anniversaire
	@OneToOne(mappedBy = "birthday")
	private Child birthdayBoy;

	// Liste des invités
	@ManyToMany(mappedBy = "birthdays")
	private Set<Child> children = new HashSet<>();

	// Liste de commandes de pizzas (pizzaA : nbPizzaA, pizzaB : nbPizzaB...)
	@OneToMany(mappedBy = "birthday", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PizzaOrder> pizzaOrders;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@ManyToOne
	@JoinColumn(name = "animatronic1_id")
	private Animatronic animatronic1;

	@ManyToOne
	@JoinColumn(name = "animatronic2_id")
	private Animatronic animatronic2;

	public Child getBirthdayBoy() {
		return birthdayBoy;
	}

	public void setBirthdayBoy(Child birthdayBoy) {
		this.birthdayBoy = birthdayBoy;
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

	public Set<Child> getChildren() {
		return children;
	}

	public void setChildren(Set<Child> children) {
		this.children = children;
	}

}
