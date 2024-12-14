package fr.cytech.restaurant_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.cytech.restaurant_management.entity.Birthday;

/**
 * Repository de la table enfant
 */
public interface BirthdayRepository extends JpaRepository<Birthday, Long>{
}
