document.addEventListener("DOMContentLoaded", () => {
  // Sélectionne tous les boutons "Voir les animatroniques"
  const viewButtons = document.querySelectorAll(".viewAnimatronics");

  viewButtons.forEach((button) => {
    button.addEventListener("click", () => {
      // Trouve l'élément parent `<li>` contenant ce bouton
      const parentLi = button.closest("li");

      if (parentLi) {
        // Récupère l'ID du restaurant depuis l'élément parent
        const restaurantId = parentLi
          .querySelector(".toDevoil")
          .id.split("-")[1];

        if (restaurantId) {
          // Trouve l'élément à afficher ou cacher
          const toDevoil = document.getElementById(`toDevoil-${restaurantId}`);

          if (toDevoil) {
            // Alterne entre afficher/cacher
            toDevoil.style.display =
              toDevoil.style.display === "none" ? "flex" : "none";
          } else {
            console.error(`Aucun élément avec l'ID toDevoil-${restaurantId}`);
          }
        } else {
          console.error("Impossible de trouver l'ID du restaurant.");
        }
      } else {
        console.error("Le bouton n'est pas dans un élément <li> valide.");
      }
    });
  });

  // Ajoute un événement pour fermer les détails
  const closeButtons = document.querySelectorAll(".closeDetail");

  closeButtons.forEach((closeButton) => {
    closeButton.addEventListener("click", () => {
      // Ferme le parent direct de ce bouton (section .toDevoil)
      const toDevoil = closeButton.closest(".toDevoil");
      if (toDevoil) {
        toDevoil.style.display = "none";
      }
    });
  });
});
