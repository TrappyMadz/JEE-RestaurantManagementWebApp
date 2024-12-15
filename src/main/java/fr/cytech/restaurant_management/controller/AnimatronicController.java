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

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.AnimatronicType;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;

@Controller
@RequestMapping("/animatronic")
public class AnimatronicController {

	private final String uploadDir = "uploads/img/animatronics";

	@Autowired
	AnimatronicRepository animatronicRepository;

	@GetMapping("/show")
	public String showAnimatronic(Model model) {
		List<Animatronic> animatronics = animatronicRepository.findAll();
		model.addAttribute("animatronics", animatronics);
		model.addAttribute("AnimatronicType", AnimatronicType.values());
		return "animatronics";
	}

	@GetMapping("/add")
	public String newAnimatronic(Model model) {
		Animatronic animatronic = new Animatronic();
		animatronic.setName("");
		animatronic.setImagePath("");
		model.addAttribute("animatronic", animatronic);
		model.addAttribute("AnimatronicType", AnimatronicType.values());
		return "animatronicForm";
	}

	@PostMapping("/show")
	public String newAnimatronicResult(@ModelAttribute Animatronic animatronic, @RequestParam("image") MultipartFile image, Model model) {
		if (animatronic.getName() == "") {
			model.addAttribute("animatronic", animatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			model.addAttribute("error", "Completez toutes les informations.");
			return "animatronicForm";
		}
		List<Animatronic> searchIfEmpty = animatronicRepository.findByNameAndType(animatronic.getName(),
				animatronic.getType());
		if (!searchIfEmpty.isEmpty()) {
			model.addAttribute("animatronic", animatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			model.addAttribute("error", "Cet animatronic existe déjà.");
			return "animatronicForm";
		}

		try {
			animatronicRepository.save(animatronic);

			String fileName = animatronic.getId() + "_" + StringUtils.cleanPath(image.getOriginalFilename());

			Path path = Paths.get(uploadDir, fileName);

			Files.createDirectories(path.getParent());

			Files.copy(image.getInputStream(), path);

			String imageUrl = "img/animatronics/" + fileName;
			animatronic.setImagePath(imageUrl);
			animatronicRepository.save(animatronic);
			return "redirect:/animatronic/show";
		} catch (Exception e) {
			model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
			model.addAttribute("animatronic", animatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			return "animatronicForm";
		}
	}

	/**
	 * Permet d'afficher les images
	 * 
	 * @param imgName chemin vers l'image (imagePath)
	 * @return l'image à afficher
	 */
	@GetMapping("/img/animatronics/{imgName}")
	public ResponseEntity<Resource> getAnimatronicImg(@PathVariable String imgName) {
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
	public String DismantleAnimatronic(@PathVariable("id") Long id) {
		Optional<Animatronic> toDelete = animatronicRepository.findById(id);
		if (toDelete.isEmpty()) {
			return "redirect:/animatronic/show";
		}
		try {
			String imagePathString = toDelete.get().getImagePath();

			String fileName = imagePathString.replace("/img/animatronics/", "");

			Path imagePath = Paths.get("uploads/", fileName);
			if (Files.exists(imagePath)) {
				Files.deleteIfExists(imagePath);
			}
		} catch (IOException e) {
			System.out.println("oopsie, le site plante, l'abus de supprimer l'image aussi....");
		}
		animatronicRepository.deleteById(id);
		return "redirect:/animatronic/show";
	}

	@GetMapping("/modify/{id}")
	public String changeAnimatronic(@PathVariable("id") Long id, Model model) {
		Optional<Animatronic> optionalAnimatronic = animatronicRepository.findById(id);

		if (optionalAnimatronic.isEmpty()) {
			model.addAttribute("error", "L'animatronique séléctionné n'existe pas.");
			return "redirect:/animatronic/show";
		} else {
			model.addAttribute("animatronic", optionalAnimatronic.get());
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			return "animatronicUpdateForm";
		}
	}

	@PostMapping("/update")
	public String rebuildTheAnimatronic(@ModelAttribute Animatronic animatronic, Model model,
			@RequestParam("image") MultipartFile image, @RequestParam("id") Long id) {
		Optional<Animatronic> optionalAnimatronic = animatronicRepository.findById(animatronic.getId());

		if (optionalAnimatronic.isEmpty()) {
			model.addAttribute("error", "L'animatronique séléctionné n'existe pas.");
			return "redirect:/animatronic/show";
		} else {
			Animatronic existingAnimatronic = optionalAnimatronic.get();

			existingAnimatronic.setName(animatronic.getName());
			existingAnimatronic.setType(animatronic.getType());
			if (existingAnimatronic.getName() == "") {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("animatronic", existingAnimatronic);
				model.addAttribute("AnimatronicType", AnimatronicType.values());
				return "animatronicUpdateForm";
			}
			List<Animatronic> searchIfEmpty = animatronicRepository.findByNameAndTypeExcludingId(existingAnimatronic.getName(),
					existingAnimatronic.getType(), existingAnimatronic.getId());
			if (!searchIfEmpty.isEmpty()) {
				model.addAttribute("animatronic", existingAnimatronic);
				model.addAttribute("AnimatronicType", AnimatronicType.values());
				model.addAttribute("error", "Cet animatronique existe déjà.");
				return "animatronicUpdateForm";
			}
			try {
				// On supprime l'image comme dans la fonction de suppression
				String imagePathString = optionalAnimatronic.get().getImagePath();
				String fileName = imagePathString.replace("/img/animatronics/", "");
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
				String imageUrl = "img/animatronics/" + fileName;
				existingAnimatronic.setImagePath(imageUrl);
			} catch (Exception e) {
				model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
				return "animatronicUpdateForm";
			}
			animatronicRepository.save(existingAnimatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			return "redirect:/animatronic/show";
		}
	}

	@GetMapping("/search")
	@ResponseBody
	public List<Animatronic> searchAnimatronic(@RequestParam("query") String query, @RequestParam(required = false) String type) {
	    if (type != null && !type.isEmpty()) {
	        // Recherche par nom et type
	        AnimatronicType animatronicType = AnimatronicType.valueOf(type);
	        return animatronicRepository.findByNameContainingIgnoreCaseAndType(query, animatronicType);
	    } else {
	        // Recherche uniquement par nom
	        return animatronicRepository.findByNameContainingIgnoreCase(query);
	    }
	}
}