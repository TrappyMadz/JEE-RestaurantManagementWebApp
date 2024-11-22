/* Au chargement de la fenêtre, on recherche le fichier "header.html" depuis la racine du projet. 
On charge la réponse sous forme de texte qu'on insère dans la balise correspondante. */
window.onload = function () {
  fetch("/header.html")
    .then((response) => response.text())
    .then((data) => {
      document.getElementById("header").innerHTML = data;

      // Lancement des animations après chargement du header
      const blinkingElements = document.querySelectorAll(".blink");

      // Initialise un délai de base
      let baseDelay = 0;

      blinkingElements.forEach((element, index) => {
        // Génère une durée aléatoire entre 1 et 3 secondes
        const duration = (Math.random() * 20 + 10).toFixed(2);
        // Ajoute un décalage progressif en fonction de l'index
        const delay = (baseDelay + index * 0.2).toFixed(2);

        // Applique les styles en ligne pour personnaliser chaque élément
        element.style.animationDuration = `${duration}s`;
        element.style.animationDelay = `${delay}s`;

        // Augmente légèrement le décalage de base aléatoirement
        baseDelay += Math.random() * 0.6;
      });
    })
    .catch((error) =>
      console.error("Erreur de chargement du header : ", error)
    );

  fetch("/footer.html")
    .then((response) => response.text())
    .then((data) => {
      document.getElementById("footer").innerHTML = data;
    })
    .catch((error) =>
      console.error("Erreur de chargement du footer : ", error)
    );
};
