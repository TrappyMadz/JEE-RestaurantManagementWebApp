package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.AnimatronicType;

public interface AnimatronicRepository extends JpaRepository<Animatronic, Long> {
	
	List<Animatronic> findByNameContainingIgnoreCase(String name);
	
	@Query("SELECT a FROM Animatronic a WHERE a.name = :name AND a.type = :type AND a.id != :id")
	List<Animatronic> findByNameAndTypeExcludingId(String name,AnimatronicType type,Long id);

	List<Animatronic> findByNameAndType(String name,AnimatronicType type);
	
	List<Animatronic> findByNameContainingIgnoreCaseAndType(String name,AnimatronicType type);
}
