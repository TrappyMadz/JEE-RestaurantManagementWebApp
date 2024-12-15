package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.PizzaOrder;

/**
 * Repos de PizzaOrder
 */
public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {

	/**
	 * Recherche d'une commande par restaurant, en passant par birthday (puisque
	 * pizzaOrder et Restaurants ne sont pas directement liés
	 * 
	 * @param restaurantId
	 * @return
	 */
	List<PizzaOrder> findByBirthday_Restaurant_Id(Long restaurantId);
}
