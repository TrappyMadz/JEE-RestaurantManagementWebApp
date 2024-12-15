document.getElementById("searchQuery").addEventListener("input", setup);
document.getElementById("searchAnimatronic").addEventListener("change", setup);

function setup() {
  let query = document.getElementById("searchQuery").value;
  let animatronic = document.getElementById("searchAnimatronic").value;

  if (query.trim().length < 3 && animatronic === "") {
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("restaurantList").style.display = "flex";
  } else if (animatronic != "") {
    searchAndShow("", animatronic);
  } else {
    searchAndShow(query, animatronic);
  }
}

function searchAndShow(query, animatronic) {
  fetch(
    `/restaurant/search?query=${encodeURIComponent(
      query
    )}&animatronic=${encodeURIComponent(animatronic)}`
  )
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      document.getElementById("restaurantList").style.display = "none";
      let resultContainer = document.getElementById("searchResults");
      resultContainer.innerHTML = "";

      // Si il y as des résultats, on les affiches dans la div
      if (data.length > 0) {
        data.forEach((restaurant) => {
          let animatronicsList = restaurant.animatronics
            .map((animatronic) => animatronic.type + " " + animatronic.name) // Extraire uniquement les noms
            .join(", "); // Les séparer par des virgules
          let childElement = document.createElement("li");
          childElement.innerHTML = `
                <span>
                  ${restaurant.name}
                </span>
				<span>
                  ${restaurant.address}
                </span>
				<div style="display: none" id="toDevoil-${restaurant.id}" class="toDevoil">
                  <button class="closeDetail">Fermer</button>
                  <h2>Animatroniques :</h2>
                  <ul id="AnimatronicList" class="list">
                    ${restaurant.animatronics
                      .map(
                        (animatronic) => `
                          <li>
                            <span>
                              <img src="/animatronic/${animatronic.imagePath}" alt="Image de ${animatronic.name}" />
                            </span>
                            <span>${animatronic.type} ${animatronic.name}</span>
                          </li>`
                      )
                      .join("")}
                  </ul>
                </div>
                <button class="viewAnimatronics" data-id="${
                  restaurant.id
                }">Voir les animatroniques</button>
                <form action="/restaurant/modify/${restaurant.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/restaurant/delete/${
                  restaurant.id
                }" method="post">
                  <button type="submit">Supprimer</button>
                </form>
            `;
          resultContainer.appendChild(childElement);
        });
      } else {
        // Si il n'y a aucun resultat, on affiche un message d'erreur
        resultContainer.innerHTML = "<p>Aucun résultat trouvé.</p>";
      }
    })
    // Si il y a un problème lors du fetch (l'appel au controller) on affiche l'erreur
    .catch((error) => console.error("Erreur de recherche : ", error));
}

document
  .getElementById("searchResults")
  .addEventListener("click", function (event) {
    // Vérifie si l'élément cliqué est le bouton "Voir les animatroniques"
    if (event.target && event.target.classList.contains("viewAnimatronics")) {
      let restaurantId = event.target.getAttribute("data-id");
      let toDevoilDiv = document.getElementById("toDevoil-" + restaurantId);
      toDevoilDiv.style.display = "flex"; // Affiche les animatroniques
    }

    // Vérifie si l'élément cliqué est le bouton "Fermer"
    if (event.target && event.target.classList.contains("closeDetail")) {
      let toDevoilDiv = event.target.closest(".toDevoil");
      toDevoilDiv.style.display = "none"; // Masque les animatroniques
    }
  });
