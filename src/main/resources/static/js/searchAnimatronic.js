document.getElementById("searchQuery").addEventListener("input", setup);
document.getElementById("searchType").addEventListener("change", setup);

function setup() {
	let query = document.getElementById("searchQuery").value;
	let type = document.getElementById("searchType").value;

	if (query.length < 3 && type === "") {
		document.getElementById("searchResults").innerHTML = "";
		document.getElementById("AnimatronicList").style.display = "flex";
	} else {
		fetch(`/animatronic/search?query=${encodeURIComponent(query)}&type=${encodeURIComponent(type)}`)
			.then((response) => response.json())
			.then((data) => {
				document.getElementById("AnimatronicList").style.display = "none";
				let resultContainer = document.getElementById("searchResults");
				resultContainer.innerHTML = "";

				if (data.length > 0) {
					data.forEach((animatronic) => {
						let childElement = document.createElement("li");
						childElement.innerHTML = `
                <span>
                <img src="${animatronic.imagePath}" alt="Image de ${animatronic.name}" />
                </span>
                <span>
                   ${animatronic.type}
                   ${animatronic.name}
                </span>
                <form action="/animatronic/modify/${animatronic.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/animatronic/delete/${animatronic.id}" method="post">
                  <button type="submit">Supprimer</button>
                </form>
            `;
						resultContainer.appendChild(childElement);
					});
				} else {
					resultContainer.innerHTML = "<p>Aucun résultat trouvé.</p>";
				}
			})
			.catch((error) => console.error("Erreur de recherche : ", error));
	}
}
