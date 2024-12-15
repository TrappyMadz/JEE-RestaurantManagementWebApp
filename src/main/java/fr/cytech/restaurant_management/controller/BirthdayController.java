package fr.cytech.restaurant_management.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Birthday;
import fr.cytech.restaurant_management.entity.Child;
import fr.cytech.restaurant_management.entity.Pizza;
import fr.cytech.restaurant_management.entity.PizzaOrder;
import fr.cytech.restaurant_management.entity.Restaurant;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;
import fr.cytech.restaurant_management.repository.BirthdayRepository;
import fr.cytech.restaurant_management.repository.ChildRepository;
import fr.cytech.restaurant_management.repository.PizzaOrderRepository;
import fr.cytech.restaurant_management.repository.PizzaRepository;
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
	@Autowired
	ChildRepository childRepository;
	@Autowired
	PizzaRepository pizzaRepository;
	@Autowired
	PizzaOrderRepository pizzaOrderRepository;
	@Autowired
	ChildRepository childrenRepository;

	@GetMapping("/viewEvent")
	public String why(Model model) {
		List<Birthday> birthdays = birthdayRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
		model.addAttribute("birthdays", birthdays);
		return "viewEvent";
	}

	@GetMapping("/viewAllEvent")
	public String ohThatsWhy(Model model) {
		List<Birthday> birthdays = birthdayRepository.findAllByOrderByDateAsc();
		model.addAttribute("birthdays", birthdays);
		return "viewEvent";
	}

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

		model.addAttribute("birthday", birthday);
		model.addAttribute("restaurants", restaurants);
		model.addAttribute("animatronics", animatronics);
		List<Child> enfants = childRepository.findAll();
		model.addAttribute("children", enfants);
		return "birthdayForm1";
	}

	@PostMapping("/add")
	public String createBirthdayPhase1(Model model, @ModelAttribute Birthday birthday) {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findAll();

		model.addAttribute("restaurants", restaurants);
		model.addAttribute("animatronics", animatronics);
		if (birthday.getDate().compareTo(LocalDate.now()) <= 0) {
			model.addAttribute("error", "Entrez une date valide.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm1";
		}
		if (birthday.getRestaurant() == null || birthday.getBirthdayBoy() == null) {
			model.addAttribute("error", "Complétez tous les champs.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm1";
		}

		model.addAttribute("birthday", birthday);
		List<Child> enfants = childRepository.findAll();
		model.addAttribute("children", enfants);
		return "birthdayForm2";
	}

	@PostMapping("/add2")
	public String createBirthdayPhase2(Model model, @ModelAttribute Birthday birthday,
			@RequestParam("childrenIds") List<Long> childrenIds) {

		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findAll();
		List<Child> enfants = childRepository.findAll();

		if (birthday.getAnimatronic1() == birthday.getAnimatronic2()) {
			birthday.setAnimatronic2(null);
		}
		if (childrenIds == null || childrenIds.isEmpty()) {
			model.addAttribute("error", "L'enfant ne doit pas être seul pour son anniversaire.");
			model.addAttribute("birthday", birthday);
			model.addAttribute("children", enfants);
			return "birthdayForm2";
		}

		List<Child> selectedChildren = childRepository.findAllById(childrenIds);
		for (Child child : selectedChildren) {
			birthday.getChildren().add(child);
		}

		List<Pizza> pizzas = pizzaRepository.findAll();
		model.addAttribute("selectedChildren", selectedChildren);
		model.addAttribute("restaurants", restaurants);
		model.addAttribute("animatronics", animatronics);
		model.addAttribute("birthday", birthday);
		model.addAttribute("children", enfants);
		model.addAttribute("pizzas", pizzas);
		return "birthdayForm3";
	}

	@PostMapping("/finish")
	public String finishBirthday(Model model, @ModelAttribute Birthday birthday,
			@RequestParam("pizzaIds[]") List<Long> pizzaIds, @RequestParam("quantities[]") List<Integer> quantities,
			@RequestParam("selectedPizzas[]") List<Long> selectedPizzas,
			@RequestParam("childrenIds") List<Child> selectedChildren) {

		List<PizzaOrder> orders = new ArrayList<>();

		for (int i = 0; i < pizzaIds.size(); i++) {
			Long pizzaId = pizzaIds.get(i);
			Integer quantity = quantities.get(i);
			if (selectedPizzas.contains(pizzaId) && quantity > 0) {
				Optional<Pizza> pizzaAct = pizzaRepository.findById(pizzaId);
				PizzaOrder order = new PizzaOrder(birthday, pizzaAct.get(), quantity);
				orders.add(order);
				order.setBirthday(birthday);
			}
		}

		birthday.setPizzaOrders(orders);
		birthday.getRestaurant().getBirthdays().add(birthday);
		birthday.getAnimatronic1().getBirthdays1().add(birthday);

		if (birthday.getAnimatronic2() != null) {
			birthday.getAnimatronic2().getBirthdays2().add(birthday);
		}

		birthday.getBirthdayBoy().setBirthday(birthday);
		for (Child child : selectedChildren) {
			birthday.getChildren().add(child);
			child.getBirthdays().add(birthday);
		}
		birthdayRepository.save(birthday);
		pizzaOrderRepository.saveAll(orders);
		childrenRepository.saveAll(selectedChildren);
		return "redirect:/";

	}

	@PostMapping("/delete/{id}")
	public String stopFun(@PathVariable("id") Long id) {
	    Optional<Birthday> optionalBirthday = birthdayRepository.findById(id);
	    if (optionalBirthday.isPresent()) {
	        Birthday birthday = optionalBirthday.get();
	        if (birthday.getBirthdayBoy() != null) {
	            birthday.getBirthdayBoy().setBirthday(null);
	            childRepository.save(birthday.getBirthdayBoy());
	        }
	        for (Child child : birthday.getChildren()) {
	            child.getBirthdays().remove(birthday);
	        }
	        birthday.getChildren().clear();
	        if (birthday.getAnimatronic1() != null) {
	            birthday.getAnimatronic1().getBirthdays1().remove(birthday);
	        }
	        if (birthday.getAnimatronic2() != null) {
	            birthday.getAnimatronic2().getBirthdays2().remove(birthday);
	        }
	        if (birthday.getRestaurant() != null) {
	            birthday.getRestaurant().getBirthdays().remove(birthday);
	        }
	        birthdayRepository.save(birthday);
	        birthdayRepository.delete(birthday);
	    }
	    return "redirect:/birthday/viewEvent";
	}


}
