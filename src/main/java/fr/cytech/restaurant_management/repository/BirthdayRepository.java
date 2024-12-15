package fr.cytech.restaurant_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Birthday;
import fr.cytech.restaurant_management.entity.Restaurant;

/**
 * Repository de la table enfant
 */
public interface BirthdayRepository extends JpaRepository<Birthday, Long>{

	List<Birthday> findByDateAfterOrderByDateAsc(LocalDate localDate);
	List<Birthday> findAllByOrderByDateAsc();
	
	List<Birthday> findByDate(LocalDate localDate);

	List<Birthday> findByRestaurant(Restaurant restaurant);
	

}
