// On ajoute un écouteur d'évènement à la barre de recherche
document.getElementById("searchQuery").addEventListener("input", setup);

function setup() {
  // On récupère le contenu de la barre de recherche
  let query = this.value;

  // On attend que l'utilisateur ai tapé au moins 3 caractères. Sinon, pour une lettre il pourrait y avoir beaucoup trop de résultats
  if (query.length < 3) {
    // Tant que la barre de recherche est vide, on affiche tous les enfants
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("pizzaList").style.display = "block";
  } else {
    /* On appel le controller gerant la recherche de pizzas.
    Une fois la réponse obtenue, on la récupère en données consultables, on cache la liste entière et on affiche la div montrant les résultats.
    */
    fetch(`/pizza/search?query=${encodeURIComponent(query)}`)
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("pizzaList").style.display = "none";
        let resultContainer = document.getElementById("searchResults");
        resultContainer.innerHTML = "";

        // Si il y as des résultats, on les affiches dans la div
        if (data.length > 0) {
          data.forEach((pizza) => {
            let pizzaElement = document.createElement("div");
            pizzaElement.innerHTML = `
              <div>
                <span>
                <img src="${pizza.imagePath}" alt="Image de ${pizza.name}" />
                  ${pizza.name} ${pizza.composition} ${pizza.price}€
                </span>
                <form action="/pizza/modify/${pizza.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/pizza/delete/${pizza.id}" method="post">
                  <button type="submit">Supprimer</button>
                </form>
              </div>
            `;
            resultContainer.appendChild(pizzaElement);
          });
        } else {
          // Si il n'y a aucun resultat, on affiche un message d'erreur
          resultContainer.innerHTML = "<p>Aucun résultat trouvé.</p>";
        }
      })
      // Si il y a un problème lors du fetch (l'appel au controller) on affiche l'erreur
      .catch((error) => console.error("Erreur de recherche : ", error));
  }
}
