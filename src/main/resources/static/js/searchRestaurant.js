document.getElementById("searchQuery").addEventListener("input", setup);

function setup() {
	let query = document.getElementById("searchQuery").value;

	if (query.length < 3) {
		document.getElementById("searchResults").innerHTML = "";
		document.getElementById("restaurantList").style.display = "flex";
	} else {
		fetch(`/restaurant/search?query=${encodeURIComponent(query)}`)
		.then((response) => response.json())
		.then((data) => {
			document.getElementById("restaurantList").style.display = "none";
			let resultContainer = document.getElementById("searchResults");
			resultContainer.innerHTML = "";

			// Si il y as des résultats, on les affiches dans la div
			if (data.length > 0) {
				data.forEach((restaurant) => {
					let childElement = document.createElement("div");
					childElement.innerHTML = `
              <div>
                <span>
                  Restaurant ${restaurant.name}, ${restaurant.address}
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
}

