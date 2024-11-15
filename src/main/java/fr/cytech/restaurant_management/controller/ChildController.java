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
		Child child = new Child();
		child.setAge(0);
		child.setName("");
		child.setLastName("");
		model.addAttribute("child", child);
		return "childForm";
	}
	
	@PostMapping("/show")
	public String newChildrenResult(@ModelAttribute Child child, Model model) {
		if (child.getAge() <= 0) {
			model.addAttribute("child",child);
			model.addAttribute("error","Entrez un âge valide.");
			return "childForm";
		}
		else if (child.getLastName() == "" || child.getName() == "") {
			model.addAttribute("child",child);
			model.addAttribute("error","Completez toutes les informations.");
			return "childForm";
		}
		childRepository.save(child);
		return "redirect:/children/show";
	}
	
	@PostMapping("/delete/{id}")
	public String killChild(@PathVariable("id") Long id) {
		childRepository.deleteById(id);
		return "redirect:/children/show";
	}
	
	@GetMapping("/modify/{id}")
	public String changeChild(@PathVariable("id") Long id,Model model) {
		Optional<Child> optionalChild = childRepository.findById(id);
		
		if (optionalChild.isEmpty()) {
			return "redirect:/children/show";
		}
		else {
			model.addAttribute("child",optionalChild.get());
			return "childUpdateForm";
		}
	}
	
	@PostMapping("/update")
	public String changeChildResult(@ModelAttribute Child child) {
		Optional<Child> optionalChild = childRepository.findById(child.getId());
		
		if (optionalChild.isEmpty()) {
			return "redirect:/children/show";
		}
		else {
			Child existingChild = optionalChild.get();
			
			existingChild.setLastName(child.getLastName());
			existingChild.setName(child.getName());
			existingChild.setAge(child.getAge());
			childRepository.save(existingChild);
			return "redirect:/children/show";
		}
	}
	
	@GetMapping("/search")
	@ResponseBody
	public List<Child> searchChildren(@RequestParam("query") String query) {
		return childRepository.findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query,query);
	}
	
	
}
