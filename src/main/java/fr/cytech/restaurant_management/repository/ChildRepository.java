package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.Child;

/**
 * Repository de la table enfant
 */
public interface ChildRepository extends JpaRepository<Child, Long>{
	/**
	 * Fonction permettant de trouver un enfant avec son nom et son prénom
	 * @param name prénom de l'enfant
	 * @param lastName nom de l'enfant
	 * @return liste des enfants correspondant à la recherche
	 */
	List<Child> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);
	
	@Query("SELECT c FROM Child c WHERE c.id != :id")
	List<Child> findAllExceptThisOne(Long id);
	
	@Query("SELECT c FROM Child c WHERE c.birthday IS NULL")
	List<Child> findThoseWhithoutBirthday();
}
