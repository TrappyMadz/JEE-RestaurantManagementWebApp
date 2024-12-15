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

/**
 * Controller pour les fonctions concernant les animatroniques
 */
@Controller
@RequestMapping("/animatronic")
public class AnimatronicController {

	// Endroit où les images sont enregistrées
	private final String uploadDir = "uploads/img/animatronics";

	@Autowired
	AnimatronicRepository animatronicRepository;

	/**
	 * Affichage de la liste des animatroniques
	 * 
	 * @param model
	 * @return la page affichant les animatroniques
	 */
	@GetMapping("/show")
	public String showAnimatronic(Model model) {
		List<Animatronic> animatronics = animatronicRepository.findAll();
		model.addAttribute("animatronics", animatronics);

		// On récupère aussi les types d'Animatroniques (une enum)
		model.addAttribute("AnimatronicType", AnimatronicType.values());
		return "animatronics";
	}

	/**
	 * Initialisation de l'ajoue d'un Animatronique
	 * 
	 * @param model
	 * @return la page permettant d'ajouter un Animatronique
	 */
	@GetMapping("/add")
	public String newAnimatronic(Model model) {
		// On initialise un animatronique pour la préremplission du form
		Animatronic animatronic = new Animatronic();
		animatronic.setName("");
		animatronic.setImagePath("");
		model.addAttribute("animatronic", animatronic);
		model.addAttribute("AnimatronicType", AnimatronicType.values());
		return "animatronicForm";
	}

	/**
	 * Sauvegarde de l'Animatronique
	 * 
	 * @param animatronic Animatronique à sauvegarder
	 * @param image       Image de l'animatronique
	 * @param model
	 * @return La page listant tous les animatronique, après avoir enregistré
	 *         l'animatronique.
	 */
	@PostMapping("/show")
	public String newAnimatronicResult(@ModelAttribute Animatronic animatronic,
			@RequestParam("image") MultipartFile image, Model model) {
		// On test les cas d'erreurs possibles
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

		// On tente d'enregistrer l'animatronique, en gerant également la sauvegarde de
		// la photo
		try {
			animatronicRepository.save(animatronic);

			String fileName = animatronic.getId() + "_" + StringUtils.cleanPath(image.getOriginalFilename());

			// /uploads/img/animatronics/...
			Path path = Paths.get(uploadDir, fileName);

			Files.createDirectories(path.getParent());

			Files.copy(image.getInputStream(), path);

			String imageUrl = "img/animatronics/" + fileName;
			animatronic.setImagePath(imageUrl);
			animatronicRepository.save(animatronic);
			return "redirect:/animatronic/show";
		} catch (Exception e) {
			model.addAttribute("error", "Erreur de chargement d'image : " + e.getMessage());
			return "animatronicForm";
		}
	}

	/**
	 * Permet d'afficher les images (après une demande ThymeLeaf par exemple)
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

	/**
	 * Permet de supprimer un Animatronique
	 * 
	 * @param id id de l'animatronique
	 * @return page listant les animatroniques, après avoir supprimer
	 *         l'animatronique séléctionné si tout vas bien
	 */
	@PostMapping("/delete/{id}")
	public String DismantleAnimatronic(@PathVariable("id") Long id) {
		Optional<Animatronic> toDelete = animatronicRepository.findById(id);

		// Si l'id entré en paramètre n'est associé à aucun animatronique, alors on ne
		// fait rien
		if (toDelete.isEmpty()) {
			return "redirect:/animatronic/show";
		}

		// On suprime l'image
		try {
			String imagePathString = toDelete.get().getImagePath();

			String fileName = imagePathString.replace("/img/animatronics/", "");

			Path imagePath = Paths.get("uploads/", fileName);
			if (Files.exists(imagePath)) {
				Files.deleteIfExists(imagePath);
			}
		} catch (IOException e) {
			System.out.println("La suppression de l'image à échouée");
		}
		animatronicRepository.deleteById(id);
		return "redirect:/animatronic/show";
	}

	/**
	 * Modification d'un animatronique
	 * 
	 * @param id
	 * @param model
	 * @return le form de modification, après l'avoir initialisé avec les infos de
	 *         l'animatronique séléctionné.
	 */
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

	/**
	 * Application des modifications d'un annimatronique
	 * 
	 * @param animatronic animatronique du form
	 * @param model
	 * @param image       image à associée à l'animatronique
	 * @param id          id de l'animatronique à modifier
	 * @return sur la liste des animatroniques après l'avoir modifier.
	 */
	@PostMapping("/update")
	public String rebuildTheAnimatronic(@ModelAttribute Animatronic animatronic, Model model,
			@RequestParam("image") MultipartFile image, @RequestParam("id") Long id) {
		Optional<Animatronic> optionalAnimatronic = animatronicRepository.findById(animatronic.getId());

		if (optionalAnimatronic.isEmpty()) {
			model.addAttribute("error", "L'animatronique séléctionné n'existe pas.");
			return "redirect:/animatronic/show";
		} else {
			// On récupère les infos de l'animatronique déjà existant
			Animatronic existingAnimatronic = optionalAnimatronic.get();

			existingAnimatronic.setName(animatronic.getName());
			existingAnimatronic.setType(animatronic.getType());

			// On vérifie que tout soit bien définie
			if (existingAnimatronic.getName() == "") {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("animatronic", existingAnimatronic);
				model.addAttribute("AnimatronicType", AnimatronicType.values());
				return "animatronicUpdateForm";
			}
			List<Animatronic> searchIfEmpty = animatronicRepository.findByNameAndTypeExcludingId(
					existingAnimatronic.getName(), existingAnimatronic.getType(), existingAnimatronic.getId());
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
			// On sauvegarde le nouvel animatronique
			animatronicRepository.save(existingAnimatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			return "redirect:/animatronic/show";
		}
	}

	/**
	 * Recherche un animatronique par son nom ou son type (appelée en ajax)
	 * 
	 * @param query recherche du nom (peut être vide)
	 * @param type  recherche du type (menu déroulant, si rien n'est séléctionné,
	 *              value = "")
	 * @return
	 */
	@GetMapping("/search")
	@ResponseBody
	public List<Animatronic> searchAnimatronic(@RequestParam("query") String query,
			@RequestParam(required = false) String type) {
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