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
	fetch(`/restaurant/search?query=${encodeURIComponent(query)}&animatronic=${encodeURIComponent(animatronic)}`)
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
						.map((animatronic) => animatronic.type + ' ' + animatronic.name) // Extraire uniquement les noms
						.join(", "); // Les séparer par des virgules
					let childElement = document.createElement("div");
					childElement.innerHTML = `
              <div>
                <span>
                  Restaurant ${restaurant.name}, ${restaurant.address}, animatronics=[${animatronicsList}]
                </span>
                <form action="/restaurant/modify/${restaurant.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/restaurant/delete/${restaurant.id}" method="post">
                  <button type="submit">Supprimer</button>
                </form>
              </div>
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
