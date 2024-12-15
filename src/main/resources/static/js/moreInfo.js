document.addEventListener("DOMContentLoaded", () => {
  const buttons = document.querySelectorAll("ul#eventList > li > button");

  // Afficher les détails lorsque le bouton est cliqué
  buttons.forEach((button) => {
    button.addEventListener("click", (event) => {
      const parentLi = event.target.closest("li");
      const detailDiv = parentLi.querySelector(".toDevoil");
      if (detailDiv) {
        detailDiv.style.display = "flex"; // Affiche les détails
      }
    });
  });

  // Délégation d'événements pour gérer le bouton "Fermer"
  document.querySelector("ul#eventList").addEventListener("click", (event) => {
    if (event.target.classList.contains("closeDetail")) {
      const detailDiv = event.target.closest(".toDevoil");
      if (detailDiv) {
        detailDiv.style.display = "none"; // Masque les détails
      }
    }
  });
});
