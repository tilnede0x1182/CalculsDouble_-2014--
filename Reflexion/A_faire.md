# À faire

## 1. Compréhension rapide
- `CalculsDouble.java` sert de fichier « maître » où des blocs (constructeurs, opérations, utilitaires) sont insérés depuis des sous-fichiers ; `CalculsDouble_modifie.java` contient la version consolidée utilisée en pratique.
- Les constructeurs acceptent `int` ou `double` et stockent séparément la partie entière (`partieEntiereStr`) et la partie décimale (`partieDecimaleStr`) sous forme de chaînes pour conserver une précision arbitraire.
- Les opérations de base (`additionneNombres`, `soustraitNombres`, `mult`, `divise`, etc.) travaillent sur ces chaînes après les avoir égalisées (`egaliseNombres`).
- Des fonctions mathématiques supplémentaires fournissent valeur absolue, arrondis, comparaison, égalité, décomposition en facteurs premiers, somme des chiffres, etc.
- Les méthodes utilitaires gèrent la représentation (toString), la copie et l’alignement du signe, ainsi que la conversion vers des formats affichables (représentation « anglaise »).
- Des scripts `.bat` (`compile.bat`, `generate_javadoc.bat`) automatisent la reconstruction de `CalculsDouble_modifie.java` et la génération de la documentation.

## 2. Bugs corrigés (2026-02-25)

### Bug division par zéro
- Problème : division_boucle entrait en boucle infinie au lieu de retourner null
- Cause : verifierCasParticuliers retournait null, mais la condition if (casParticulier != null) laissait passer ce null vers effectuerDivision
- Correction : Vérification estNul(diviseur) déplacée directement dans division_boucle avec return null immédiat

### Bug grands nombres (Integer.parseInt)
- Problème : Integer.parseInt plantait sur les nombres > 10 chiffres
- Correction :
  - Ajout de fonctions d'arithmétique sur chaînes dans Fonctions_utilitaires.java : additionneChaines, soustraitChaines, comparerChaines
  - Remplacement de Integer.parseInt par ces fonctions dans Addition.java et Soustraction.java
  - Remplacement de (int)(nombre) par (long)(nombre) dans les constructeurs

### Bug division off-by-one (5.1 au lieu de 5.0)
- Problème : mult(0, 10) produisait "00", et compareNombres("00", "4") comparait les longueurs (2 > 1) retournant incorrectement 0 > 4
- Correction : Ajout de supprimerZerosGauche() dans comparerPartiesEntieres pour normaliser avant comparaison

### Bug addition décimal + entier
- Ce bug était déjà corrigé ou n'existait pas dans cette version (tests passent)

## 3. État actuel
- 23/23 tests passent
- Division par zéro : retourne null et affiche un message

## 4. DRY en priorité
- La classe unique mêle logique métier, affichage, I/O console (`aff/affnn`) et helpers mathématiques ; séparer en plusieurs classes (structures, opérations, IHM) améliorerait la maintenabilité et éviterait les copier-coller (ex. `compareNombres` et `compareNombres_abs` dupliquent la traversée caractère par caractère).
- Les fonctions de concaténation de chaînes et de calcul de longueur réécrivent des fonctionnalités de `StringBuilder`/`String.length()`. Utiliser les API standard éviterait de maintenir du code maison fragile.

## 5. Sécurité
- Les méthodes publiques n'encapsulent pas les chaînes internes : un appel externe peut récupérer `partieEntiereStr`/`partieDecimaleStr`, les modifier et casser l'invariant. Rendre ces champs privés et exposer des accesseurs défensifs évitera les corruptions accidentelles.
- Toutes les conversions reposent sur `Integer.parseInt` sans garde et laissent remonter les exceptions vers l’IHM. Il faut capturer les erreurs utilisateur et afficher un message clair au lieu d’exposer la trace Java complète.
