package fr.cytech.restaurant_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Child;

public interface ChildRepository extends JpaRepository<Child, Long>{
	List<Child> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);
}
