package fr.cytech.restaurant_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Birthday;
import fr.cytech.restaurant_management.entity.Restaurant;

/**
 * Repository de la table anniversaire
 */
public interface BirthdayRepository extends JpaRepository<Birthday, Long> {

	/**
	 * Trouver la liste des anniversaires à venir et les tries par date
	 * 
	 * @param localDate date à partir de laquelle rechercher
	 * @return
	 */
	List<Birthday> findByDateAfterOrderByDateAsc(LocalDate localDate);

	/**
	 * Trouver la liste des aniversaires triés par date
	 * 
	 * @return
	 */
	List<Birthday> findAllByOrderByDateAsc();

	/**
	 * Trouver les anniversaires se déroulants à la date donnée
	 * 
	 * @param localDate
	 * @return
	 */
	List<Birthday> findByDate(LocalDate localDate);

	/**
	 * Trouver la liste des anniversaire se déroulant dans le restaurant donnés
	 * 
	 * @param restaurant
	 * @return
	 */
	List<Birthday> findByRestaurant(Restaurant restaurant);

}
