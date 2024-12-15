package fr.cytech.restaurant_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

/**
 * Commandes, contenant le type de pizza et leur nombre. Un anniversaire peut
 * avoir plusieurs commandes de pizzas.
 */
@Entity
public class PizzaOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Anniversaire pour lequel est faite la commande
	@ManyToOne
	@JoinColumn(name = "birthday_id")
	private Birthday birthday;

	@ManyToOne
	@JoinColumn(name = "pizza_id")
	private Pizza pizza;

	@NotNull
	private int nbPizza;

	/**
	 * Constructeur par défaut utilisé par JPA
	 */
	public PizzaOrder() {
	}

	/**
	 * Constructeur utilisé lors de l'initialisation d'un anniversaire
	 * 
	 * @param birthday Anniversaire
	 * @param pizza    Type de pizza
	 * @param nbPizza  Nombre de ces pizzas
	 */
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
