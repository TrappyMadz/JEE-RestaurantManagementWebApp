
/* Au chargement de la fenêtre, on recherche le fichier "header.html" depuis la racine du projet. 
On charge la réponse sous forme de texte qu'on insère dans la balise correspondante. */
window.onload = function () {
  fetch("/header.html")
    .then((response) => response.text())
    .then((data) => {
      document.getElementById("header").innerHTML = data;
    })
    .catch((error) =>
      console.error("Erreur de chargement du header : ", error)
    );
};
