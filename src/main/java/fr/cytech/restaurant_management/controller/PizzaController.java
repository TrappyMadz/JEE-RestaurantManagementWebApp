package fr.cytech.restaurant_management.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import fr.cytech.restaurant_management.entity.Pizza;
import fr.cytech.restaurant_management.repository.PizzaRepository;

/**
 * Controller pour l'affichage et la gestion des pizzas
 */
@Controller
@RequestMapping("/pizza")
public class PizzaController {

	// Chemin d'upload pour les pizzas. Il est situé à la racine du projet, au même endroit que le dossier src
	private final String uploadDir = "uploads/img/pizzas";

	@Autowired
	private PizzaRepository pizzaRepository;

	/**
	 * Permet d'afficher la liste des pizza
	 * @param model
	 * @return la page d'affichage de la liste des pizzas, pizza.html
	 */
	@GetMapping("/show")
	public String showPizza(Model model) {
		List<Pizza> pizzas = pizzaRepository.findAll();
		model.addAttribute("pizzas", pizzas);
		return "pizza";
	}

	/**
	 * Initialise l'ajout des pizza en envoyant l'utilisateur sur le form correspondant
	 * @param model
	 * @return la page permettant d'ajouter une nouvelle pizza
	 */
	@GetMapping("/add")
	public String newPizza(Model model) {
		// On initialise une pizza vide pour que si l'utilisateur fait une erreur, il n'ait pas à tout recommencer.
		// Ce système requiert qu'un enfant soit initialisé avant le début du form
		Pizza pizza = new Pizza();
		pizza.setImagePath("");
		pizza.setName("");
		pizza.setPrice(0);
		model.addAttribute("pizza", pizza);
		return "pizzaForm";
	}

	/**
	 * Finalise l'ajout d'une pizza à la base de donnée
	 * @param pizza pizza à ajoutée
	 * @param image image associée à la pizza
	 * @param model
	 * @return si il y a un problème, on reviens sur la page de form, sinon on retourne à la liste des pizzas
	 */
	@PostMapping("/show")
	public String addPizza(@ModelAttribute Pizza pizza, @RequestParam("image") MultipartFile image, Model model) {

		// On vérifie si les champs sont bien remplies
		if (pizza.getName().isBlank() || pizza.getComposition().isBlank()) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("error", "Veuillez entrer toutes les informations");
			return "pizzaForm";
		}
		// On vérifie si le prix n'est pas négatif, ce qui n'aurait aucun sens
		if (pizza.getPrice() < 0) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("error", "Veuillez entrer un prix valide");
			return "pizzaForm";
		}

		try {
			// On sauvegarde la pizza une première fois sans imagePath pour generer une id
			pizzaRepository.save(pizza);

			// On récupère le nom du fichier et on concatène l'id de la pizza au début pour que chaque image soit distincte
			String fileName = pizza.getId() + "_" + StringUtils.cleanPath(image.getOriginalFilename());

			// On récupère l'endroit où on doit enregistrer l'image
			Path path = Paths.get(uploadDir, fileName);

			// Si les dossiers n'existent pas, on les créer
			Files.createDirectories(path.getParent());

			// On copie l'image au bon endroit
			Files.copy(image.getInputStream(), path);

			// On attribut l'imagePath et on enregistre la pizza dans la bdd
			String imageUrl = "img/pizzas/" + fileName;
			pizza.setImagePath(imageUrl);
			pizzaRepository.save(pizza);
			return "redirect:/pizza/show";
		} catch (Exception e) {
			model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
			return "pizzaForm";
		}
	}

	/**
	 * Permet d'afficher les images
	 * @param imgName chemin vers l'image (imagePath)
	 * @return l'image à afficher
	 */
	@GetMapping("/img/pizzas/{imgName}")
	public ResponseEntity<Resource> getPizzaImg(@PathVariable String imgName) {
		try {
			// On récupère l'image si elle existe
			Path path = Paths.get(uploadDir).resolve(imgName);
			Resource resource = new FileSystemResource(path);
			if (resource.exists()) {
				String contentType = Files.probeContentType(path);
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
						.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"").body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Suppression d'une pizza
	 * @param id id de la pizza à supprimer
	 * @return retour à la liste des pizzas après suppression
	 */
	@PostMapping("/delete/{id}")
	public String eatPizza(@PathVariable("id") Long id) {
		// Si la pizza n'existe pas, Optional sera vide
		Optional<Pizza> toDelete = pizzaRepository.findById(id);

		// Si l'objet Optional est vide, la pizza n'existe pas. On renvoi donc à la liste des pizzas
		if (toDelete.isEmpty()) {
			return "redirect:/pizza/show";
		}
		try {
			// On veut supprimer l'image associée à la pizza. On récupère donc la variable imagePath
			String imagePathString = toDelete.get().getImagePath();

			// L'imagePath est enregistrée tel que imagePath = /img/pizzas/nomImage
			// Pour la suite, on a besoin du nom de l'image, donc on retire ce qu'il y a avant
			String fileName = imagePathString.replace("/img/pizzas/", "");

			// On concatène le chemin "uploads" pour retrouver le fichier, et si une image existe à cet endroit, on la supprime.
			Path imagePath = Paths.get("uploads/", fileName);
			if (Files.exists(imagePath)) {
				Files.deleteIfExists(imagePath);
			}
		} catch (IOException e) {
			// En cas d'erreur lors de la suppression du fichier
		}

		// On supprime ensuite la pizza
		pizzaRepository.delete(toDelete.get());
		return "redirect:/pizza/show";
	}

	/**
	 * Initialise la modification d'une pizza
	 * @param id id de la pizza à modifier
	 * @param model
	 * @return au form de modification si la pizza existe, à la liste des pizzas sinon
	 */
	@GetMapping("/modify/{id}")
	public String changePizza(@PathVariable("id") Long id, Model model) {
		Optional<Pizza> optionalPizza = pizzaRepository.findById(id);

		if (optionalPizza.isEmpty()) {
			model.addAttribute("error", "La pizza séléctionnée n'existe pas.");
			return "redirect:/pizza/show";
		} else {
			// Si la pizza existe, on préremplie le form
			model.addAttribute("pizza", optionalPizza.get());
			return "pizzaUpdateForm";
		}

	}

	/**
	 * Applique la modification de la pizza dans la bdd
	 * @param pizza pizza modifiée
	 * @param model
	 * @param image nouvelle image
	 * @param id id de la pizza à modifier
	 * @return sur le form si il y a un problème, sur la liste des pizzas sinon
	 */
	@PostMapping("/update")
	public String changePizzaResult(@ModelAttribute Pizza pizza, Model model,
			@RequestParam("image") MultipartFile image, @RequestParam("id") Long id) {
		Optional<Pizza> optionalPizza = pizzaRepository.findById(id);

		if (optionalPizza.isEmpty()) {
			model.addAttribute("error", "La pizza séléctionnée n'existe pas.");
			return "redirect:/pizza/show";
		} else {
			Pizza existingPizza = optionalPizza.get();

			// On actualise les attributs de la pizza existante pour les remplacer par ceux du form
			existingPizza.setComposition(pizza.getComposition());
			existingPizza.setName(pizza.getName());
			existingPizza.setPrice(pizza.getPrice());

			// On vérifie si la pizza est valide. Si elle ne l'est pas, on renvoie sur le form
			if (existingPizza.getComposition().isBlank() || existingPizza.getImagePath().isBlank()
					|| existingPizza.getName().isBlank()) {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("pizza", existingPizza);
				return "pizzaUpdateForm";
			}
			if (existingPizza.getPrice() < 0) {
				model.addAttribute("error", "Entrez un prix valide.");
				model.addAttribute("pizza", existingPizza);
				return "pizzaUpdateForm";
			}

			try {
				// On supprime l'image comme dans la fonction de suppression
				String imagePathString = optionalPizza.get().getImagePath();
				String fileName = imagePathString.replace("/img/pizzas/", "");
				Path imagePath = Paths.get("uploads/", fileName);
				if (Files.exists(imagePath)) {
					Files.deleteIfExists(imagePath);
				}
			} catch (IOException e) {
				// En cas d'erreur lors de la suppression du fichier
			}

			try {
				// Et on copie la nouvelle image comme dans la fonction d'ajout
				String fileName = id + "_" + StringUtils.cleanPath(image.getOriginalFilename());
				Path path = Paths.get(uploadDir, fileName);
				Files.createDirectories(path.getParent());
				Files.copy(image.getInputStream(), path);
				String imageUrl = "img/pizzas/" + fileName;
				existingPizza.setImagePath(imageUrl);
			} catch (Exception e) {
				model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
				return "pizzaForm";
			}

			pizzaRepository.save(existingPizza);
			return "redirect:/pizza/show";
		}

	}

	/**
	 * Fonction permettant de chercher une pizza via son nom ou ses ingredients
	 * @param query recherche
	 * @return liste de pizzas répondant à la recherche
	 */
	@GetMapping("/search")
	@ResponseBody
	public List<Pizza> searchpizzas(@RequestParam("query") String query) {
		return pizzaRepository.findByNameContainingIgnoreCaseOrCompositionContainingIgnoreCase(query, query);
	}
}
