-Demo Publique

# GCMRM

Gestionnaire de campagne de compatge de cellules pour la recherche médicale 🦠

## Résumé

### Fonctionnalités

- M1 :
    Mode comptage rapide 🔎

- M2 :
    Gestion de campagnes de comptage avec plusieurs images et algorithmes de comptages 🗃

- S1 :
    Correction manuelle des mesures des images ✏

- S2 :
    Liste des images et campagnes 📃

- S3 :
    Utilisation de deep learning pour le comptages des cellules 🧠

### Contraintes

- environement desktop (windows ou autre) hors ligne 💻

- ajout d'algorithmes via des plugin pour évolution 🔌

- evolutivité : possibilité d'utilisé pour compter autre chose que des cellules 🐌

- possibilité d'utilisation par des biologistes, pas des informaticiens 👩‍🔬

## Configuration

### Prérequis

<strong>Java JDK version 14 minimum</strong> est nécéssaire : https://www.oracle.com/java/technologies/javase-downloads.html.

> ⚠ Il faut que le dossier bin du JDK java se trouve dans la variable d'environement PATH sur windows, sinon, gradle pourrais ne pas fonctionner. De plus, il faut changer la variable JAVA_HOME dans le dossier du JDK.

Un IDE est recommandé mais pas nécéssaire, il est possible d'utiliser eclipse, visual studio code (vscodium), ou autre.

### Commandes

Ce projet utilise l'outils <a href="https://gradle.org">gradle</a> pour la compiliation et la gestion de dépendances, les commandes pour l'utiliser sont :

- `./gradlew tasks` donne la liste des commandes disponnibles.
- `./gradlew build` lance la compilation.
- `./gradlew run` lance la compilation puis lance l'application.
- `./gradlew clean` supprime les fichiers intermediaires.
- `./gradlew jar` crée un jar executable.
- `./gradlew javadoc` crée la javadoc.
- `./gradlew test` lance la compilation puis lance les tests JUnit.
- `./gradlew eclipse` créer un environment eclipse. (il se peut qu'eclipse demande un classe pour le "run" dans ce cas, choisir "com.gcmrm.Launcher)

### Dépendances

Les bibliothèques nécéssaires pour le logiciel seront automatiquement téléchargées par gradle, donc pas besoins de réglages particuliés.

- <a href="https://openjfx.io">JavaFX</a> (via dépot maven), avec les plugins controls (= widget), graphics, media, swing, fxml et web. le tout en <strong>version 15</strong>.
- <a href="https://hibernate.org/orm/">Hibernate</a> (via dépot maven)
- <a href="https://github.com/xerial/sqlite-jdbc">SQLite JDBC</a> (via dépot maven)
- <a href="https://github.com/gwenn/sqlite-dialect/">SQLite Hibernate Dialect</a> (via dépot maven)
- <a href="https://github.com/google/gson">GSON</a> (via dépot maven), gestion de fichier json.
- <a href="http://jopendocument.org">JOpenDocument</a> (via fichier jar), gestion de fichier office.
- <a href="https://imagej.net/Welcome">ImageJ</a> (via dépot maven), lib scientifique de traitment d'image.
- <a href="https://deeplearning4j.org">DL4J</a> (via dépot maven), deep learning.


Aide: Contacter Mahmod Alhabaj mhabaj99@hotmail.com

Contributions:
ALHABAJ Mahmod, BOUTONNET Clément, CHABIN Jean-Malo, CHANTRE Honorine,
DROUET Martin, PONS Maël, SOUVILLE-CHASSING Samuel, UNG Alexandre