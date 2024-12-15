package fr.cytech.restaurant_management.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cytech.restaurant_management.entity.Animatronic;
import fr.cytech.restaurant_management.entity.Birthday;
import fr.cytech.restaurant_management.entity.PizzaOrder;
import fr.cytech.restaurant_management.entity.Restaurant;
import fr.cytech.restaurant_management.repository.BirthdayRepository;
import fr.cytech.restaurant_management.repository.PizzaOrderRepository;
import fr.cytech.restaurant_management.repository.RestaurantRepository;

@Controller
public class MainController {

    @Autowired
    BirthdayRepository birthdayRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    PizzaOrderRepository pizzaOrderRepository;

    @GetMapping("/attribution")
    public String showAttr(Model model) {
        return "attribution";
    }

    @GetMapping("/viewSchedule")
    public String toDo(@RequestParam(value = "restaurantId", required = false) Long restaurantId, Model model) {
        // Récupérer la liste des restaurants pour l'afficher dans le menu déroulant
        List<Restaurant> restaurants = restaurantRepository.findAll();
        model.addAttribute("restaurants", restaurants);

        // Si aucun restaurant n'est sélectionné, ne rien afficher
        if (restaurantId == null) {
            return "schedule"; // Aucun contenu n'est ajouté au modèle
        }

        // Récupérer les anniversaires pour aujourd'hui
        List<Birthday> birthdays = birthdayRepository.findByDate(LocalDate.now());
        boolean found = false;
        List<PizzaOrder> mergedPizzaOrders = new ArrayList<>();
        Set<Animatronic> mergedAnimatronics = new HashSet<>();

        for (Birthday b : birthdays) {
            // Filtrer les anniversaires en fonction du restaurant sélectionné
            if (!b.getRestaurant().getId().equals(restaurantId)) {
                continue;  // Ignorer cet anniversaire si le restaurant ne correspond
            }

            // Filtrer les commandes de pizzas pour le restaurant sélectionné
            for (PizzaOrder orders : b.getPizzaOrders()) {
                if (!orders.getBirthday().getRestaurant().getId().equals(restaurantId)) {
                    continue;  // Ignorer cette commande si le restaurant ne correspond
                }

                found = false;
                for (int i = 0; i < mergedPizzaOrders.size(); i++) {
                    if (mergedPizzaOrders.get(i).getPizza() == orders.getPizza()) {
                        found = true;
                        mergedPizzaOrders.get(i).setNbPizza(mergedPizzaOrders.get(i).getNbPizza() + 1);
                    }
                }
                if (!found) {
                    mergedPizzaOrders.add(orders);
                }
            }

            // Ajouter les animatroniques associés à ce restaurant
            if (b.getAnimatronic1() != null && b.getAnimatronic1().getRestaurant().getId().equals(restaurantId)) {
                mergedAnimatronics.add(b.getAnimatronic1());
            }
            if (b.getAnimatronic2() != null && b.getAnimatronic2().getRestaurant().getId().equals(restaurantId)) {
                mergedAnimatronics.add(b.getAnimatronic2());
            }
        }

        // Ajouter les données filtrées au modèle
        model.addAttribute("orders", mergedPizzaOrders);
        model.addAttribute("birthdays", birthdays);
        model.addAttribute("animatronics", mergedAnimatronics);

        return "schedule";
    }
}
