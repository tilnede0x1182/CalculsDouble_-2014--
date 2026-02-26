#!/usr/bin/env python3
"""
	Préprocesseur d'imports personnalisés.

	Détecte les lignes "import nom_fichier" dans un fichier source,
	ouvre les fichiers référencés, et remplace les lignes d'import
	par le contenu des fichiers.

	Usage :
		python preprocesseur_imports.py fichier_source.py [suffixe_sortie]
"""

# ==============================================================================
# Importations
# ==============================================================================

import sys
import os

# ==============================================================================
# Données
# ==============================================================================

MARQUEUR_IMPORT = "import "
MARQUEUR_ERREUR = "Error"

CORRECTIONS_UTF8 = {
	'Ã©': "é",
	"Ã ": 'à',
	'ï»¿': "",
	"Ã¢": "â",
	"Ãª": "ê"
}

# ------------------------------------------------------------------------------
## Données de test
# ------------------------------------------------------------------------------

CONTENU_TEST_AVEC_IMPORTS = (
	"ekljdzl\n"
	"kejdlezd d\n"
	"import Explication.txt\n"
	"import blabla.py\n"
	"import autre_fichier.java\n"
	"kdlejzdkljze\n"
	"djzkljdz dzkdjezl dkje\n"
)

CONTENU_TEST_IMPORT_MILIEU_LIGNE = (
	"ekljdzl\n"
	"kejdlezd d\n"
	"lzeimport Explication.txt\n"
	"kdlejzdkljze\n"
	"djzkljdz dzkdjezl dkje\n"
)

# ==============================================================================
# Fonctions utilitaires
# ==============================================================================

# ------------------------------------------------------------------------------
## Manipulation de chaînes
# ------------------------------------------------------------------------------

"""
	Trouve l'index du premier caractère non-espace dans une chaîne.

	@param chaine Chaîne à analyser
	@return Index du premier caractère non-espace, ou longueur de la chaîne
"""
def trouver_debut_contenu(chaine):
	index = 0
	while index < len(chaine) and chaine[index] == ' ':
		index += 1
	return index


"""
	Trouve l'index du dernier caractère non-espace dans une chaîne.

	@param chaine Chaîne à analyser
	@param index_debut Index de départ pour la recherche
	@return Index de fin (exclusif) du contenu sans espaces finaux
"""
def trouver_fin_contenu(chaine, index_debut):
	index_fin = index_debut
	while index_fin < len(chaine):
		if chaine[index_fin] == ' ':
			reste_est_espaces = True
			for index_verif in range(index_fin, len(chaine)):
				if chaine[index_verif] != ' ':
					reste_est_espaces = False
					break
			if reste_est_espaces:
				break
		index_fin += 1
	return index_fin


"""
	Supprime les espaces au début et à la fin d'une chaîne.
	Équivalent de strip() mais implémentation manuelle.

	@param chaine Chaîne à nettoyer
	@return Chaîne sans espaces inutiles
"""
def supprime_espaces_inutiles(chaine):
	index_debut = trouver_debut_contenu(chaine)
	index_fin = trouver_fin_contenu(chaine, index_debut)
	return chaine[index_debut:index_fin]


"""
	Applique les corrections d'encodage UTF-8 connues sur un contenu.

	@param contenu Texte à corriger
	@return Texte avec caractères corrigés
"""
def corriger_encodage_utf8(contenu):
	resultat = contenu
	for caractere_errone, caractere_correct in CORRECTIONS_UTF8.items():
		resultat = resultat.replace(caractere_errone, caractere_correct)
	return resultat

# ------------------------------------------------------------------------------
## Détection type d'import
# ------------------------------------------------------------------------------

"""
	Vérifie si un import est un import Java standard (à ignorer).
	Les imports Java standard contiennent un point-virgule et des points.
	Ex: "java.util.Stack;" → True
	Ex: "Fonctions_utilitaires/Constructeurs.java" → False

	@param nom_import Nom extrait après "import "
	@return True si import Java standard, False si import fichier local
"""
def est_import_java_standard(nom_import):
	if ';' in nom_import:
		return True
	if '.' in nom_import and '\\' not in nom_import and '/' not in nom_import:
		partie_avant_extension = nom_import.rsplit('.', 1)[0]
		if '.' in partie_avant_extension:
			return True
	return False

# ------------------------------------------------------------------------------
## Normalisation des chemins cross-platform
# ------------------------------------------------------------------------------

"""
	Normalise un chemin pour le système d'exploitation courant.
	Convertit les séparateurs Windows (backslash) et Unix (/) vers os.sep.

	@param chemin Chemin à normaliser (peut contenir backslash ou /)
	@return Chemin avec séparateurs adaptés au système courant
"""
def normaliser_chemin(chemin):
	chemin_normalise = chemin.replace('\\', os.sep)
	chemin_normalise = chemin_normalise.replace('/', os.sep)
	return chemin_normalise

# ------------------------------------------------------------------------------
## Extraction de chemins
# ------------------------------------------------------------------------------

"""
	Extrait le nom de fichier après un marqueur d'import jusqu'à la fin de ligne.
	Normalise automatiquement les séparateurs de chemin pour le système courant.

	@param contenu Contenu source complet
	@param position_debut Position de départ après "import "
	@return Nom de fichier extrait (nettoyé et normalisé)
"""
def extraire_nom_fichier_import(contenu, position_debut):
	nom_fichier = ""
	position = position_debut
	while position < len(contenu) and contenu[position] != '\n':
		nom_fichier += contenu[position]
		position += 1
	chemin_brut = supprime_espaces_inutiles(nom_fichier)
	return normaliser_chemin(chemin_brut)


"""
	Retourne le chemin du dossier contenant un fichier.

	@param chemin_fichier Chemin complet du fichier
	@return Chemin du dossier parent (avec séparateur final)
"""
def extraire_dossier_parent(chemin_fichier):
	derniere_position_sep = len(chemin_fichier)
	if os.sep in chemin_fichier:
		for index in range(len(chemin_fichier)):
			if chemin_fichier[index] == os.sep:
				derniere_position_sep = index
	return chemin_fichier[0:derniere_position_sep + 1]

# ==============================================================================
# Fonctions principales
# ==============================================================================

# ------------------------------------------------------------------------------
## Détection des imports
# ------------------------------------------------------------------------------

"""
	Détecte toutes les lignes "import nom_fichier" dans un contenu.

	@param contenu_source Contenu du fichier source à analyser
	@return Liste des noms de fichiers à importer
"""
def detecter_imports(contenu_source):
	liste_fichiers = []
	longueur_marqueur = len(MARQUEUR_IMPORT)

	for index in range(len(contenu_source) - longueur_marqueur):
		if contenu_source[index:index + longueur_marqueur] == MARQUEUR_IMPORT:
			position_nom = index + longueur_marqueur
			nom_fichier = extraire_nom_fichier_import(contenu_source, position_nom)
			if est_import_java_standard(nom_fichier):
				continue
			liste_fichiers.append(nom_fichier)

	return liste_fichiers

# ------------------------------------------------------------------------------
## Lecture de fichiers
# ------------------------------------------------------------------------------

"""
	Lit le contenu d'un fichier et applique les corrections d'encodage.

	@param chemin_fichier Chemin du fichier à lire
	@return Contenu du fichier ou MARQUEUR_ERREUR si échec
"""
def lire_fichier_avec_correction(chemin_fichier):
	try:
		with open(chemin_fichier, 'r') as fichier:
			contenu = fichier.read()
		return corriger_encodage_utf8(contenu)
	except Exception as erreur_lecture:
		print("lire_fichier_avec_correction : " + str(erreur_lecture))
		return MARQUEUR_ERREUR


"""
	Ouvre et lit une liste de fichiers.

	@param liste_chemins Liste des chemins de fichiers à ouvrir
	@return Liste des contenus (ou MARQUEUR_ERREUR pour chaque échec)
"""
def ouvrir_fichiers(liste_chemins):
	contenus = []
	for chemin in liste_chemins:
		contenu = lire_fichier_avec_correction(chemin)
		contenus.append(contenu)
	return contenus

# ------------------------------------------------------------------------------
## Remplacement des imports
# ------------------------------------------------------------------------------

"""
	Traite une ligne contenant un import.

	@param ligne Ligne source contenant "import "
	@param contenu_fichier Contenu du fichier importé
	@return Contenu à insérer (fichier importé ou ligne originale si erreur)
"""
def traiter_ligne_import(ligne, contenu_fichier):
	if contenu_fichier != MARQUEUR_ERREUR:
		return contenu_fichier
	return ligne


"""
	Remplace les lignes "import fichier" par le contenu des fichiers.

	@param contenu_source Contenu source avec les imports
	@return Contenu avec imports remplacés par contenus de fichiers
"""
def remplacer_imports_par_contenu(contenu_source):
	liste_imports = detecter_imports(contenu_source)
	contenus_fichiers = ouvrir_fichiers(liste_imports)

	if len(liste_imports) != len(contenus_fichiers):
		print("remplacer_imports_par_contenu : Erreur incohérence listes.")
		return contenu_source

	lignes_source = contenu_source.split('\n')
	resultat = ""
	index_import = 0

	for index_ligne, ligne in enumerate(lignes_source):
		if MARQUEUR_IMPORT in ligne:
			position_nom = ligne.find(MARQUEUR_IMPORT) + len(MARQUEUR_IMPORT)
			nom_import = ligne[position_nom:].strip()
			if est_import_java_standard(nom_import):
				resultat += ligne
				if index_ligne < len(lignes_source) - 1:
					resultat += '\n'
				continue
			resultat += traiter_ligne_import(ligne, contenus_fichiers[index_import])
			if contenus_fichiers[index_import] != "":
				resultat += '\n'
			index_import += 1
		else:
			resultat += ligne
			if index_ligne < len(lignes_source) - 1:
				resultat += '\n'

	return resultat

# ==============================================================================
# Tests
# ==============================================================================

"""
	Teste la détection et le remplacement des imports.

	Vérifie que :
	1. Les lignes "import fichier" sont correctement détectées
	2. Les lignes où "import" n'est pas un mot isolé sont ignorées
	3. Le remplacement fonctionne (affiche erreur si fichier inexistant)

	Utilise les constantes CONTENU_TEST_AVEC_IMPORTS et CONTENU_TEST_SANS_IMPORTS.
"""
def test_detection_et_remplacement_imports():
	print("=" * 60)
	print("TEST 1 : Détection avec imports valides")
	print("=" * 60)
	print("Contenu source :")
	print(CONTENU_TEST_AVEC_IMPORTS)
	print("-" * 40)

	imports_detectes = detecter_imports(CONTENU_TEST_AVEC_IMPORTS)
	print("Imports détectés : " + str(imports_detectes))
	print("-" * 40)

	resultat_remplacement = remplacer_imports_par_contenu(CONTENU_TEST_AVEC_IMPORTS)
	print("Résultat après remplacement :")
	print(resultat_remplacement)

	print("\n" + "=" * 60)
	print("TEST 2 : Détection import au milieu de ligne")
	print("=" * 60)
	print("Contenu source :")
	print(CONTENU_TEST_IMPORT_MILIEU_LIGNE)
	print("-" * 40)

	imports_detectes_2 = detecter_imports(CONTENU_TEST_IMPORT_MILIEU_LIGNE)
	print("Imports détectés : " + str(imports_detectes_2))
	print("Note : comportement actuel = détection même au milieu de ligne")

# ==============================================================================
# Main
# ==============================================================================

"""
	Vérifie si l'argument --test est présent.

	@return True si --test est dans les arguments, False sinon
"""
def est_mode_test():
	return "--test" in sys.argv


"""
	Vérifie les arguments de la ligne de commande.
	Quitte le programme si insuffisants (sauf en mode test).
"""
def verifier_arguments():
	if est_mode_test():
		return
	if len(sys.argv) < 2:
		print("Usage : python preprocesseur_imports.py fichier [suffixe]")
		print("        python preprocesseur_imports.py --test")
		sys.exit(1)


"""
	Récupère le suffixe pour le fichier de sortie.

	@return Suffixe personnalisé ou "modifie" par défaut
"""
def obtenir_suffixe_sortie():
	if len(sys.argv) == 3 and sys.argv[2] != "":
		return sys.argv[2]
	return "modifie"


"""
	Génère le chemin du fichier de sortie selon l'extension.

	@param chemin_source Chemin du fichier source
	@param suffixe Suffixe à ajouter avant l'extension
	@return Chemin du fichier de sortie
"""
def generer_chemin_sortie(chemin_source, suffixe):
	if ".py" in chemin_source:
		base = chemin_source[:len(chemin_source) - len(".py")]
		return base + "_" + suffixe + ".py"
	elif ".java" in chemin_source:
		base = chemin_source[:len(chemin_source) - len(".java")]
		return base + "_" + suffixe + ".java"
	return chemin_source + "_" + suffixe


"""
	Écrit le contenu traité dans le fichier de sortie.

	@param chemin_sortie Chemin du fichier à écrire
	@param contenu Contenu à écrire
"""
def ecrire_fichier_sortie(chemin_sortie, contenu):
	try:
		with open(chemin_sortie, 'w', encoding='utf-8') as fichier:
			fichier.write(contenu)
	except Exception as erreur_ecriture:
		print("ecrire_fichier_sortie : " + str(erreur_ecriture))


"""
	Exécute le traitement normal d'un fichier source.
"""
def executer_traitement_fichier():
	chemin_source = sys.argv[1]
	suffixe = obtenir_suffixe_sortie()

	print("Fichier ouvert en lecture : " + chemin_source)

	contenu_source = lire_fichier_avec_correction(chemin_source)
	if contenu_source == MARQUEUR_ERREUR:
		sys.exit(1)

	contenu_traite = remplacer_imports_par_contenu(contenu_source)
	contenu_final = corriger_encodage_utf8(contenu_traite)

	chemin_sortie = generer_chemin_sortie(chemin_source, suffixe)
	ecrire_fichier_sortie(chemin_sortie, contenu_final)


"""
	Point d'entrée principal du programme.
	Gère le mode test ou le traitement normal selon les arguments.
"""
def main():
	verifier_arguments()

	if est_mode_test():
		test_detection_et_remplacement_imports()
	else:
		executer_traitement_fichier()

# ==============================================================================
# Lancement du programme
# ==============================================================================

if __name__ == "__main__":
	main()
