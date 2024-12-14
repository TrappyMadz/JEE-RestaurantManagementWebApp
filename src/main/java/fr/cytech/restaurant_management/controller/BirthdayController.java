package fr.cytech.restaurant_management.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findAll();
		
		model.addAttribute("restaurants",restaurants);
		model.addAttribute("animatronics",animatronics);
		if (birthday.getDate().compareTo(LocalDate.now()) <= 0) {
			model.addAttribute("error", "Entrez une date valide.");
			model.addAttribute("birthday",birthday);
			return "birthdayForm1";
		}
		if (birthday.getRestaurant() == null || birthday.getAnimatronic1() == null) {
			model.addAttribute("error", "Complétez tous les champs.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm1";
		}
		if (birthday.getAnimatronic1() == birthday.getAnimatronic2()) {
			birthday.setAnimatronic2(null);
		}
		model.addAttribute("birthday",birthday);
		List<Child> enfants = childRepository.findAll();
		model.addAttribute("children", enfants);
		return "birthdayForm2";
	}
	
	@PostMapping("/add2")
	public String createBirthdayPhase2(Model model, @ModelAttribute Birthday birthday, 
            @RequestParam(required = false) List<Long> childrenIds) {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findAll();
		List<Child> enfants = childRepository.findAll();
		
		if (childrenIds == null || childrenIds.isEmpty()) {
	    	model.addAttribute("error", "L'enfant ne doit pas être seul pour son anniversaire.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm2";
	    }
		
        List<Child> selectedChildren = childRepository.findAllById(childrenIds);
        for (Child child : selectedChildren) {
        	birthday.getChildren().add(child);
        }
		
		
		model.addAttribute("selectedChildren",selectedChildren);
		model.addAttribute("restaurants",restaurants);
		model.addAttribute("animatronics",animatronics);
		model.addAttribute("birthday",birthday);
		model.addAttribute("children", enfants);
		return "birthdayForm3";

    s
	@PostMapping("/finish")
	public String finishBirthday(Model model, @ModelAttribute Birthday birthday, @RequestParam Map<String, String> pizzasWithQty) {
		List<PizzaOrder> orders = new ArrayList<>();
		
		for (Map.Entry<String, String> e : pizzasWithQty.entrySet()) {
			String pizzaIdStr = e.getKey();
			String quantityStr = e.getValue();
			
			if (pizzaIdStr == null || pizzaIdStr == "" || quantityStr.isEmpty()) {
				model.addAttribute("error", "Pizza invalide ou quantité vide");
				model.addAttribute("birthday", birthday);
				List<Pizza> pizzas = pizzaRepository.findAll();
				model.addAttribute("pizzas", pizzas);
				return "birthdayForm3";
			}
			try {
				int qty = Integer.parseInt(quantityStr);
				if (qty <= 0) {
					model.addAttribute("error", "Veuillez entrer des quantités positives");
					model.addAttribute("birthday", birthday);
					List<Pizza> pizzas = pizzaRepository.findAll();
					model.addAttribute("pizzas", pizzas);
					return "birthdayForm3";
				}
				Long pizzaId = Long.parseLong(pizzaIdStr);
				Pizza pizza = pizzaRepository.findById(pizzaId).orElseThrow(() -> new RuntimeException("Pizza non trouvée"));
				
				orders.add(new PizzaOrder(birthday, pizza, qty));
			}
			catch(Error err) {
			}
		}
		birthday.setPizzaOrders(orders);
		birthday.getRestaurant().getBirthdays().add(birthday);
		birthday.getAnimatronic1().getBirthdays().add(birthday);
		birthday.getAnimatronic2().getBirthdays().add(birthday);
		birthday.getBirthdayBoy().setBirthday(birthday);
		for (Child child : birthday.getChildren()) {
			child.getBirthdays().add(birthday);
		}
		birthdayRepository.save(birthday);
		return "redirect:/";

	}

}
