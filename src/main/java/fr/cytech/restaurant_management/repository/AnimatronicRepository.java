package fr.cytech.restaurant_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.AnimatronicType;
import fr.cytech.restaurant_management.entity.Restaurant;

public interface AnimatronicRepository extends JpaRepository<Animatronic, Long> {
	
	List<Animatronic> findByNameContainingIgnoreCase(String name);
	
	@Query("SELECT a FROM Animatronic a WHERE a.name = :name AND a.type = :type AND a.id != :id")
	List<Animatronic> findByNameAndTypeExcludingId(String name,AnimatronicType type,Long id);

	List<Animatronic> findByNameAndType(String name,AnimatronicType type);
	
	Optional<Animatronic> findById(Long id);
	
	List<Animatronic> findByNameContainingIgnoreCaseAndType(String name,AnimatronicType type);
	
	@Query("SELECT a FROM Animatronic a WHERE a.restaurant IS NULL")
	List<Animatronic> findThoseWithoutRestaurant();
	
	@Query("SELECT a FROM Animatronic a WHERE a.restaurant IS NULL OR a.restaurant=:restaurant")
	List<Animatronic> findThoseWithoutRestaurantOrSameRestaurant(Restaurant restaurant);
	
	
}
