package fr.cytech.restaurant_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	List<Restaurant> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address);

	@Query("""
			SELECT r
			FROM Restaurant r
			WHERE r.id = :#{#animatronic.restaurant.id}
			""")
	List<Restaurant> findByAnimatronic(@Param("animatronic") Animatronic animatronic);
	
	Optional<Restaurant> findById(Long id);

}
