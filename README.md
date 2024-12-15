
![Logo](https://i.imgur.com/fr5vab6.png)



# Freddy's Organizer

Ce projet JEE est un devoir et représente un site de gestion d'une chaîne de restaurants dans le thème de la licence Five Nigths At Freddy's.

En arrivant sur le site, il y a trois boutons : "+ Nouvel Anniversaire", "Liste des Anniversaires" et "Tâches journalières". Les deux premiers boutons sont plutôt explicites et nous reviendrons plus tard pour cette page.

La barre de navigation contient cinq liens :
- le premier "Accueil" envoie sur la page principale.
- "Adhérents" envoie sur la page gérant les personnes inscrites. Il y a une barre de recherche pour rechercher selon le nom ou le prénom, un bouton pour ajouter un adhérent, et en dessous s'affichent les adhérents sous forme de nom, prénom et âge. Pour chaque adhérent, il y a un bouton modifier et un bouton supprimer. Ajouter (resp. modifier) envoient sur une page pour ajouter (resp. modifier) un adhérent. Il faut y inscrire un nom, un prénom et un âge (pour la modification, les attribut de l'adhérent non modifié y sont écris à l'avance).
- "Pizzas" envoie sur la page gérant les pizzas. De même, on peut rechercher des pizza selon le nom ou la composition et ajouter une pizza. Il y a en dessous les pizza déjà présentes, affichées avec leur image, leur nom, leur composition, leur prix puis un bouton modifier et un bouton supprimer. Le bouton pour ajouter et celui pour modifier amènent chacun sur une page pour remplir les informations pour la pizza (image, nom, composition et prix), mais pour la page de modification les informations sont préremplies, sauf l'image.
- "Restaurant" envoie sur la page gérant les restaurants. On peut rechercher selon le nom du restaurant, son adresse ou un animatronique. De même, il y a un bouton pour jouter un animatronique, les restaurants sont affichés en dessous avec leur nom, adresse, un bouton envoyant sur une page montrant les animatroniques présent dans le restaurant, un bouton pour modifier et un autre pour supprimer le resaturant. Les boutons d'ajout et de modification envoient sur une page permettant de remplir les informations du non et de l'adresse ainsi qu'une checkbox permettant de choisir les animatroniques à y placer (seuls les animatroniques qui ne sont pas déjà dans un restaurant sont présent et décocher lors de la modification permet d'enlever l'animatronique du restaurant).
- "Animatroniques" envoie sur la page gérant les animatroniques. Il est possible de rechercher des animatroniques selon leur nom et/ou leur type, il y a ensuite de même un bouton pour ajouter un animatronique. En dessous, sont présents les animatroniques déjà créés avec leur image, leur type et leur nom, puis un bouton de modification et un bouton de suppression. Les boutons d'ajout et de modification envoient sur une page permettant de remplir les informations image, nom et type (le type de l'animatronique se trouve parmi une sélection prédéfini par nous, ce sont des types existant dont nous avons repris les noms).

Revenons à la page d'accueil:
- Le bouton "+ Nouvel Anniversaire" permet la création d'un anniversaire à travers un formulaire en trois étapes :
    - Dans la première étape on peut choisir la date de l'anniversaire (il ne peut pas être le jour de la création ni avant), le restaurant pour l'anniversaire et l'enfant dont ce sera l'anniversaire (parmi les enfants qui n'ont pas déjà eu d'anniversaire).
    - Dans la deuxième étape, on choisit les animatroniques présents lors de l'anniversaire parmi les animatronique du resaturant choisi à l'étape 1, puis les enfants participants à l'anniversaire. Les animatroniques ont une limite d'un anniversaire par jour et on ne peut pas ne mettre aucun enfant participant.
    - Dans l'étape trois on peut choisir les pizzas pour l'anniversaire et le nombre de chaque pizza choisit. Il n'est ni possible de ne pas choisir de pizza ni ne pas mettre un nombre de pizza lorsqu'elle est cochée.
- Le bouton "Liste des Anniversaires" envoie sur une page contenant les anniversaires déjà créés avec la possibilité de modifier (cela reprend les mêmes étapes que la création) ou de supprimer. il y a aussi un bouton pour afficher les anniversaires passés car les anniversaires passés sont cachés.
- Le bouton "Tâches journalières" envoie sur une page montrant les animatroniques à préparer ainsi que les pizzas à préparer pour la journée selon le restaurant choisit.
# Lancer le projet en local

[Télécharger le zip du projet](https://github.com/TrappyMadz/JEE-RestaurantManagementWebApp)

Importer ce zip en tant que projet dans eclipse JEE

Ouvrir le fichier suivant :
```
src/main/java/fr.cytech.restaurant_management/RestaurantManagementApplication.java
```
Éxecuter le projet

Lancer un navigateur et aller à l'adresse suivante :
```
http://localhost:8080/
```

Vous êtes prêt à profiter du projet !
# Auteurs :

- [Marco Vanni](https://github.com/MVchara)
- [Simon Charrier](https://github.com/TrappyMadz)




# Auto-évaluation :

## Fonctionnalités :
### L'application contient les Fonctionnalités demandées :
Notre application est bel et bien développée en utilisant SpringBoot et Thymeleaf. Nous avons un principe de MVC avec, d'un coté les controller qui se charge de traiter les données, de repository qui font le lien avec les recherches en base de donnée, et de vues html qui utilisent les données envoyées par les controller.

Il y a 6 entité :
- Animatronic, un robot humanoïde comme on peut en voir dans des parcs à thème
- Restaurant, les restaurant faisant partie de la chaîne
- Birthday, les anniversaires organnisés par ces restaurants
- Child, la liste des enfants ayant eu des interactions avec les restaurants
- Pizza, la liste des pizzas disponibles
- PizzaOrder, les commandes par type de pizzas

Pour ce qui est des relations : 
- Restaurant à une relation 1-N avec Animatronic et Birthday
- Pizza à une relation 1-N avec PizzaOrder
- Child à une relation 1-1 et N-N avec Birthday. Il peut fêter son anniversaire ou être invité à un anniversaire
- Animatronic à deux relations 1-N avec Birthday, car chaque Birthday à 2 Animatronic

Pour ce qui est de dissocier et associer graphiquement, via les formulaires. Le plus souvent, les relations ne se gèrent que d'un seul coté, et pour certaines associations il est necessaire de supprimer une des entités. Par exemple, on ne peut enlever un annimatronique que depuis un restaurant.

Notre application sert surtout de récapitulatif de la journée. En effet, en choisissant un restaurant, on peut voir les tâches à faire pour la journée. L'application additionne automatiquement le nombre de pizzas qu'il est necessaire de préparer et montre tous les animatroniques à sortir du placard. Ceci peut être un grand gain de temps, et c'est ce qui fait que notre application à une logique métier.

Enfin, nous avons utilisés github tout au long du projet.

### L'application permet d'insérer, mettre à jour, supprimer, chercher une entité en BDD :
En effet, chaque entité peut et doit être créée en BDD pour fonctionner. Ce process se fait via des formulaires, qui demandent parfois à ce que d'autres entitées existent déjà. Par exemple, impossible de créer un anniversaire sans enfants.
Toutes les entités peuvent êtres supprimées, mais certaines demandent à ce qu'elles soient dissociées manuellement des autres avant. Au moindre soucis, un petit message apparaît expliquant la marche à suivre, alors il est impossible de se perdre. Il est également possible de mettre à jour chaque entité, et des barres de recherches et différents filtres sont disponibles pour faciliter la recherche d'entitées.

### L'application permet de lier deux entitées en BDD :
Comme dit précédemment, les entitées sont fortement liées entre elles. Un anniversaire est organnisés par un restaurant, avec des pizzas commandées, des enfants y participant et des animatroniques pour animer le tout. Pour lier deux entités, il faudra la plupart du temps modifier une entité précise ou le faire au moment de la création. Par exemple, à la création d'un restaurant, il est possible d'y ajouter autant d'animatronique que voulu. Il est ensuite possible de modifier ce même restaurant pour retirer ou ajouter les animatroniques voulus.

### L'application permet, pour une entité donnée, de créer un lien à une entité en BDD :
Pour reprendre l'exemple du paragraphe précédent, on peut prendre un restaurant existant pour le lier à un animatronique. Il est aussi possible de modifier un anniversaire pour y changer les commandes de pizzas par exemple.

## Technique :

### L'application utilise le design pattern MVC pour chaque Fonctionnalités :
En effet, chacune des Fonctionnalités de l'application passe par un controller qui gère les données puis qui les envoies à une vue html qui se contente de les afficher.

### Les controlleurs utilisent les méthodes HTTP: GET, POST, PUT, DELETE :
Nous n'utilisons que GET et POST tout au long de notre projet.

### Chaque vue manipule des données transmises par son controlleur :
Seule une vue ne répond pas à cette consigne : la page d'accueil.

## Qualité :
### L'applicationest jolie / utilise un framework CSS :
Nous avons tenus à avoir une direction artistique très unique, s'inspirant des dinner américains des années 80. Elle est en particulier inspiré du design du restaurant du film "Five Night At Freddy's", si il n'était pas tombé en ruines.

### Le code source est livré dans un repo Github/Gitlab. Il est de bonne qualité :
Le code source est intégralement disponible sur notre Github. Nous avons fait de notre mieux pour créer un code cohérent, lisible et le plus optimisé possible, mais avec notre manque d'expérience dans ce langage de petits couacs sont inévitables. Globalement, nous sommes plutôt fière de ce que nous avons produit. Le code est commenté et les noms de variables sont bien choisis.

### Le repo comporte des commits réguliers de chaque membre du groupe :
Mise à part une petite pose pendant les partiels, nous avont constamment mis à jour le repo. Nous nous étions répartis les tâches au préalable, ce qui fait que nous avons à peut prêt tous les deux travaillé un même montant horaire.

## Soutenance :
Nous n'avons pas pu passé la soutenance pour des raisons qui ne dépendent pas de nous, mais je suis sûr que nous aurions pu avoir une discution intéressante et répondre à toutes les questions posées.

