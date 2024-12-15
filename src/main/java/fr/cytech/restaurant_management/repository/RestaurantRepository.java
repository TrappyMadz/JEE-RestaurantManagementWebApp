package fr.cytech.restaurant_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Restaurant;

/**
 * Gestion des restaurants
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	/**
	 * Recherche par nom et adresse
	 * 
	 * @param name
	 * @param address
	 * @return
	 */
	List<Restaurant> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address);

	/**
	 * Recherche du restaurant auquel appartient un animatronique
	 * 
	 * @param animatronic
	 * @return
	 */
	@Query("""
			SELECT r
			FROM Restaurant r
			WHERE r.id = :#{#animatronic.restaurant.id}
			""")
	List<Restaurant> findByAnimatronic(@Param("animatronic") Animatronic animatronic);

	/**
	 * Recherche par id d'un restaurant
	 */
	Optional<Restaurant> findById(Long id);

}
