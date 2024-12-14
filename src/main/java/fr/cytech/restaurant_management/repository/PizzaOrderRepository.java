package fr.cytech.restaurant_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.PizzaOrder;


public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long>{
}
