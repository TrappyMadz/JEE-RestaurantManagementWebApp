package fr.cytech.restaurant_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Birthday;
import fr.cytech.restaurant_management.entity.Restaurant;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;
import fr.cytech.restaurant_management.repository.BirthdayRepository;
import fr.cytech.restaurant_management.repository.RestaurantRepository;

@Controller
@RequestMapping("/birthday")
public class BirthdayController {
	
	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	AnimatronicRepository animatronicRepository;
	@Autowired
	BirthdayRepository birthdayRepository;
	
	@GetMapping("/add")
	public String initBirthday(Model model) {
		Birthday birthday = new Birthday();
		birthday.setDate(LocalDate.now());
		Restaurant restaurantAct = null;
		Animatronic animatronic1Act = null;
		Animatronic animatronic2Act = null;
		
		birthday.setAnimatronic1(animatronic1Act);
		birthday.setAnimatronic2(animatronic2Act);
		birthday.setRestaurant(restaurantAct);
		
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findAll();
		
		model.addAttribute("birthday",birthday);
		model.addAttribute("restaurants",restaurants);
		model.addAttribute("animatronics",animatronics);
		return "birthdayForm1";
	}
	
	@PostMapping("/add")
	public String createBirthdayPhase1(Model model, @ModelAttribute Birthday birthday) {
		if (birthday.getDate().compareTo(LocalDate.now()) <= 0) {
			model.addAttribute("error", "Entrez une date valide.");
			model.addAttribute("birthday",birthday);
			return "birthdayForm1";
		}
		if (birthday.getRestaurant() == null || birthday.getAnimatronic1() == null || birthday.getAnimatronic2() == null) {
			model.addAttribute("error", "Complétez tous les champs.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm1";
		}
		model.addAttribute("birthday",birthday);
		return "birthdayForm2";
	}

}
