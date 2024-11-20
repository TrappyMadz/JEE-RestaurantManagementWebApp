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
import fr.cytech.restaurant_management.entity.AnimatronicType;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;

@Controller
@RequestMapping("/animatronic")
public class AnimatronicController {

	@Autowired
	AnimatronicRepository animatronicRepository;

	@GetMapping("/show")
	public String showAnimatronic(Model model) {
		List<Animatronic> animatronics = animatronicRepository.findAll();
		model.addAttribute("animatronics", animatronics);
		return "animatronics";
	}

	@GetMapping("/add")
	public String newAnimatronic(Model model) {
		Animatronic animatronic = new Animatronic();
		animatronic.setName("");
		model.addAttribute("animatronic", animatronic);
		model.addAttribute("AnimatronicType", AnimatronicType.values());
		return "createAnimatronicForm";
	}

	@PostMapping("/show")
	public String newAnimatronicResult(@ModelAttribute Animatronic animatronic, Model model) {
		if (animatronic.getName() == "") {
			model.addAttribute("animatronic", animatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			model.addAttribute("error", "Completez toutes les informations.");
			return "createAnimatronicForm";
		}
		List<Animatronic> searchIfEmpty = animatronicRepository.findByNameAndType(animatronic.getName(),
				animatronic.getType());
		if (!searchIfEmpty.isEmpty()) {
			model.addAttribute("animatronic", animatronic);
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			model.addAttribute("error", "Cet animatronic existe déjà.");
			return "createAnimatronicForm";
		}

		animatronicRepository.save(animatronic);
		return "redirect:/animatronic/show";
	}

	@PostMapping("/delete/{id}")
	public String DismantleAnimatronic(@PathVariable("id") Long id) {
		animatronicRepository.deleteById(id);
		return "redirect:/animatronic/show";
	}

	@GetMapping("/modify/{id}")
	public String changeAnimatronic(@PathVariable("id") Long id, Model model) {
		Optional<Animatronic> optionalAnimatronic = animatronicRepository.findById(id);

		if (optionalAnimatronic.isEmpty()) {
			model.addAttribute("error", "L'animatronic séléctionné n'existe pas.");
			return "redirect:/animatronic/show";
		} else {
			model.addAttribute("animatronic", optionalAnimatronic.get());
			model.addAttribute("AnimatronicType", AnimatronicType.values());
			return "animatronicUpdateForm";
		}
	}

	@PostMapping("/update")
	public String rebuildTheAnimatronic(@ModelAttribute Animatronic animatronic, Model model) {
		Optional<Animatronic> optionalAnimatronic = animatronicRepository.findById(animatronic.getId());

		if (optionalAnimatronic.isEmpty()) {
			model.addAttribute("error", "L'animatronic séléctionné n'existe pas.");
			return "redirect:/animatronic/show";
		} else {
			Animatronic existingAnimatronic = optionalAnimatronic.get();
			model.addAttribute("animatronic", existingAnimatronic);
			existingAnimatronic.setName(animatronic.getName());
			if (existingAnimatronic.getName() == "") {
				model.addAttribute("error", "Completez toutes les informations.");
				model.addAttribute("animatronic", existingAnimatronic);
				model.addAttribute("AnimatronicType", AnimatronicType.values());
				return "animatronicUpdateForm";
			}
			List<Animatronic> searchIfEmpty = animatronicRepository.findByNameAndType(existingAnimatronic.getName(),
					existingAnimatronic.getType());
			if (!searchIfEmpty.isEmpty()) {
				model.addAttribute("animatronic", existingAnimatronic);
				model.addAttribute("AnimatronicType", AnimatronicType.values());
				model.addAttribute("error", "Cet animatronic existe déjà.");
				return "animatronicUpdateForm";
			}
			animatronicRepository.save(existingAnimatronic);
			return "redirect:/animatronic/show";
		}
	}

	@GetMapping("/search")
	@ResponseBody
	public List<Animatronic> searchAnimatronic(@RequestParam("query") String query) {
		return animatronicRepository.findByNameContainingIgnoreCase(query);
	}

}