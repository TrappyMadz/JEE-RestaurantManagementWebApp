package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.AnimatronicType;

public interface AnimatronicRepository extends JpaRepository<Animatronic, Long> {
	List<Animatronic> findByNameContainingIgnoreCase(String name);
	List<Animatronic> findByNameAndType(String name,AnimatronicType type);
}
