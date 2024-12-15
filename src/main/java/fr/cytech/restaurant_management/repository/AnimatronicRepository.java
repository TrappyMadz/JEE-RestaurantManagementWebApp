package fr.cytech.restaurant_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.AnimatronicType;
import fr.cytech.restaurant_management.entity.Restaurant;

/**
 * Repo des animatroniques
 */
public interface AnimatronicRepository extends JpaRepository<Animatronic, Long> {

	/**
	 * Fonction servant à trouver un animatronique via son nom
	 * 
	 * @param name nom à chercher
	 * @return la liste des animatroniques correspondants
	 */
	List<Animatronic> findByNameContainingIgnoreCase(String name);

	/**
	 * Recherche par nom et type, mais excluant l'id, pour rechercher des doublons
	 * mais sans être la copie exacte.
	 * 
	 * @param name nom de l'animatronique
	 * @param type type de l'animatronique
	 * @param id   id de l'animatronique avec lequel faire la comparaison
	 * @return la liste correspondante
	 */
	@Query("SELECT a FROM Animatronic a WHERE a.name = :name AND a.type = :type AND a.id != :id")
	List<Animatronic> findByNameAndTypeExcludingId(String name, AnimatronicType type, Long id);

	/**
	 * Recherche par nom et type
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	List<Animatronic> findByNameAndType(String name, AnimatronicType type);

	/**
	 * Recherche par Id
	 */
	Optional<Animatronic> findById(Long id);

	/**
	 * Recherche par nom et type en ignorant la casse sur le nom et en regardant
	 * uniquement si la chaîne recherchée est contenue dans le nom
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	List<Animatronic> findByNameContainingIgnoreCaseAndType(String name, AnimatronicType type);

	/**
	 * Recherche des animatroniques sans restaurant
	 * 
	 * @return
	 */
	@Query("SELECT a FROM Animatronic a WHERE a.restaurant IS NULL")
	List<Animatronic> findThoseWithoutRestaurant();

	/**
	 * Recherche des animatroniques sans restaurant où faisant partie du restaurant
	 * associé
	 * 
	 * @param restaurant
	 * @return
	 */
	@Query("SELECT a FROM Animatronic a WHERE a.restaurant IS NULL OR a.restaurant=:restaurant")
	List<Animatronic> findThoseWithoutRestaurantOrSameRestaurant(Restaurant restaurant);

	/**
	 * Recherche des animatroniques appartenant au restaurant passé en paramètre
	 * 
	 * @param restaurant
	 * @return
	 */
	@Query("SELECT a FROM Animatronic a WHERE a.restaurant=:restaurant")
	List<Animatronic> findByRestaurant(Restaurant restaurant);

}
