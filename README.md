
![Logo](https://i.imgur.com/fr5vab6.png)


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

