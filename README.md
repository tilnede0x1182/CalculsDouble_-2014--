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
