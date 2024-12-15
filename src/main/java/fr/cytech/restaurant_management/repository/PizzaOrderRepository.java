package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.PizzaOrder;


public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long>{
	
	@Query("SELECT p FROM PizzaOrder p WHERE p.pizza.id=:id")
	List<PizzaOrder> findbyPizzaId(Long id);
}
