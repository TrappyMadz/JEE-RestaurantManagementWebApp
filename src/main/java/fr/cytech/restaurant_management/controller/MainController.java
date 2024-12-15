package fr.cytech.restaurant_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/attribution")
	public String showAttr(Model model) {
		return "attribution";
	}
	
	

}
