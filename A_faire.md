# À faire

## 1. Compréhension rapide
- `CalculsDouble.java` sert de fichier « maître » où des blocs (constructeurs, opérations, utilitaires) sont insérés depuis des sous-fichiers ; `CalculsDouble_modifie.java` contient la version consolidée utilisée en pratique.
- Les constructeurs acceptent `int` ou `double` et stockent séparément la partie entière (`ent`) et la partie décimale (`dec`) sous forme de chaînes pour conserver une précision arbitraire.
- Les opérations de base (`additionneNombres`, `soustraitNombres`, `mult`, `divise`, etc.) travaillent sur ces chaînes après les avoir égalisées (`egaliseNombres`).
- Des fonctions mathématiques supplémentaires fournissent valeur absolue, arrondis, comparaison, égalité, décomposition en facteurs premiers, somme des chiffres, etc.
- Les méthodes utilitaires gèrent la représentation (toString), la copie et l’alignement du signe, ainsi que la conversion vers des formats affichables (représentation « anglaise »).
- Des scripts `.bat` (`compile.bat`, `generate_javadoc.bat`) automatisent la reconstruction de `CalculsDouble_modifie.java` et la génération de la documentation.

## 2. Bugs à corriger
- `additionneNombres` (l. 80+) n’écrit une partie décimale dans `c.dec` que si **les deux** opérandes en possèdent une. Toute addition du type `12,5 + 3` perd donc `.5`. Il faut traiter séparément le cas où une seule décimale est non vide.
- `arrondi` (vers l. 540) force `n0.estNegatif = true` lorsque `n0.dec.isEmpty()`. Une valeur déjà positive sans décimales ressort donc négative. Cette affectation doit disparaître.
- Les conversions `Integer.parseInt(a.dec)` et `Integer.parseInt(a.ent)` lèvent `NumberFormatException` dès que la chaîne dépasse 10 chiffres. Il faut basculer sur `BigInteger` ou opérér chiffre par chiffre, sinon tout nombre long fait planter l’outil.

## 3. DRY en priorité
- La classe unique mêle logique métier, affichage, I/O console (`aff/affnn`) et helpers mathématiques ; séparer en plusieurs classes (structures, opérations, IHM) améliorerait la maintenabilité et éviterait les copier-coller (ex. `compareNombres` et `compareNombres_abs` dupliquent la traversée caractère par caractère).
- Les fonctions de concaténation de chaînes et de calcul de longueur réécrivent des fonctionnalités de `StringBuilder`/`String.length()`. Utiliser les API standard éviterait de maintenir du code maison fragile.

## 4. Sécurité
- Les méthodes publiques n’encapsulent pas les chaînes internes : un appel externe peut récupérer `ent`/`dec`, les modifier et casser l’invariant. Rendre ces champs privés et exposer des accesseurs défensifs évitera les corruptions accidentelles.
- Toutes les conversions reposent sur `Integer.parseInt` sans garde et laissent remonter les exceptions vers l’IHM. Il faut capturer les erreurs utilisateur et afficher un message clair au lieu d’exposer la trace Java complète.
