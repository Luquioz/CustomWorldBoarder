# CustomWorldBoarder

Plugin Minecraft 1.21.1 permettant de créer et gérer plusieurs worldborders personnalisés pour vos événements.

## Fonctionnalités

- Créer plusieurs worldborders avec des noms personnalisés
- Créer des bordures par sélection de coordonnées
- Activer/Désactiver les bordures individuellement
- Zone d'avertissement avant la bordure
- Téléportation automatique au centre si un joueur sort
- Particules et sons personnalisables
- **Particules visuelles** pour délimiter la bordure (cercle de particules)
- **Couleurs personnalisables** pour les particules par bordure
- **Bypass par joueur** - Système de bypass individuel pour chaque joueur et bordure
- **Réduction progressive** de la bordure sur une durée définie
- **Dégâts** aux joueurs hors de la bordure (zone configurable)
- Sauvegarde automatique des bordures

## Installation

1. Compiler le plugin avec Gradle:
```bash
./gradlew build
```

Sur Windows:
```bash
gradlew.bat build
```

2. Placer le fichier JAR généré dans le dossier `plugins` de votre serveur Paper 1.21.1

3. Redémarrer le serveur

## Commandes

Toutes les commandes commencent par `/cwb` (alias: `/customworldborder`, `/cborder`)

### Créer une bordure
```
/cwb create <nom> <taille>
```
Crée une bordure à votre position actuelle avec le nom et la taille spécifiés (en blocs).

### Créer une bordure par sélection
```
/cwb createsel <nom> <x1> <z1> <x2> <z2>
```
Crée une bordure en spécifiant deux coins (x1, z1) et (x2, z2). Le centre et la taille sont calculés automatiquement.

### Supprimer une bordure
```
/cwb delete <nom>
```
Supprime la bordure spécifiée.

### Lister les bordures
```
/cwb list
```
Affiche toutes les bordures créées avec leur statut.

### Activer une bordure
```
/cwb activate <nom>
```
Active la bordure spécifiée (les joueurs seront restreints).

### Désactiver une bordure
```
/cwb deactivate <nom>
```
Désactive la bordure spécifiée.

### Informations sur une bordure
```
/cwb info <nom>
```
Affiche les détails d'une bordure (centre, taille, etc.).

### Modifier le centre
```
/cwb setcenter <nom>
```
Définit le centre de la bordure à votre position actuelle.

### Modifier la taille
```
/cwb setsize <nom> <taille>
```
Modifie la taille de la bordure.

### Modifier la distance d'avertissement
```
/cwb setwarning <nom> <distance>
```
Modifie la distance à partir de laquelle les avertissements apparaissent avant la bordure.

### Réduire progressivement la bordure
```
/cwb reduce <nom> <taille> <ticks>
```
Réduit progressivement la taille de la bordure vers la taille cible sur la durée spécifiée (en ticks). La réduction est linéaire et progressive : la taille diminue petit à petit à chaque tick (20 ticks = 1 seconde).

### Définir les dégâts
```
/cwb setdamage <nom> <degats>
```
Définit les dégâts infligés aux joueurs (par tick) lorsqu'ils sont à l'extérieur de la bordure, dans la zone de dégâts. Mettez 0 pour désactiver.

### Définir la distance de dégâts
```
/cwb setdamagerange <nom> <distance>
```
Définit la distance (en blocs) après la limite de la bordure où les dégâts sont appliqués. Par défaut : 100 blocs.

### Définir la couleur d'avertissement
```
/cwb setwarningcolor <nom> <r,g,b>
```
Définit la couleur des particules d'avertissement pour cette bordure spécifique (format RGB).

### Définir la couleur visuelle
```
/cwb setvisualcolor <nom> <r,g,b>
```
Définit la couleur des particules visuelles (cercle) pour cette bordure spécifique (format RGB).

### Gérer le bypass
```
/cwb bypass <all|bordure> <on|off> <joueur|groupe>
```
Active ou désactive le bypass pour un joueur ou un groupe LuckPerms sur une bordure spécifique ou toutes les bordures. Le système détecte automatiquement si c'est un joueur ou un groupe.

Exemples :
- `/cwb bypass arene on Steve` - Active le bypass pour le joueur Steve sur la bordure arene
- `/cwb bypass all on Steve` - Active le bypass pour le joueur Steve sur toutes les bordures
- `/cwb bypass arene on admin` - Active le bypass pour le groupe LuckPerms "admin" sur la bordure arene
- `/cwb bypass all on admin` - Active le bypass pour le groupe LuckPerms "admin" sur toutes les bordures

### Modifier la configuration globale
```
/cwb config <option> [valeur]
```
Modifie les options de configuration globale du plugin sans éditer le fichier config.yml.

Options disponibles :
- `warning-distance <nombre>` - Distance d'avertissement par défaut
- `border-particles <true|false>` - Activer/désactiver les particules d'avertissement
- `border-sounds <true|false>` - Activer/désactiver les sons de bordure
- `border-visual-particles <true|false>` - Activer/désactiver les particules visuelles
- `warning-title <texte>` - Titre d'avertissement
- `warning-subtitle <texte>` - Sous-titre d'avertissement
- `damage-message <true|false>` - Activer/désactiver le message de dégâts
- `damage-title <texte>` - Titre de dégâts
- `damage-subtitle <texte>` - Sous-titre de dégâts
- `kick-title <texte>` - Titre de kick
- `kick-subtitle <texte>` - Sous-titre de kick
- `default-damage-range <nombre>` - Distance de dégâts par défaut

Exemples :
- `/cwb config warning-distance 10` - Change la distance d'avertissement à 10 blocs
- `/cwb config border-particles false` - Désactive les particules d'avertissement
- `/cwb config warning-title &c⚠ Attention !` - Change le titre d'avertissement

## Configuration

Le fichier `config.yml` permet de personnaliser:

- Messages d'avertissement et de kick
- Distance d'avertissement par défaut
- Activation des particules et sons d'avertissement
- **Activation des particules visuelles** (cercle délimitant la bordure)
- **Bypass pour les OPs** (activé par défaut)
- Langue des messages

## Permissions

- `customworldborder.use` - Utiliser les commandes de base
- `customworldborder.admin` - Créer, supprimer et modifier les bordures
- `customworldborder.*` - Toutes les permissions

## Exemple d'utilisation

Pour un événement:
1. Créer une bordure à l'arène: `/cwb create arene 100`
2. Activer la bordure: `/cwb activate arene`
3. Les joueurs recevront un avertissement à 95 blocs du centre
4. S'ils dépassent 100 blocs, ils seront téléportés au centre
5. Après l'événement, désactiver: `/cwb deactivate arene`

## Développement

Ce plugin est développé pour Paper 1.21.1 avec Java 21 et Gradle.

### Structure du projet
- `src/main/java/fr/luquioz/customworldboarder/`
  - `CustomWorldBoarder.java` - Classe principale
  - `commands/` - Commandes du plugin
  - `listeners/` - Écouteurs d'événements
  - `managers/` - Gestionnaires (BorderManager)
  - `models/` - Modèles de données (Border)

### Lancer le serveur de test
```bash
./gradlew runServer
```

Sur Windows:
```bash
gradlew.bat runServer
```
