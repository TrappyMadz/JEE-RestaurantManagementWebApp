package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Animatronic;

public interface AnimatronicRepository  extends JpaRepository<Animatronic, Long>{
	List<Animatronic> findByNameContainingIgnoreCase(String name);

}
