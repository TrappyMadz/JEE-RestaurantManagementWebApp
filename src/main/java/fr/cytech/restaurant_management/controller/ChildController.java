package fr.cytech.restaurant_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cytech.restaurant_management.entity.Child;
import fr.cytech.restaurant_management.repository.ChildRepository;

@Controller
@RequestMapping("/children")
public class ChildController {
	
	@Autowired
	ChildRepository childRepository;
	
	@GetMapping("/show")
	public String showChildren(Model model) {
		List<Child> children = childRepository.findAll();
		model.addAttribute("children",children);
		return "children";
	}
	
	@GetMapping("/add")
	public String newChildren(Model model) {
		return "childForm";
	}
	
	@PostMapping("/show")
	public String newChildrenResult(@ModelAttribute Child child) {
		childRepository.save(child);
		return "redirect:/children/show";
	}
}
