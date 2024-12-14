package fr.cytech.restaurant_management.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Restaurant;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;
import fr.cytech.restaurant_management.repository.RestaurantRepository;

/**
 * Controller de la class Restaurant (accessible via l'adresse /restaurant)
 */
@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	AnimatronicRepository animatronicRepository;

	/**
	 * Permet de montrer la liste des restaurants
	 * 
	 * @param model
	 * @return la page permettant de lister les restaurants
	 */
	@GetMapping("/show")
	public String showRestaurant(Model model) {
		// On récupère les restaurants pour les afficher
		List<Restaurant> restaurantList = restaurantRepository.findAll();
		model.addAttribute("restaurantList", restaurantList);
		model.addAttribute("ChooseAnimatronic", animatronicRepository.findAll());
		return "restaurants";
	}

	/**
	 * Fonction permettant d'initialiser la page d'ajout de restaurant
	 * 
	 * @param model
	 * @return le formulaire d'ajout de restaurant
	 */
	@GetMapping("/add")
	public String openRestaurant(Model model) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("");
		restaurant.setAddress("");
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("animatronics", animatronicRepository.findThoseWithoutRestaurant());
		return "restaurantForm";
	}

	/**
	 * Gère l'ajout du restaurant dans la bdd
	 * 
	 * @param restaurant Le restaurant à ajouter
	 * @param model
	 * @return retour au formulaire si il y a un problème, sinon retour à la liste
	 *         des restaurants après avoir geré l'ajout
	 */
	@PostMapping("/show")
	public String openRestaurantResult(@ModelAttribute Restaurant restaurant,  Model model, 
            @RequestParam(required = false) List<Long> animatronicIds) {
		// Vérification du nom et de l'adresse
		if (animatronicIds != null && !animatronicIds.isEmpty()) {
	        List<Animatronic> selectedAnimatronics = animatronicRepository.findAllById(animatronicIds);
	        restaurant.setAnimatronics(selectedAnimatronics);
	        
	        // Il est important de définir le restaurant pour chaque animatronique sélectionné
	        for (Animatronic animatronic : selectedAnimatronics) {
	            animatronic.setRestaurant(restaurant);
	        }
	    }
		if (restaurant.getName() == "" || restaurant.getAddress() == "") {
			model.addAttribute("restaurant", restaurant);
			model.addAttribute("error", "Completez toutes les informations.");
			model.addAttribute("animatronics", animatronicRepository.findThoseWithoutRestaurant());
			return "restaurantForm";
		}
		restaurantRepository.save(restaurant);
		return "redirect:/restaurant/show";
	}

	/**
	 * Fonction permettant de supprimer un restaurant
	 * 
	 * @param id id du restaurant à supprimer
	 * @return retour à la liste des restaurants
	 */
	@PostMapping("/delete/{id}")
	public String burnTheRestaurant(@PathVariable("id") Long id) {
		restaurantRepository.deleteById(id);
		return "redirect:/restaurant/show";
	}

	/**
	 * Fonction permettant d'initialiser les modifications d'un restaurant
	 * 
	 * @param id    id du restaurant choisis
	 * @param model
	 * @return liste des restaurants si le restaurant n'existe pas, le formulaire de
	 *         modifications sinon
	 */
	@GetMapping("/modify/{id}")
	public String relocateRestaurant(@PathVariable("id") Long id, Model model) {
		// Le type optionnel est là car on n'est pas sûr que le restaurant existe
		Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

		// Si le restaurant n'existe pas
		if (optionalRestaurant.isEmpty()) {
			model.addAttribute("error", "Le restaurant séléctionné n'existe pas.");
			return "redirect:/restaurant/show";
		} else {
			model.addAttribute("restaurant", optionalRestaurant.get());
			model.addAttribute("animatronics", animatronicRepository.findThoseWithoutRestaurantOrSameRestaurant(optionalRestaurant.get()));
			return "restaurantUpdateForm";
		}

	}

	/**
	 * Mise à jour de la bdd après avoir modifié le restaurant
	 * 
	 * @param restaurant le restaurant modifié
	 * @param model
	 * @return liste des restaurants si tout s'est bien passé, retour sur le form
	 *         sinon
	 */
	@PostMapping("/update")
	public String relocateRestaurantResult(@ModelAttribute Restaurant restaurant, Model model, 
            @RequestParam(required = false) List<Long> animatronicIds) {
		// On récupère le restaurant non-modifié
		Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurant.getId());

		// On gère toujours l'erreur de "si le restaurant n'existe pas..."
		if (optionalRestaurant.isEmpty()) {
			model.addAttribute("error", "Le restaurant séléctionné n'existe pas.");
			return "redirect:/restaurant/show";
		} else {
			// On récupère les infos du form
			Restaurant existingRestaurant = optionalRestaurant.get();
			model.addAttribute("restaurant", existingRestaurant);
			existingRestaurant.setAddress(restaurant.getAddress());
			existingRestaurant.setName(restaurant.getName());

			// On vérifie le nom et l'adresse
			if (existingRestaurant.getName() == "" || existingRestaurant.getAddress() == "") {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("restaurant", existingRestaurant);
				model.addAttribute("animatronics", animatronicRepository.findThoseWithoutRestaurantOrSameRestaurant(existingRestaurant));
				return "restaurantUpdateForm";
			} else {
				for (Animatronic animatronic : animatronicRepository.findThoseWithoutRestaurantOrSameRestaurant(existingRestaurant)) {
					animatronic.setRestaurant(null);
				}
				if (animatronicIds != null && !animatronicIds.isEmpty()) {
			        List<Animatronic> selectedAnimatronics = animatronicRepository.findAllById(animatronicIds);
			        restaurant.setAnimatronics(selectedAnimatronics);
			        
			        // Il est important de définir le restaurant pour chaque animatronique sélectionné
			        for (Animatronic animatronic : selectedAnimatronics) {
			            animatronic.setRestaurant(restaurant);
			        }
			    }
				restaurantRepository.save(existingRestaurant);
				return "redirect:/restaurant/show";
			}
		}
	}

	/**
	 * Fonction permettant de chercher les restaurants
	 * 
	 * @param query la phrase recherchée par l'utilisateur
	 * @return La liste des restaurants concernés par la recherche
	 */
	@GetMapping("/search")
	@ResponseBody
	public List<Restaurant> searchRestaurant(@RequestParam(required = false) String query, @RequestParam(required = false) Long animatronic) {
		if (animatronic != null) {
			Animatronic ExistingAnimatronic = animatronicRepository.findById(animatronic)
	                .orElseThrow(() -> new IllegalArgumentException("Animatronic non trouvé pour l'ID : " + animatronic));
	        return List.of(ExistingAnimatronic.getRestaurant());
	    } else {
	        // Recherche uniquement par nom
	    	return restaurantRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(query, query);
	    }
	}

}
