# CalculsDouble

Calculatrice Java à précision arbitraire. Les nombres sont stockés sous forme de chaînes de caractères, permettant des calculs sans perte de précision.

## Prérequis

- Java (JDK 8+)
- Python 3
- Make

## Compilation

```bash
make compile
```

Cette commande :
1. Lance le préprocesseur Python sur `src/CalculsDouble.java`
2. Génère `src/CalculsDouble_modifie.java` (fichier consolidé)
3. Compile vers `build/`

## Exécution

```bash
make run
```

## Génération Javadoc

```bash
make javadoc
```

La documentation est générée dans `javadoc/`.

**Note Windows** : Le dossier `javadoc/` doit être créé manuellement depuis l'explorateur ou PowerShell avant la première exécution.

## Structure

```
.
├── src/
│   ├── CalculsDouble.java          # Fichier maître (imports personnalisés)
│   ├── CalculsDouble_modifie.java  # Fichier généré (compilable)
│   ├── Fonctions_utilitaires/      # Constructeurs, utilitaires
│   ├── Operations/                 # +, -, *, /
│   └── Utilitaire/                 # Préprocesseur Python
├── build/                          # Classes compilées
├── javadoc/                        # Documentation générée
└── tests/                          # Tests JavaScript
```

## Fonctionnalités

- Opérations de base : addition, soustraction, multiplication, division
- Comparaison, valeur absolue, arrondis
- Décomposition en facteurs premiers
- Support représentation française (virgule) et anglaise (point)
- Arithmétique sur grands nombres (pas de limite Integer.MAX_VALUE)

## Tests

```bash
make run
```

23 tests unitaires couvrant :
- Constructeurs (positif, négatif, décimal, zéro)
- Addition, soustraction, multiplication, division
- Valeur absolue
- Division par zéro (retourne null avec message)
- Grands nombres (> 10 chiffres)

## À noter

**Reconnaissance_expression/** : Contient 2 fichiers mais seul `Reconnaissance_doubles_sans_parentheses.java` est utile. La version `_entiers_` est redondante car la version doubles gère aussi les entiers (un entier est un cas particulier de décimal).

**Archives/** : Anciennes versions avant refactor (2026-02-25)
- `CalculsDouble_modifie.java` : ancienne version du fichier consolidé
- `CalculsDouble_modifie.java.backup` : sauvegarde de sécurité
- `compile.bat` : ancien script batch (remplacé par Makefile)
- `generate_javadoc.bat` : ancien script batch (remplacé par Makefile)
- `python3m1.py` : ancienne version du préprocesseur (code non refactorisé)
- `READ-ME.txt` : instructions de compilation originales
