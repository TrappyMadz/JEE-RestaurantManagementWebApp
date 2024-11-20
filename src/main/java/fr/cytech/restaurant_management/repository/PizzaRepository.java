package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Pizza;

/**
 * Repository de la table pizza
 */
public interface PizzaRepository extends JpaRepository<Pizza, Long>{

	/**
	 * Fonction permettant de chercher une pizza par son nom ou sa composition
	 * @param name nom de la pizza à chercher
	 * @param composition ingrédients à chercher
	 * @return liste de pizzas correspondantes
	 */
	Pizza findByName(String name);
	List<Pizza> findByNameContainingIgnoreCaseOrCompositionContainingIgnoreCase(String name, String composition);
}
