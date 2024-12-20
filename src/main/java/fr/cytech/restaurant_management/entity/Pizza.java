package fr.cytech.restaurant_management.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Table pizzas
 */
@Entity
public class Pizza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String composition;

	private double price;

	// Commandes
	@OneToMany(mappedBy = "pizza")
	private List<PizzaOrder> pizzaOrder;

	// Chemin vers l'image de la forme
	// /img/pizzas/idImage_nomOriginalImage.formatImage
	private String imagePath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return name + "\n" + composition + "\n" + price + "€";
	}

	public Long getId() {
		return id;
	}

	public List<PizzaOrder> getPizzaOrder() {
		return pizzaOrder;
	}

	public void setPizzaOrder(List<PizzaOrder> pizzaOrder) {
		this.pizzaOrder = pizzaOrder;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
