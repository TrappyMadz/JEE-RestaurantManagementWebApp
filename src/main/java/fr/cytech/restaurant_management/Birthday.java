package fr.cytech.restaurant_management;

import java.util.Date;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Birthday {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date date;
	private Child birthdayBoy;
	private Child[] children;
	private Pizza pizza;
	private int nbPizza;

}
