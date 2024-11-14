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
