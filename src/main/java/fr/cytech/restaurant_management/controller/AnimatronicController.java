package fr.cytech.restaurant_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.repository.AnimatronicRepository;

@Controller
@RequestMapping("/animatronic")
public class AnimatronicController {

	@Autowired
	AnimatronicRepository animatronicRepository;
	
	@GetMapping("/show")
	public String showAnimatronic(Model model) {
		List<Animatronic> animatronics = animatronicRepository.findAll();
		model.addAttribute("animatronics",animatronics);
		return "animatronics";
	}
	
	@PostMapping("/add")
	public String addAnimatronic(@ModelAttribute Animatronic animatronic) {
		animatronicRepository.save(animatronic);
		return "animatronic/show";
	}

}