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

@Controller
@RequestMapping("/pizza")
public class PizzaController {

	private final String uploadDir = "uploads/img/pizzas";

	@Autowired
	private PizzaRepository pizzaRepository;

	@GetMapping("/show")
	public String showPizza(Model model) {
		List<Pizza> pizzas = pizzaRepository.findAll();
		model.addAttribute("pizzas", pizzas);
		return "pizza";
	}

	@GetMapping("/add")
	public String newPizza(Model model) {
		Pizza pizza = new Pizza();
		pizza.setImagePath("");
		pizza.setName("");
		pizza.setPrice(0);
		model.addAttribute("pizza", pizza);
		return "pizzaForm";
	}

	@PostMapping("/show")
	public String addPizza(@ModelAttribute Pizza pizza, @RequestParam("image") MultipartFile image, Model model) {

		if (pizza.getName().isBlank() || pizza.getComposition().isBlank()) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("error", "Veuillez entrer toutes les informations");
			return "pizzaForm";
		}
		if (pizza.getPrice() < 0) {
			model.addAttribute("pizza", pizza);
			model.addAttribute("error", "Veuillez entrer un prix valide");
			return "pizzaForm";
		}

		try {
			pizzaRepository.save(pizza);
			String fileName = pizza.getId() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
			Path path = Paths.get(uploadDir, fileName);
			Files.createDirectories(path.getParent());
			Files.copy(image.getInputStream(), path);
			String imageUrl = "img/pizzas/" + fileName;
			pizza.setImagePath(imageUrl);
			pizzaRepository.save(pizza);
			return "redirect:/pizza/show";
		} catch (Exception e) {
			model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
			return "pizzaForm";
		}
	}

	@GetMapping("/img/pizzas/{imgName}")
	public ResponseEntity<Resource> getPizzaImg(@PathVariable String imgName) {
		try {
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

	@PostMapping("/delete/{id}")
	public String killPizza(@PathVariable("id") Long id) {
		Optional<Pizza> toDelete = pizzaRepository.findById(id);

		if (toDelete.isEmpty()) {
			return "redirect:/pizza/show";
		}
		try {
			String imagePathString = toDelete.get().getImagePath();

			String fileName = imagePathString.replace("/img/pizzas/", "");

			Path imagePath = Paths.get("uploads/", fileName);
			if (Files.exists(imagePath)) {
				Files.deleteIfExists(imagePath);
			}
		} catch (IOException e) {
			// En cas d'erreur lors de la suppression du fichier
		}

		pizzaRepository.delete(toDelete.get());
		return "redirect:/pizza/show";
	}

	@GetMapping("/modify/{id}")
	public String changePizza(@PathVariable("id") Long id, Model model) {
		Optional<Pizza> optionalPizza = pizzaRepository.findById(id);

		if (optionalPizza.isEmpty()) {
			model.addAttribute("error", "La pizza séléctionnée n'existe pas.");
			return "redirect:/pizza/show";
		} else {
			model.addAttribute("pizza", optionalPizza.get());
			return "pizzaUpdateForm";
		}

	}

	@PostMapping("/update")
	public String changePizzaResult(@ModelAttribute Pizza pizza, Model model,
			@RequestParam("image") MultipartFile image, @RequestParam("id") Long id) {
		Optional<Pizza> optionalPizza = pizzaRepository.findById(id);

		if (optionalPizza.isEmpty()) {
			model.addAttribute("error", "La pizza séléctionnée n'existe pas.");
			return "redirect:/pizza/show";
		} else {
			Pizza existingPizza = optionalPizza.get();

			existingPizza.setComposition(pizza.getComposition());
			existingPizza.setName(pizza.getName());
			existingPizza.setPrice(pizza.getPrice());
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

	@GetMapping("/search")
	@ResponseBody
	public List<Pizza> searchpizzas(@RequestParam("query") String query) {
		return pizzaRepository.findByNameContainingIgnoreCaseOrCompositionContainingIgnoreCase(query, query);
	}
}
