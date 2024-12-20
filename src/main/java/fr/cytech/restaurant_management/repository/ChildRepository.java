package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.Child;

/**
 * Repository de la table enfant
 */
public interface ChildRepository extends JpaRepository<Child, Long> {
	/**
	 * Fonction permettant de trouver un enfant avec son nom et son prénom
	 * 
	 * @param name     prénom de l'enfant
	 * @param lastName nom de l'enfant
	 * @return liste des enfants correspondant à la recherche
	 */
	List<Child> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);

	/**
	 * Récupérer tous les enfants sauf celui passé en paramètre.
	 * 
	 * @param id
	 * @return
	 */
	@Query("SELECT c FROM Child c WHERE c.id != :id")
	List<Child> findAllExceptThisOne(Long id);

	/**
	 * Récupérer les enfants sans anniversaires dont ils sont "la star"
	 * 
	 * @return
	 */
	@Query("SELECT c FROM Child c WHERE c.birthday IS NULL")
	List<Child> findThoseWhithoutBirthday();
	
	@Query("SELECT c FROM Child c WHERE c.birthday IS NULL OR c.birthday.id=:id")
	List<Child> findThoseWhithoutBirthdayOrThisOne(Long id);
}
