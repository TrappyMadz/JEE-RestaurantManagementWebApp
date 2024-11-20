package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long>{

	Pizza findByName(String name);
	List<Pizza> findByNameContainingIgnoreCaseOrCompositionContainingIgnoreCase(String name, String composition);
}
