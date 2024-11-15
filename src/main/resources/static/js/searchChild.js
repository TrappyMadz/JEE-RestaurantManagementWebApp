document.getElementById("searchQuery").addEventListener("input", setup);

function setup() {
  let query = this.value;

  if (query.length < 3) {
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("childrenList").style.display = "flex";
  } else {
    fetch(`/children/search?query=${encodeURIComponent(query)}`)
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("childrenList").style.display = "none";
        let resultContainer = document.getElementById("searchResults");
        resultContainer.innerHTML = "";

        if (data.length > 0) {
          data.forEach((child) => {
            let childElement = document.createElement("div");
            childElement.innerHTML = `
              <div>
                <span>
                  ${child.name} ${child.lastName} ${child.age}ans
                </span>
                <form action="/children/modify/${child.id}" method="get">
                  <button type="submit">Modifier</button>
                </form>
                <form action="/children/delete/${child.id}" method="post">
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
