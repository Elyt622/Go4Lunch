# OpenClassrooms - Go4Lunch

## Parcours développement d’application mobile - Projet 7

L’application Go4Lunch est une application collaborative utilisée par tous les employés. Elle permet de rechercher un restaurant dans les environs, puis de sélectionner celui de son choix en en faisant part à ses collègues. De la même manière, il est possible de consulter les restaurants sélectionnés par les collègues afin de se joindre à eux. Un peu avant l’heure du déjeuner, l’application notifie les différents employés pour les inviter à rejoindre leurs collègues.

## Sommaire
1. [Objectifs](#objectifs)
2. [Installation de l’application et de la Google cloud function](#installation)
3. [Application & Fonctionnalités](#application)

## Objectifs
Créer l’application de A à Z.

- Mettre en place une base de données en ligne NoSQL Firebase
- Mettre en place les moyens d’authentification (Email/Mot de passe, Facebook, Google puis Twitter
- Mettre en place les 3 fragments de l’accueil (Map, List, Workmates);
- Localisation
- Requête retrofit pour récupérer les données des restaurants selon la localisation
- Organisation et requête firestore
- Mettre en place la Google Map
- Restructurer le code de l’architecture MVC vers MVP
- Mettre en place la Google Cloud Function pour envoyer la notification
- Tester

## Installation

### Télécharger l’application

Le lien du projet est le suivant : git@github.com:Elyt622/Go4Lunch.git
Cliquez sur le bouton "Clone or Download"
Téléchargez le projet en cliquant sur "Download ZIP"

### Cloner l'application
Cloner le projet à partir d'un terminal avec la commande: "git clone git@github.com:Elyt622/Todoc.git"

### Installation
Importer le projet dans Android Studio
Lancer le projet

### Installer la Google Cloud Function
Pour tester la notification envoyée à midi, il faut déployer la Google Cloud Function.

Configurer Node.js et la Command Line Interface Firebase

```
npm install -g firebase-tools
```

Aller dans le répertoire de la google function et exécuter la commande: 

```
firebase deploy --only functions
```

## Application
<img src="images/1.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/2.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/3.jpg" alt="Screenshot de l'application]" width="200"/>

<img src="images/4.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/5.jpg" alt="Screenshot de l'application]" width="200"/>

<img src="images/6.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/7.jpg" alt="Screenshot de l'application]" width="200"/>

<img src="images/8.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/9.jpg" alt="Screenshot de l'application]" width="200"/>

<img src="images/10.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/11.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/12.jpg" alt="Screenshot de l'application]" width="200"/>

<img src="images/13.jpg" alt="Screenshot de l'application]" width="200"/> <img src="images/14.jpg" alt="Screenshot de l'application]" width="200"/>


