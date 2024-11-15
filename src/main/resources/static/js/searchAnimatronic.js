document.getElementById("searchQuery").addEventListener("input", setup);

function setup() {
  let query = this.value;

  if (query.length < 3) {
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("AnimatronicList").style.display = "flex";
  } else {
    fetch(`/animatronic/search?query=${encodeURIComponent(query)}`)
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("AnimatronicList").style.display = "none";
        let resultContainer = document.getElementById("searchResults");
        resultContainer.innerHTML = "";

        if (data.length > 0) {
          data.forEach((animatronic) => {
            let childElement = document.createElement("div");
            childElement.innerHTML = `
              <div>
                <span>
                  ${animatronic.name} ${animatronic.type}
                </span>
                <form action="/animatronic/modify/${animatronic.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/animatronic/delete/${animatronic.id}" method="post">
                  <button type="submit">Supprimer</button>
                </form>
              </div>
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
