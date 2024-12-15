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

/**
 * Anniversaires
 */
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

	/**
	 * Afficher la liste des anniversaires futur
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/viewEvent")
	public String why(Model model) {
		List<Birthday> birthdays = birthdayRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
		model.addAttribute("birthdays", birthdays);
		return "viewEvent";
	}

	/**
	 * Voir tous les anniversaires
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/viewAllEvent")
	public String ohThatsWhy(Model model) {
		List<Birthday> birthdays = birthdayRepository.findAllByOrderByDateAsc();
		model.addAttribute("birthdays", birthdays);
		return "viewEvent";
	}

	/**
	 * Initialisation de la création d'un anniversaire
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String initBirthday(Model model) {

		// On initialise un anniversaire vide pour l'autocomplétion en cas d'erreur
		Birthday birthday = new Birthday();
		birthday.setDate(LocalDate.now());
		Restaurant restaurantAct = null;
		Animatronic animatronic1Act = null;
		Animatronic animatronic2Act = null;

		birthday.setAnimatronic1(animatronic1Act);
		birthday.setAnimatronic2(animatronic2Act);
		birthday.setRestaurant(restaurantAct);

		List<Restaurant> restaurants = restaurantRepository.findAll();

		model.addAttribute("birthday", birthday);
		model.addAttribute("restaurants", restaurants);
		List<Child> enfants = childRepository.findThoseWhithoutBirthday();
		model.addAttribute("children", enfants);
		return "birthdayForm1";
	}

	/**
	 * Fin de la première phase de création d'un anniversaire
	 * 
	 * @param model
	 * @param birthday
	 * @return birthday avec date, enfant star et restaurant de parametrés
	 */
	@PostMapping("/add")
	public String createBirthdayPhase1(Model model, @ModelAttribute Birthday birthday) {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		model.addAttribute("restaurants", restaurants);
		List<Child> enfants = childRepository.findAll();

		// On gère les potentielles erreurs ici
		if (birthday.getDate().compareTo(LocalDate.now()) <= 0) {
			model.addAttribute("error", "Entrez une date valide.");
			model.addAttribute("birthday", birthday);
			model.addAttribute("children", enfants);
			return "birthdayForm1";
		}
		if (birthday.getRestaurant() == null || birthday.getBirthdayBoy() == null) {
			model.addAttribute("error", "Complétez tous les champs.");
			model.addAttribute("birthday", birthday);
			model.addAttribute("children", enfants);
			return "birthdayForm1";
		}

		model.addAttribute("children", enfants);
		enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
		model.addAttribute("childrenList", enfants);
		List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
		model.addAttribute("animatronics", animatronics);
		model.addAttribute("birthday", birthday);

		// Si tout s'est bien passé, on passe à la suite du formulaire
		return "birthdayForm2";
	}

	/**
	 * Partie 2 du formulaire
	 * 
	 * @param model
	 * @param birthday
	 * @param childrenIds liste des enfants qui ont été cochés suite au second
	 *                    formulaire
	 * @return le birthday avec en plus les animatroniques et les invités
	 */
	@PostMapping("/add2")
	public String createBirthdayPhase2(Model model, @ModelAttribute Birthday birthday,
			@RequestParam(required = false) List<Long> childrenIds) {

		List<Restaurant> restaurants = restaurantRepository.findAll();
		List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
		List<Child> enfants = childRepository.findAll();
		model.addAttribute("children", enfants);
		enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
		model.addAttribute("childrenList", enfants);
		model.addAttribute("animatronics", animatronics);
		model.addAttribute("restaurants", restaurants);

		if (childrenIds == null || childrenIds.isEmpty()) {
			model.addAttribute("error", "L'enfant ne doit pas être seul pour son anniversaire.");
			model.addAttribute("birthday", birthday);
			return "birthdayForm2";

		}
		List<Child> selectedChildren = childRepository.findAllById(childrenIds);
		for (Child child : selectedChildren) {
			birthday.getChildren().add(child);
		}
		model.addAttribute("selectedChildren", selectedChildren);

		if (birthday.getAnimatronic1() == birthday.getAnimatronic2()) {
			birthday.setAnimatronic2(null);
		}
		List<Birthday> sameDayBirthday = birthdayRepository.findByDate(birthday.getDate());
		for (Birthday b : sameDayBirthday) {
			if (b.getAnimatronic1().getId() == birthday.getAnimatronic1().getId() || (birthday.getAnimatronic2() != null
					&& b.getAnimatronic1().getId() == birthday.getAnimatronic2().getId())) {
				model.addAttribute("error", "Les animatroniques ne peuvent être utilisés plusieurs fois par jour. "
						+ b.getAnimatronic1() + " doit se recharger cette journée.");
				model.addAttribute("birthday", birthday);
				return "birthdayForm2";
			} else if (b.getAnimatronic2().getId() != null
					&& (b.getAnimatronic2().getId() == birthday.getAnimatronic1().getId()
							|| (birthday.getAnimatronic2() != null
									&& b.getAnimatronic2().getId() == birthday.getAnimatronic2().getId()))) {
				model.addAttribute("error", "Les animatroniques ne peuvent être utilisés plusieurs fois par jour. "
						+ b.getAnimatronic2() + " doit se recharger cette journée.");
				model.addAttribute("birthday", birthday);
				return "birthdayForm2";
			}
		}

		List<Pizza> pizzas = pizzaRepository.findAll();
		model.addAttribute("birthday", birthday);
		model.addAttribute("pizzas", pizzas);

		return "birthdayForm3";
	}

	/**
	 * Finalisation du form
	 * 
	 * @param model
	 * @param birthday
	 * @param pizzaIds         liste des id des pizzas existantes
	 * @param quantities       quantités liées à chaque pizza existantes (null si
	 *                         non cochée)
	 * @param selectedPizzas   liste des pizzas cochées
	 * @param selectedChildren enfants cochés
	 * @return Création de l'anniversaire
	 */
	@PostMapping("/finish")
	public String finishBirthday(Model model, @ModelAttribute Birthday birthday,
			@RequestParam("pizzaIds[]") List<Long> pizzaIds, @RequestParam("quantities[]") List<Integer> quantities,
			@RequestParam(value = "selectedPizzas[]", required = false) List<Long> selectedPizzas,
			@RequestParam("childrenIds") List<Child> selectedChildren) {

		List<PizzaOrder> orders = new ArrayList<>();

		// On vérifie la validité des paramètres
		if (selectedPizzas == null || selectedPizzas.isEmpty()) {
			model.addAttribute("error", "Veuillez selectionner au moins une pizza.");

			List<Restaurant> restaurants = restaurantRepository.findAll();
			List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
			List<Child> enfants = childRepository.findAll();
			model.addAttribute("children", enfants);
			enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
			model.addAttribute("childrenList", enfants);
			model.addAttribute("animatronics", animatronics);
			model.addAttribute("restaurants", restaurants);
			List<Pizza> pizzas = pizzaRepository.findAll();
			model.addAttribute("birthday", birthday);
			model.addAttribute("pizzas", pizzas);
			model.addAttribute("selectedChildren", selectedChildren);
			return "birthdayForm3";
		}
		if (quantities.isEmpty()) {
			quantities.add(null);
		}
		for (int i = 0; i < pizzaIds.size(); i++) {
			Long pizzaId = pizzaIds.get(i);
			Integer quantity = quantities.get(i);
			if (selectedPizzas.contains(pizzaId)) {
				if (quantity == null || quantity <= 0) {

					model.addAttribute("error",
							"Veuillez donner un nombre de pizza positif pour chaque pizza selectionnée.");

					List<Restaurant> restaurants = restaurantRepository.findAll();
					List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
					List<Child> enfants = childRepository.findAll();
					model.addAttribute("children", enfants);
					enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
					model.addAttribute("childrenList", enfants);
					model.addAttribute("animatronics", animatronics);
					model.addAttribute("restaurants", restaurants);
					List<Pizza> pizzas = pizzaRepository.findAll();
					model.addAttribute("birthday", birthday);
					model.addAttribute("pizzas", pizzas);
					model.addAttribute("selectedChildren", selectedChildren);
					return "birthdayForm3";
				} else {

					// On s'occupe du pizzaOrder pour chaque pizza
					Optional<Pizza> pizzaAct = pizzaRepository.findById(pizzaId);
					PizzaOrder order = new PizzaOrder(birthday, pizzaAct.get(), quantity);
					orders.add(order);
					order.setBirthday(birthday);
				}
			}
		}

		// On finalise la création de l'anniversaire
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

	/**
	 * Suppression d'un évènement
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/delete/{id}")
	public String stopFun(@PathVariable("id") Long id) {
		Optional<Birthday> optionalBirthday = birthdayRepository.findById(id);
		if (optionalBirthday.isPresent()) {
			Birthday birthday = optionalBirthday.get();

			// Dissocier l'enfant principal (OneToOne)
			if (birthday.getBirthdayBoy() != null) {
				birthday.getBirthdayBoy().setBirthday(null);
				childRepository.save(birthday.getBirthdayBoy());
			}

			// Dissocier les enfants invités (ManyToMany)
			for (Child child : birthday.getChildren()) {
				child.getBirthdays().remove(birthday);
			}
			birthday.getChildren().clear();

			// Dissocier les animatronics (ManyToOne)
			if (birthday.getAnimatronic1() != null) {
				birthday.getAnimatronic1().getBirthdays1().remove(birthday);
			}
			if (birthday.getAnimatronic2() != null) {
				birthday.getAnimatronic2().getBirthdays2().remove(birthday);
			}

			// Dissocier le restaurant
			if (birthday.getRestaurant() != null) {
				birthday.getRestaurant().getBirthdays().remove(birthday);
			}

			// Sauvegarder les dissociations avant suppression
			birthdayRepository.save(birthday);
			birthdayRepository.delete(birthday);
		}
		return "redirect:/birthday/viewEvent";

	}

	/*
	 * Initialisation de la Modification (envoi sur birthdayUpdateForm1, voir la
	 * requête Get /add pour plus de détails en commentaires)
	 */
	@GetMapping("/update/{id}")
	public String changeBirthday(@PathVariable("id") Long id, Model model) {
		Optional<Birthday> optionalBirthday = birthdayRepository.findById(id);
		if (optionalBirthday.isEmpty()) {
			model.addAttribute("error", "L'anniversaire sélectionné n'existe pas.");
			return "viewEvent";
		} else {
			List<Restaurant> restaurants = restaurantRepository.findAll();
			model.addAttribute("restaurants", restaurants);
			List<Child> enfants = childRepository.findThoseWhithoutBirthdayOrThisOne(id);
			model.addAttribute("children", enfants);
			model.addAttribute("birthday", optionalBirthday.get());
			return "birthdayUpdateForm1";
		}
	}

	/*
	 * Modification fonctionnant comme la requête Post /add
	 */
	@PostMapping("/update/{id}")
	public String changeBirthday2(@PathVariable("id") Long id, Model model, @ModelAttribute Birthday birthday) {
		Optional<Birthday> optionalBirthday = birthdayRepository.findById(birthday.getId());

		if (optionalBirthday.isEmpty()) {
			model.addAttribute("error", "L'anniversaire séléctionné n'existe pas.");
			return "viewEvent";
		} else {
			Birthday existingBirthday = optionalBirthday.get();
			List<Restaurant> restaurants = restaurantRepository.findAll();
			model.addAttribute("restaurants", restaurants);
			List<Child> enfants = childRepository.findAll();

			if (birthday.getDate().compareTo(existingBirthday.getDate()) < 0) {
				model.addAttribute("error",
						"Vous ne pouvez pas entrer une date antérieure à l'anniversaire déjà enregistré.");
				model.addAttribute("birthday", birthday);
				enfants = childRepository.findThoseWhithoutBirthdayOrThisOne(id);
				model.addAttribute("children", enfants);
				return "birthdayUpdateForm1";
			}
			if (birthday.getRestaurant() == null || birthday.getBirthdayBoy() == null) {
				model.addAttribute("error", "Complétez tous les champs.");
				model.addAttribute("birthday", birthday);
				enfants = childRepository.findThoseWhithoutBirthdayOrThisOne(id);
				model.addAttribute("children", enfants);
				return "birthdayUpdateForm1";
			}

			model.addAttribute("children", enfants);
			enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
			model.addAttribute("childrenList", enfants);
			List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
			model.addAttribute("animatronics", animatronics);
			model.addAttribute("birthday", birthday);
			model.addAttribute("existingBirthday", existingBirthday);
			model.addAttribute("selectedChildren", existingBirthday.getChildren());
			return "birthdayUpdateForm2";
		}
	}

	/*
	 * Modification étape 2 : Fonctionne comme la requête Post /add2
	 */
	@PostMapping("/update2/{id}")
	public String changeBirthday2(@PathVariable("id") Long id, Model model, @ModelAttribute Birthday birthday,
			@RequestParam(required = false) List<Long> childrenIds) {
		Optional<Birthday> optionalBirthday = birthdayRepository.findById(birthday.getId());
		if (optionalBirthday.isEmpty()) {
			model.addAttribute("error", "L'anniversaire séléctionné n'existe pas.");
			return "viewEvent";
		} else {
			Birthday existingBirthday = optionalBirthday.get();

			List<Restaurant> restaurants = restaurantRepository.findAll();
			List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
			List<Child> enfants = childRepository.findAll();
			model.addAttribute("children", enfants);
			enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
			model.addAttribute("childrenList", enfants);
			model.addAttribute("animatronics", animatronics);
			model.addAttribute("restaurants", restaurants);

			if (childrenIds == null || childrenIds.isEmpty()) {
				model.addAttribute("error", "L'enfant ne doit pas être seul pour son anniversaire.");
				model.addAttribute("birthday", birthday);
				return "birthdayForm2";

			}
			List<Child> selectedChildren = childRepository.findAllById(childrenIds);
			for (Child child : selectedChildren) {
				birthday.getChildren().add(child);
			}
			model.addAttribute("selectedChildren", selectedChildren);

			if (birthday.getAnimatronic1() == birthday.getAnimatronic2()) {
				birthday.setAnimatronic2(null);
			}
			List<Birthday> sameDayBirthday = birthdayRepository.findByDate(birthday.getDate());
			for (Birthday b : sameDayBirthday) {
				if (b.getId() == existingBirthday.getId()) {
					continue;
				}
				if (b.getAnimatronic1().getId() == birthday.getAnimatronic1().getId()
						|| (birthday.getAnimatronic2() != null
								&& b.getAnimatronic1().getId() == birthday.getAnimatronic2().getId())) {
					model.addAttribute("error", "Les animatroniques ne peuvent être utilisés plusieurs fois par jour. "
							+ b.getAnimatronic1() + " doit se recharger cette journée.");
					model.addAttribute("birthday", birthday);
					return "birthdayForm2";
				} else if (b.getAnimatronic2().getId() != null
						&& (b.getAnimatronic2().getId() == birthday.getAnimatronic1().getId()
								|| (birthday.getAnimatronic2() != null
										&& b.getAnimatronic2().getId() == birthday.getAnimatronic2().getId()))) {
					model.addAttribute("error", "Les animatroniques ne peuvent être utilisés plusieurs fois par jour. "
							+ b.getAnimatronic2() + " doit se recharger cette journée.");
					model.addAttribute("birthday", birthday);
					return "birthdayForm2";
				}
			}

			List<Pizza> pizzas = pizzaRepository.findAll();
			model.addAttribute("birthday", birthday);
			model.addAttribute("pizzas", pizzas);

			return "birthdayUpdateForm3";
		}
	}

	/*
	 * Modification finale, fonctionne comme le /finish
	 */
	@PostMapping("/finishUpdate/{id}")
	public String finishUpdatingBirthday(@PathVariable("id") Long id, Model model, @ModelAttribute Birthday birthday,
			@RequestParam("pizzaIds[]") List<Long> pizzaIds, @RequestParam("quantities[]") List<Integer> quantities,
			@RequestParam(value = "selectedPizzas[]", required = false) List<Long> selectedPizzas,
			@RequestParam("childrenIds") List<Child> selectedChildren) {
		Optional<Birthday> optionalBirthday = birthdayRepository.findById(birthday.getId());
		if (optionalBirthday.isEmpty()) {
			model.addAttribute("error", "L'anniversaire sélectionné n'existe pas.");
			return "viewEvent";
		} else {
			Birthday existingBirthday = optionalBirthday.get();
			if (selectedPizzas == null || selectedPizzas.isEmpty()) {
				model.addAttribute("error", "Veuillez selectionner au moins une pizza.");

				List<Restaurant> restaurants = restaurantRepository.findAll();
				List<Animatronic> animatronics = animatronicRepository.findByRestaurant(birthday.getRestaurant());
				List<Child> enfants = childRepository.findAll();
				model.addAttribute("children", enfants);
				enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
				model.addAttribute("childrenList", enfants);
				model.addAttribute("animatronics", animatronics);
				model.addAttribute("restaurants", restaurants);
				List<Pizza> pizzas = pizzaRepository.findAll();
				model.addAttribute("birthday", birthday);
				model.addAttribute("pizzas", pizzas);
				model.addAttribute("selectedChildren", selectedChildren);
				return "birthdayUpdateForm3";
			}
			List<PizzaOrder> orders = new ArrayList<>();
			for (PizzaOrder po : existingBirthday.getPizzaOrders()) {
				po.setBirthday(null);
				pizzaOrderRepository.save(po);
			}
			if (quantities.isEmpty()) {
				quantities.add(null);
			}
			for (int i = 0; i < pizzaIds.size(); i++) {
				Long pizzaId = pizzaIds.get(i);
				Integer quantity = quantities.get(i);
				if (selectedPizzas.contains(pizzaId)) {
					if (quantity == null || quantity < 0) {
						model.addAttribute("error",
								"Veuillez donner un nombre de pizza positif pour chaque pizza selectionnée.");

						List<Restaurant> restaurants = restaurantRepository.findAll();
						List<Animatronic> animatronics = animatronicRepository
								.findByRestaurant(birthday.getRestaurant());
						List<Child> enfants = childRepository.findAll();
						model.addAttribute("children", enfants);
						enfants = childRepository.findAllExceptThisOne(birthday.getBirthdayBoy().getId());
						model.addAttribute("childrenList", enfants);
						model.addAttribute("animatronics", animatronics);
						model.addAttribute("restaurants", restaurants);
						List<Pizza> pizzas = pizzaRepository.findAll();
						model.addAttribute("birthday", birthday);
						model.addAttribute("pizzas", pizzas);
						model.addAttribute("selectedChildren", selectedChildren);
						return "birthdayUpdateForm3";
					} else {
						Optional<Pizza> pizzaAct = pizzaRepository.findById(pizzaId);
						PizzaOrder order = new PizzaOrder(birthday, pizzaAct.get(), quantity);
						orders.add(order);
						order.setBirthday(birthday);

					}
				}
			}

			birthday.setPizzaOrders(orders);
			birthday.getRestaurant().getBirthdays().add(birthday);
			birthday.getAnimatronic1().getBirthdays1().add(birthday);

			if (birthday.getAnimatronic2() != null) {
				birthday.getAnimatronic2().getBirthdays2().add(birthday);
			}
			existingBirthday.getBirthdayBoy().setBirthday(null);
			childrenRepository.save(existingBirthday.getBirthdayBoy());
			birthday.getBirthdayBoy().setBirthday(birthday);
			childrenRepository.save(birthday.getBirthdayBoy());

			for (Child child : childrenRepository.findAll()) {
				if (child.getBirthdays().contains(existingBirthday)) {
					child.getBirthdays().remove(existingBirthday);
				}
				if (selectedChildren.contains(child)) {
					birthday.getChildren().add(child);
					child.getBirthdays().add(birthday);
				}
			}

			// Sauvegarder les entités mises à jour
			birthdayRepository.save(birthday);
			pizzaOrderRepository.saveAll(orders);
			childrenRepository.saveAll(childrenRepository.findAll());

			return "redirect:/";
		}
	}

}
