package fr.cytech.restaurant_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.cytech.restaurant_management.entity.Child;
import fr.cytech.restaurant_management.repository.ChildRepository;

@Controller
public class ChildController {
	
	@Autowired
	ChildRepository childRepository;
	
	@GetMapping("/children")
	public String showChildren(Model model) {
		List<Child> children = childRepository.findAll();
		model.addAttribute("children",children);
		return "children";
	}
}
