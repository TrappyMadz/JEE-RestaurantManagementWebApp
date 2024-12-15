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

import fr.cytech.restaurant_management.entity.Child;
import fr.cytech.restaurant_management.repository.ChildRepository;

/**
 * Controller de la class Child (accessible via l'adresse /children)
 */
@Controller
@RequestMapping("/children")
public class ChildController {

	@Autowired
	ChildRepository childRepository;

	/**
	 * Permet de montrer la liste des enfants
	 * 
	 * @param model
	 * @return la page permettant de lister les enfants
	 */
	@GetMapping("/show")
	public String showChildren(Model model) {
		// On récupère les enfants pour les afficher
		List<Child> children = childRepository.findAll();
		model.addAttribute("children", children);
		return "children";
	}

	/**
	 * Fonction permettant d'initialiser la page d'ajout d'enfant
	 * 
	 * @param model
	 * @return le formulaire d'ajout d'enfant
	 */
	@GetMapping("/add")
	public String newChildren(Model model) {
		/*
		 * On créer un enfant "vide" pour pouvoir initialiser le formulaire. Il sera
		 * prérempli en cas d'erreur utilisateur (pour ne pas avoir à tout refaire en
		 * cas d'erreurs)
		 */
		Child child = new Child();
		child.setAge(0);
		child.setName("");
		child.setLastName("");
		model.addAttribute("child", child);
		return "childForm";
	}

	/**
	 * Gère l'ajout d'enfants dans la bdd
	 * 
	 * @param child L'enfant à ajouter
	 * @param model
	 * @return retour au formulaire si il y a un problème, sinon retour à la liste
	 *         des enfants après avoir geré l'ajout
	 */
	@PostMapping("/show")
	public String newChildrenResult(@ModelAttribute Child child, Model model) {

		// Vérification de l'age
		if (child.getAge() <= 0) {
			model.addAttribute("child", child);
			model.addAttribute("error", "Entrez un âge valide.");
			return "childForm";
		}
		// Vérification du nom et du prénom
		else if (child.getLastName() == "" || child.getName() == "") {
			model.addAttribute("child", child);
			model.addAttribute("error", "Completez toutes les informations.");
			return "childForm";
		}
		childRepository.save(child);
		return "redirect:/children/show";
	}

	/**
	 * Fonction permettant de supprimer un enfant
	 * 
	 * @param id id de l'enfant à supprimer
	 * @return retour à la liste des enfants
	 */
	@PostMapping("/delete/{id}")
	public String killChild(@PathVariable("id") Long id) {
		childRepository.deleteById(id);
		return "redirect:/children/show";
	}

	/**
	 * Fonction permettant d'initialiser les modifications d'un enfant
	 * 
	 * @param id    id de l'enfant choisis
	 * @param model
	 * @return liste des enfants si l'enfant n'existe pas, le formulaire de
	 *         modifications sinon
	 */
	@GetMapping("/modify/{id}")
	public String changeChild(@PathVariable("id") Long id, Model model) {
		// Le type optionnel est là car on n'est pas sûr que l'enfant existe
		Optional<Child> optionalChild = childRepository.findById(id);

		// Si l'enfant n'existe pas
		if (optionalChild.isEmpty()) {
			model.addAttribute("error", "L'enfant séléctionné n'existe pas.");
			return "redirect:/children/show";
		} else {
			model.addAttribute("child", optionalChild.get());
			return "childUpdateForm";
		}

	}

	/**
	 * Mise à jour de la bdd après avoir modifié l'enfant
	 * 
	 * @param child l'enfant modifié
	 * @param model
	 * @return liste des enfants si tout s'est bien passé, retour sur le form sinon
	 */
	@PostMapping("/update")
	public String changeChildResult(@ModelAttribute Child child, Model model) {
		// On récupère l'enfant non-modifié
		Optional<Child> optionalChild = childRepository.findById(child.getId());

		// On gère toujours l'erreur de "si l'enfant n'existe pas..."
		if (optionalChild.isEmpty()) {
			model.addAttribute("error", "L'enfant séléctionné n'existe pas.");
			return "redirect:/children/show";
		} else {
			// On récupère les infos du form
			Child existingChild = optionalChild.get();
			model.addAttribute("child", existingChild);
			existingChild.setLastName(child.getLastName());
			existingChild.setName(child.getName());
			existingChild.setAge(child.getAge());

			// On vérifie l'âge, le nom et le prénom
			if (existingChild.getAge() <= 0) {
				model.addAttribute("error", "Entrez un âge valide.");
				model.addAttribute("child", existingChild);
				return "childUpdateForm";
			} else if (existingChild.getLastName() == "" || existingChild.getName() == "") {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("child", existingChild);
				return "childUpdateForm";
			} else {
				childRepository.save(existingChild);
				return "redirect:/children/show";
			}
		}
	}

	/**
	 * Fonction permettant de chercher les enfants
	 * 
	 * @param query la phrase recherchée par l'utilisateur
	 * @return La liste des enfants concernés par la recherche
	 */
	@GetMapping("/search")
	@ResponseBody
	public List<Child> searchChildren(@RequestParam("query") String query) {
		return childRepository.findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
	}

}
