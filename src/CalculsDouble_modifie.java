import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Classe principale pour les calculs sur nombres decimaux.
 * Represente un nombre avec partie entiere et decimale.
 * Utilise un preprocesseur pour inclure les fichiers sources.
 */
class CalculsDouble {

// ==============================================================================
// Donnees
// ==============================================================================

	boolean estNegatif = false;
	boolean representation_anglaise = false;
	String partieEntiere;
	String partieDecimale;

// ==============================================================================
// Constructeurs
// ==============================================================================

	/**
	 * Constructeur par defaut.
	 * Initialise un CalculsDouble a zero.
	 */
	public CalculsDouble() {
		partieEntiere = "0";
		partieDecimale = "";
	}

	/**
	 * Constructeur a partir d'un entier.
	 * @param nombre Valeur entiere a stocker.
	 */
	public CalculsDouble(int nombre) {
		partieEntiere = "" + (int)(nombre);
		partieDecimale = "";
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un double.
	 * @param nombre Valeur decimale a stocker.
	 */
	public CalculsDouble(double nombre) {
		partieEntiere = "" + (long)(nombre);
		partieDecimale = partieDecimale(nombre);
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un entier avec representation.
	 * @param nombre Valeur entiere a stocker.
	 * @param representationAnglaise True pour notation avec point.
	 */
	public CalculsDouble(int nombre, boolean representationAnglaise) {
		representation_anglaise = representationAnglaise;
		partieEntiere = "" + (int)(nombre);
		partieDecimale = "0";
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un double avec representation.
	 * @param nombre Valeur decimale a stocker.
	 * @param representationAnglaise True pour notation avec point.
	 */
	public CalculsDouble(double nombre, boolean representationAnglaise) {
		representation_anglaise = representationAnglaise;
		partieEntiere = "" + (long)(nombre);
		partieDecimale = partieDecimale(nombre);
		extraireSigneNegatif();
	}

	/**
	 * Extrait le signe negatif de la partie entiere.
	 * Met a jour estNegatif et nettoie partieEntiere.
	 */
	private void extraireSigneNegatif() {
		if (partieEntiere.contains("-")) {
			estNegatif = true;
			partieEntiere = partieEntiere.replace("-", "");
		}
	}


// ==============================================================================
// Operations arithmetiques
// ==============================================================================

	/**
	 * Additionne deux CalculsDouble.
	 * Gere les signes et les retenues.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Resultat de l'addition.
	 */
	public CalculsDouble additionneNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(compteOcurrences(nombreA.partieDecimale, '0'), compteOcurrences(nombreB.partieDecimale, '0'));
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		CalculsDouble resultat = new CalculsDouble();
		CalculsDouble additionSignee = traiterSignesAddition(nombreA, nombreB, resultat);
		if (additionSignee != null) {
			return additionSignee;
		}
		return calculerSomme(nombreA, nombreB, resultat, chiffresAvantVirgule);
	}

// ------------------------------------------------------------------------------
// Gestion des signes pour l'addition
// ------------------------------------------------------------------------------

	/**
	 * Traite les signes pour l'addition.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat si cas special, null sinon.
	 */
	private CalculsDouble traiterSignesAddition(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (nombreA.estNegatif && nombreB.estNegatif) {
			resultat.estNegatif = true;
			return null;
		}
		if (nombreA.estNegatif || nombreB.estNegatif) {
			return traiterAdditionMixte(nombreA, nombreB, resultat);
		}
		return null;
	}

	/**
	 * Traite l'addition avec un seul nombre negatif.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat de l'addition mixte.
	 */
	private CalculsDouble traiterAdditionMixte(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		int comparaison = compareNombres_abs(nombreA, nombreB);
		if (comparaison == 0) {
			resultat.partieEntiere = "0";
			resultat.partieDecimale = "";
			resultat.estNegatif = false;
			return resultat;
		}
		if (comparaison == 2) {
			CalculsDouble temp = nombreA;
			nombreA = nombreB;
			nombreB = temp;
		}
		return effectuerAdditionMixte(nombreA, nombreB, resultat);
	}

	/**
	 * Effectue l'addition mixte apres reordonnancement.
	 * @param nombreA Premier nombre (le plus grand en valeur absolue).
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat de l'addition.
	 */
	private CalculsDouble effectuerAdditionMixte(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (!nombreA.estNegatif && nombreB.estNegatif) {
			nombreA.estNegatif = false;
			nombreB.estNegatif = false;
			return soustraitNombres(nombreA, nombreB);
		}
		if (nombreA.estNegatif && !nombreB.estNegatif) {
			nombreA.estNegatif = false;
			nombreB.estNegatif = false;
			resultat = soustraitNombres(nombreA, nombreB);
			resultat.estNegatif = true;
			return resultat;
		}
		return null;
	}

// ------------------------------------------------------------------------------
// Calcul de la somme
// ------------------------------------------------------------------------------

	/**
	 * Calcule la somme des parties entieres et decimales.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat de la somme.
	 */
	private CalculsDouble calculerSomme(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		resultat.partieEntiere = additionneChaines(nombreA.partieEntiere, nombreB.partieEntiere);
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			resultat.partieDecimale = additionneChaines(nombreA.partieDecimale, nombreB.partieDecimale);
		} else {
			resultat.partieDecimale = "";
		}
		resultat = gererRetenueDecimale(nombreA, nombreB, resultat);
		resultat = nettoyerSignes(resultat);
		resultat = ajouterZerosDecimaux(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Gere la retenue de la partie decimale vers l'entiere.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat avec retenue geree.
	 */
	private CalculsDouble gererRetenueDecimale(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			int sommePremiersChiffres = chiffreVersInt(nombreA.partieDecimale.charAt(0)) + chiffreVersInt(nombreB.partieDecimale.charAt(0));
			if (sommePremiersChiffres > 9) {
				resultat.partieEntiere = incrementeChaine(resultat.partieEntiere);
				resultat.partieDecimale = resultat.partieDecimale.substring(1, resultat.partieDecimale.length());
			}
		}
		return resultat;
	}

	/**
	 * Nettoie les signes negatifs parasites.
	 * @param resultat Resultat a nettoyer.
	 * @return Resultat nettoye.
	 */
	private CalculsDouble nettoyerSignes(CalculsDouble resultat) {
		if (resultat.partieDecimale.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieDecimale = resultat.partieDecimale.replace("-", "");
		}
		resultat.partieEntiere = resultat.partieEntiere.replace("-", "");
		return resultat;
	}

	/**
	 * Ajoute les zeros necessaires a la partie decimale.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat avec zeros ajoutes.
	 */
	private CalculsDouble ajouterZerosDecimaux(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		if (!resultat.partieDecimale.isEmpty()) {
			int recul = 0;
			if (nombreA.partieDecimale.charAt(0) == '0' || nombreB.partieDecimale.charAt(0) == '0') {
				recul = 1;
			}
			for (int index = 0; index < chiffresAvantVirgule + recul - 2; index++) {
				resultat.partieDecimale = "0" + resultat.partieDecimale;
			}
		}
		return resultat;
	}

	/**
	 * Multiplie deux CalculsDouble.
	 * Utilise la methode de multiplication chiffre par chiffre.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Resultat de la multiplication.
	 */
	public CalculsDouble mult(CalculsDouble nombreA, CalculsDouble nombreB) {
		boolean estNegatif = calculerSigneMultiplication(nombreA, nombreB);
		int nombreDecimales = compterDecimales(nombreA, nombreB);
		CalculsDouble[] nombresOrdonnes = ordonnerParLongueur(nombreA, nombreB);
		nombreA = nombresOrdonnes[0];
		nombreB = nombresOrdonnes[1];
		String chaineA = nombreA.partieEntiere + nombreA.partieDecimale;
		String chaineB = nombreB.partieEntiere + nombreB.partieDecimale;
		String[] matrice = construireMatriceMultiplication(chaineA, chaineB);
		CalculsDouble resultat = additionnerLignesMatrice(matrice, chaineB.length());
		resultat = placerVirgule(resultat, nombreA, nombreB, nombreDecimales);
		resultat.estNegatif = estNegatif;
		return resultat;
	}

// ------------------------------------------------------------------------------
// Preparation de la multiplication
// ------------------------------------------------------------------------------

	/**
	 * Calcule le signe du resultat de la multiplication.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return True si le resultat est negatif.
	 */
	private boolean calculerSigneMultiplication(CalculsDouble nombreA, CalculsDouble nombreB) {
		boolean unSeulNegatif = nombreA.estNegatif || nombreB.estNegatif;
		boolean lesDeuxNegatifs = nombreA.estNegatif && nombreB.estNegatif;
		return unSeulNegatif && !lesDeuxNegatifs;
	}

	/**
	 * Compte le nombre total de decimales.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Nombre total de decimales.
	 */
	private int compterDecimales(CalculsDouble nombreA, CalculsDouble nombreB) {
		int total = 0;
		if (!nombreA.partieDecimale.isEmpty()) {
			total += nombreA.partieDecimale.length();
		}
		if (!nombreB.partieDecimale.isEmpty()) {
			total += nombreB.partieDecimale.length();
		}
		return total;
	}

	/**
	 * Ordonne les nombres par longueur decroissante.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Tableau avec le plus grand en premier.
	 */
	private CalculsDouble[] ordonnerParLongueur(CalculsDouble nombreA, CalculsDouble nombreB) {
		int longueurA = nombreA.partieEntiere.length() + nombreA.partieDecimale.length();
		int longueurB = nombreB.partieEntiere.length() + nombreB.partieDecimale.length();
		if (longueurB > longueurA) {
			return new CalculsDouble[]{nombreB, nombreA};
		}
		return new CalculsDouble[]{nombreA, nombreB};
	}

// ------------------------------------------------------------------------------
// Construction de la matrice de multiplication
// ------------------------------------------------------------------------------

	/**
	 * Construit la matrice des produits partiels.
	 * @param chaineA Chaine du premier nombre.
	 * @param chaineB Chaine du second nombre.
	 * @return Matrice des produits partiels.
	 */
	private String[] construireMatriceMultiplication(String chaineA, String chaineB) {
		int longueurA = chaineA.length();
		int longueurB = chaineB.length();
		String[] matrice = new String[longueurB];
		for (int index = 0; index < longueurB; index++) {
			matrice[index] = "";
		}
		int retenue = 0;
		for (int indexB = longueurB - 1; indexB >= 0; indexB--) {
			retenue = 0;
			retenue = calculerLigneMatrice(chaineA, chaineB, matrice, indexB, retenue);
			ajouterZerosDecalage(matrice, longueurB, indexB);
		}
		return matrice;
	}

	/**
	 * Calcule une ligne de la matrice de multiplication.
	 * @param chaineA Chaine du premier nombre.
	 * @param chaineB Chaine du second nombre.
	 * @param matrice Matrice a remplir.
	 * @param indexB Index dans B.
	 * @param retenue Retenue initiale.
	 * @return Retenue finale.
	 */
	private int calculerLigneMatrice(String chaineA, String chaineB, String[] matrice, int indexB, int retenue) {
		int longueurA = chaineA.length();
		int longueurB = chaineB.length();
		for (int indexA = longueurA - 1; indexA >= 0; indexA--) {
			int chiffreA = Integer.parseInt("" + chaineA.charAt(indexA));
			int chiffreB = Integer.parseInt("" + chaineB.charAt(indexB));
			int produit = retenue + (chiffreA * chiffreB);
			String produitChaine = "" + produit;
			if (produitChaine.length() > 1 && indexA > 0) {
				retenue = Integer.parseInt(produitChaine.substring(0, produitChaine.length() - 1));
				produitChaine = produitChaine.substring(produitChaine.length() - 1);
			} else {
				retenue = 0;
			}
			int positionMatrice = longueurB - indexB - 1;
			matrice[positionMatrice] = produitChaine + matrice[positionMatrice];
		}
		return retenue;
	}

	/**
	 * Ajoute les zeros de decalage a une ligne de la matrice.
	 * @param matrice Matrice a modifier.
	 * @param longueurB Longueur de B.
	 * @param indexB Index courant dans B.
	 */
	private void ajouterZerosDecalage(String[] matrice, int longueurB, int indexB) {
		int positionMatrice = longueurB - indexB - 1;
		for (int indexZero = 0; indexZero < positionMatrice; indexZero++) {
			matrice[positionMatrice] += "0";
		}
	}

// ------------------------------------------------------------------------------
// Addition des lignes de la matrice
// ------------------------------------------------------------------------------

	/**
	 * Additionne toutes les lignes de la matrice.
	 * @param matrice Matrice des produits partiels.
	 * @param longueurB Nombre de lignes.
	 * @return Resultat de la somme.
	 */
	private CalculsDouble additionnerLignesMatrice(String[] matrice, int longueurB) {
		CalculsDouble resultat = new CalculsDouble(0);
		CalculsDouble valeurLigne = new CalculsDouble(0);
		for (int index = 0; index < longueurB; index++) {
			valeurLigne.partieEntiere = matrice[index];
			resultat.partieEntiere = additionneNombres(valeurLigne, resultat).partieEntiere;
		}
		return resultat;
	}

	/**
	 * Place la virgule dans le resultat.
	 * @param resultat Resultat sans virgule.
	 * @param nombreA Premier nombre original.
	 * @param nombreB Second nombre original.
	 * @param nombreDecimales Nombre de decimales attendues.
	 * @return Resultat avec virgule placee.
	 */
	private CalculsDouble placerVirgule(CalculsDouble resultat, CalculsDouble nombreA, CalculsDouble nombreB, int nombreDecimales) {
		if (!nombreA.partieDecimale.isEmpty() || !nombreB.partieDecimale.isEmpty()) {
			int position = resultat.partieEntiere.length() - nombreDecimales;
			resultat.partieDecimale = resultat.partieEntiere.substring(position);
			resultat.partieEntiere = resultat.partieEntiere.substring(0, position);
		}
		return resultat;
	}

	/**
	 * Soustrait deux CalculsDouble (A - B).
	 * Utilise l'addition avec l'oppose de B.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre a soustraire.
	 * @return Resultat de la soustraction.
	 */
	public CalculsDouble soustraitNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(
			compteOcurrences(nombreA.partieDecimale, '0'),
			compteOcurrences(nombreB.partieDecimale, '0')
		);
		CalculsDouble resultat = new CalculsDouble();
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		integrerSignesPourSoustraction(nombreA, nombreB);
		resultat = calculerDifference(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Integre les signes dans les parties entieres pour la soustraction.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 */
	private void integrerSignesPourSoustraction(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.estNegatif || nombreB.estNegatif) {
			if (nombreA.estNegatif) {
				nombreA.partieEntiere = "-" + nombreA.partieEntiere;
			}
			if (nombreB.estNegatif) {
				nombreB.partieEntiere = "-" + nombreB.partieEntiere;
			}
		}
	}

	/**
	 * Calcule la difference des parties entieres et decimales.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat de la difference.
	 */
	private CalculsDouble calculerDifference(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		resultat.partieEntiere = soustraitChaines(nombreA.partieEntiere, nombreB.partieEntiere);
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			resultat.partieDecimale = soustraitChaines(nombreA.partieDecimale, nombreB.partieDecimale);
		} else {
			resultat.partieDecimale = "";
			resultat = nettoyerSignesSoustraction(resultat);
			return resultat;
		}
		resultat = nettoyerSignesSoustraction(resultat);
		resultat = ajouterZerosSoustraction(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Nettoie les signes negatifs parasites apres soustraction.
	 * @param resultat Resultat a nettoyer.
	 * @return Resultat nettoye.
	 */
	private CalculsDouble nettoyerSignesSoustraction(CalculsDouble resultat) {
		if (resultat.partieDecimale.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieDecimale = resultat.partieDecimale.replace("-", "");
		}
		if (resultat.partieEntiere.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieEntiere = resultat.partieEntiere.replace("-", "");
		}
		return resultat;
	}

	/**
	 * Ajoute les zeros necessaires a la partie decimale.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat avec zeros ajoutes.
	 */
	private CalculsDouble ajouterZerosSoustraction(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		int recul = 0;
		if (nombreA.partieDecimale.charAt(0) == '0' || nombreB.partieDecimale.charAt(0) == '0') {
			recul = 1;
		}
		for (int index = 0; index < chiffresAvantVirgule + recul - 2; index++) {
			resultat.partieDecimale = "0" + resultat.partieDecimale;
		}
		return resultat;
	}

	/**
	 * Division avec precision par defaut (8 chiffres significatifs).
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @return Tableau [quotient, reste].
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur) {
		return division_boucle(dividende, diviseur, true, 8);
	}

	/**
	 * Division avec precision specifiee.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param chiffresSignificatifs Precision souhaitee.
	 * @return Tableau [quotient, reste].
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur, int chiffresSignificatifs) {
		return division_boucle(dividende, diviseur, true, chiffresSignificatifs);
	}

	/**
	 * Division complete avec tous les parametres.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat sinon arrondit.
	 * @param chiffresSignificatifs Precision souhaitee (max 8).
	 * @return Tableau [quotient, reste] ou null si division par zero.
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur, boolean tronquee, int chiffresSignificatifs) {
		if (!tronquee) {
			chiffresSignificatifs++;
		}
		chiffresSignificatifs = validerChiffresSignificatifs(chiffresSignificatifs);
		if (estNul(diviseur)) {
			aff("Impossible de diviser par 0.");
			return null;
		}
		CalculsDouble[] casParticulier = verifierCasParticuliers(dividende, diviseur);
		if (casParticulier != null) {
			return casParticulier;
		}
		return effectuerDivision(dividende, diviseur, tronquee, chiffresSignificatifs);
	}

// ------------------------------------------------------------------------------
// Validation et cas particuliers
// ------------------------------------------------------------------------------

	/**
	 * Valide et limite le nombre de chiffres significatifs.
	 * @param chiffresSignificatifs Valeur demandee.
	 * @return Valeur validee (max 9).
	 */
	private int validerChiffresSignificatifs(int chiffresSignificatifs) {
		if (chiffresSignificatifs > 9) {
			affnn("Erreur : impossible de faire ");
			aff("plus de 8 chiffres significatifs.");
			return 9;
		}
		return chiffresSignificatifs;
	}

	/**
	 * Verifie les cas particuliers de division.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @return Resultat si cas particulier, null sinon.
	 */
	private CalculsDouble[] verifierCasParticuliers(CalculsDouble dividende, CalculsDouble diviseur) {
		if (estNul(dividende)) {
			return creerResultatZero();
		}
		if (diviseur.equals(new CalculsDouble(1))) {
			return creerResultatDiviseurUn(dividende);
		}
		if (dividende.equals(diviseur)) {
			return creerResultatEgaux();
		}
		return null;
	}

	/**
	 * Cree le resultat pour dividende = 0.
	 * @return Tableau [0, 0].
	 */
	private CalculsDouble[] creerResultatZero() {
		return new CalculsDouble[]{new CalculsDouble(0), new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour diviseur = 1.
	 * @param dividende Le dividende.
	 * @return Tableau [dividende, 0].
	 */
	private CalculsDouble[] creerResultatDiviseurUn(CalculsDouble dividende) {
		CalculsDouble quotient = new CalculsDouble(0);
		quotient.partieEntiere = dividende.partieEntiere;
		quotient.partieDecimale = dividende.partieDecimale;
		return new CalculsDouble[]{quotient, new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour dividende = diviseur.
	 * @return Tableau [1, 0].
	 */
	private CalculsDouble[] creerResultatEgaux() {
		return new CalculsDouble[]{new CalculsDouble(1), new CalculsDouble(0)};
	}

// ------------------------------------------------------------------------------
// Algorithme de division
// ------------------------------------------------------------------------------

	/**
	 * Effectue la division par soustractions successives.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat.
	 * @param chiffresSignificatifs Precision.
	 * @return Tableau [quotient, reste].
	 */
	private CalculsDouble[] effectuerDivision(CalculsDouble dividende, CalculsDouble diviseur, boolean tronquee, int chiffresSignificatifs) {
		int chiffresRestants = chiffresSignificatifs;
		CalculsDouble reste = new CalculsDouble(0);
		reste.partieEntiere = dividende.partieEntiere + dividende.partieDecimale;
		CalculsDouble quotient = new CalculsDouble(0);
		CalculsDouble diviseurComplet = new CalculsDouble(0);
		diviseurComplet.partieEntiere = diviseur.partieEntiere + diviseur.partieDecimale;
		boolean apresVirguleGlobal = false;
		CalculsDouble[] etatDivision = new CalculsDouble[]{quotient, reste};
		etatDivision = boucleDivision(etatDivision, diviseurComplet, chiffresRestants, chiffresSignificatifs, apresVirguleGlobal);
		quotient = etatDivision[0];
		reste = etatDivision[1];
		quotient.estNegatif = calculerSigneDivision(dividende, diviseur);
		if (!tronquee) {
			quotient = arrondi(quotient, chiffresSignificatifs - 1);
		}
		return new CalculsDouble[]{quotient, reste};
	}

	/**
	 * Boucle principale de la division.
	 * @param etat Tableau [quotient, reste].
	 * @param diviseur Diviseur complet.
	 * @param chiffresRestants Chiffres restants a calculer.
	 * @param chiffresTotal Chiffres totaux.
	 * @param apresVirgule Indicateur de position apres virgule.
	 * @return Tableau [quotient, reste] mis a jour.
	 */
	private CalculsDouble[] boucleDivision(CalculsDouble[] etat, CalculsDouble diviseur, int chiffresRestants, int chiffresTotal, boolean apresVirgule) {
		CalculsDouble quotient = etat[0];
		CalculsDouble reste = etat[1];
		do {
			boolean resteInferieur = !resteSuperieurOuEgal(reste, diviseur);
			if (resteInferieur) {
				apresVirgule = true;
			}
			if (chiffresRestants > 0 && resteInferieur) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
			reste = soustraitNombres(reste, diviseur);
			quotient = incrementerQuotient(quotient, apresVirgule, chiffresRestants, chiffresTotal);
			resteInferieur = !resteSuperieurOuEgal(reste, diviseur);
			if (resteInferieur) {
				apresVirgule = true;
			}
			if (chiffresRestants > 0 && resteInferieur) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
		} while (resteSuperieurOuEgal(reste, diviseur));
		return new CalculsDouble[]{quotient, reste};
	}

	/**
	 * Verifie si le reste est superieur ou egal au diviseur.
	 * @param reste Le reste courant.
	 * @param diviseur Le diviseur.
	 * @return True si reste >= diviseur.
	 */
	private boolean resteSuperieurOuEgal(CalculsDouble reste, CalculsDouble diviseur) {
		int comparaison = compareNombres(reste, diviseur);
		return comparaison == 0 || comparaison == 1;
	}

	/**
	 * Incremente le quotient selon la position.
	 * @param quotient Quotient actuel.
	 * @param apresVirgule Si on est apres la virgule.
	 * @param chiffresRestants Chiffres restants.
	 * @param chiffresTotal Chiffres totaux.
	 * @return Quotient incremente.
	 */
	private CalculsDouble incrementerQuotient(CalculsDouble quotient, boolean apresVirgule, int chiffresRestants, int chiffresTotal) {
		CalculsDouble increment = new CalculsDouble(0);
		if (!apresVirgule) {
			increment.partieEntiere = "1";
			increment.partieDecimale = "";
		} else {
			increment.partieEntiere = "0";
			String zeros = "";
			for (int index = chiffresRestants + 1; index < chiffresTotal; index++) {
				zeros += "0";
			}
			increment.partieDecimale = zeros + "1";
		}
		return additionneNombres(quotient, increment);
	}

	/**
	 * Calcule le signe du quotient.
	 * @param dividende Le dividende.
	 * @param diviseur Le diviseur.
	 * @return True si le quotient est negatif.
	 */
	private boolean calculerSigneDivision(CalculsDouble dividende, CalculsDouble diviseur) {
		boolean unSeulNegatif = dividende.estNegatif || diviseur.estNegatif;
		boolean lesDeuxNegatifs = dividende.estNegatif && diviseur.estNegatif;
		return unSeulNegatif && !lesDeuxNegatifs;
	}


// ==============================================================================
// Operations mathematiques
// ==============================================================================

	/**
	 * Enleve les zeros en debut de la partie entiere.
	 * Exemple : "007" devient "7", "000" devient "0".
	 * @param partieEntiere Chaine representant la partie entiere.
	 * @return Chaine nettoyee des zeros superflus.
	 */
	public String epureZerosEnt(String partieEntiere) {
		int longueur = partieEntiere.length();
		int index = 0;
		while (index < longueur && partieEntiere.charAt(index) == '0') {
			index++;
		}
		if (index == longueur) {
			return "0";
		}
		return partieEntiere.substring(index, longueur);
	}

	/**
	 * Enleve les zeros inutiles en fin de partie decimale.
	 * Exemple : "1200" devient "12", "100" devient "1".
	 * @param partieDecimale Chaine representant la partie decimale.
	 * @return Chaine nettoyee des zeros superflus.
	 */
	public String envleveZerosInutils(String partieDecimale) {
		int longueur = partieDecimale.length();
		int positionFinale = longueur;
		for (int index = 0; index < longueur; index++) {
			if (partieDecimale.charAt(index) == '0') {
				positionFinale = index;
			} else {
				positionFinale = longueur;
			}
		}
		return partieDecimale.substring(0, positionFinale);
	}

	/**
	 * Ajuste les chiffres significatifs de la partie decimale.
	 * Si nombreChiffres = -1, enleve les zeros inutiles (mode maths).
	 * Sinon, ajuste a exactement nombreChiffres decimales.
	 * @param partieDecimale Chaine de la partie decimale.
	 * @param nombreChiffres Nombre de chiffres souhaites (-1 = minimum).
	 * @return Partie decimale ajustee.
	 */
	public String chiffresSignificatifs(String partieDecimale, int nombreChiffres) {
		int longueur = partieDecimale.length();
		int zerosAAjouter = nombreChiffres - longueur;
		partieDecimale = envleveZerosInutils(partieDecimale);
		if (nombreChiffres == -1) {
			return partieDecimale;
		}
		for (int index = 0; index < zerosAAjouter; index++) {
			partieDecimale += '0';
		}
		return partieDecimale;
	}


	/**
	 * Retourne la valeur absolue d'un entier.
	 * @param nombre Entier a traiter.
	 * @return Valeur absolue.
	 */
	public int abs(int nombre) {
		if (nombre < 0) {
			return -nombre;
		}
		return nombre;
	}

	/**
	 * Met la valeur absolue sur l'instance courante.
	 * Modifie directement estNegatif a false.
	 */
	public void abs() {
		this.estNegatif = false;
	}

	/**
	 * Retourne la valeur absolue d'un CalculsDouble.
	 * Cree une copie avec estNegatif a false.
	 * @param nombre CalculsDouble a traiter.
	 * @return Nouvelle instance avec valeur absolue.
	 */
	public CalculsDouble abs(CalculsDouble nombre) {
		CalculsDouble resultat = new CalculsDouble(0);
		resultat.partieEntiere = nombre.partieEntiere;
		resultat.partieDecimale = nombre.partieDecimale;
		resultat.representation_anglaise = nombre.representation_anglaise;
		resultat.estNegatif = false;
		return resultat;
	}

	/**
	 * Retourne la partie entiere d'un double.
	 * @param nombre Double a tronquer.
	 * @return Partie entiere.
	 */
	public int partieEntiere(double nombre) {
		return (int)(nombre);
	}

	/**
	 * Retourne la partie entiere de l'instance courante.
	 * @return Partie entiere ou 0 si vide.
	 */
	public int partieEntiere() {
		if (this.partieEntiere.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(this.partieEntiere);
	}


	/**
	 * Arrondit un CalculsDouble a l'entier le plus proche.
	 * @param nombre CalculsDouble a arrondir.
	 * @return Partie entiere arrondie.
	 */
	public int arrondiEntier(CalculsDouble nombre) {
		String resultat = arrondi(nombre).partieEntiere;
		if (resultat.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(resultat);
	}

	/**
	 * Arrondit un CalculsDouble et retourne une chaine.
	 * @param nombre CalculsDouble a arrondir.
	 * @return Chaine de l'arrondi.
	 */
	public String arrondiString(CalculsDouble nombre) {
		return arrondiString(nombre, 0);
	}

	/**
	 * Arrondit un CalculsDouble avec precision et retourne une chaine.
	 * @param nombre CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales souhaitees.
	 * @return Chaine de l'arrondi.
	 */
	public String arrondiString(CalculsDouble nombre, int nombreDecimales) {
		return "" + arrondi(nombre, nombreDecimales);
	}

	/**
	 * Arrondit un CalculsDouble a l'entier le plus proche.
	 * @param nombre CalculsDouble a arrondir.
	 * @return CalculsDouble arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble nombre) {
		return arrondi(nombre, 0);
	}

// ------------------------------------------------------------------------------
// Arrondi avec precision
// ------------------------------------------------------------------------------

	/**
	 * Arrondit un CalculsDouble avec precision specifiee.
	 * @param nombreOriginal CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales (0 = entier).
	 * @return CalculsDouble arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble nombreOriginal, int nombreDecimales) {
		CalculsDouble nombre = copie(nombreOriginal);
		if (nombreDecimales < 1) {
			return arrondiEntierSeul(nombre);
		}
		if (nombre.partieDecimale.isEmpty()) {
			return nombre;
		}
		if (nombre.partieDecimale.length() <= nombreDecimales) {
			return nombre;
		}
		return arrondiAvecDecimales(nombre, nombreDecimales);
	}

	/**
	 * Arrondit a l'entier sans garder de decimales.
	 * @param nombre CalculsDouble a arrondir.
	 * @return CalculsDouble arrondi a l'entier.
	 */
	private CalculsDouble arrondiEntierSeul(CalculsDouble nombre) {
		if (nombre.partieDecimale.isEmpty()) {
			return nombre;
		}
		if (Integer.parseInt("" + nombre.partieDecimale.charAt(0)) > 4) {
			nombre = this.additionneNombres(nombre, new CalculsDouble(1));
		}
		nombre.partieDecimale = "";
		return nombre;
	}

	/**
	 * Arrondit en conservant un nombre de decimales.
	 * @param nombre CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales a garder.
	 * @return CalculsDouble arrondi.
	 */
	private CalculsDouble arrondiAvecDecimales(CalculsDouble nombre, int nombreDecimales) {
		if (Integer.parseInt("" + nombre.partieDecimale.charAt(nombreDecimales)) <= 4) {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, nombreDecimales);
			return nombre;
		}
		return propagerArrondi(nombre, nombreDecimales);
	}

	/**
	 * Propage l'arrondi vers les chiffres superieurs.
	 * @param nombre CalculsDouble a traiter.
	 * @param position Position de depart.
	 * @return CalculsDouble avec arrondi propage.
	 */
	private CalculsDouble propagerArrondi(CalculsDouble nombre, int position) {
		int positionCourante = position;
		boolean continuer = true;
		while (positionCourante > 0 && continuer) {
			if (Integer.parseInt("" + nombre.partieDecimale.charAt(positionCourante)) <= 4) {
				continuer = false;
			} else {
				nombre = incrementerDecimale(nombre, positionCourante);
				positionCourante--;
			}
		}
		if (positionCourante == 0 && Integer.parseInt("" + nombre.partieDecimale.charAt(0)) > 4) {
			nombre.partieDecimale = "";
			nombre.partieEntiere = "" + (Integer.parseInt(nombre.partieEntiere) + 1);
		}
		return nombre;
	}

	/**
	 * Incremente une decimale a une position donnee.
	 * @param nombre CalculsDouble a modifier.
	 * @param position Position de la decimale.
	 * @return CalculsDouble modifie.
	 */
	private CalculsDouble incrementerDecimale(CalculsDouble nombre, int position) {
		int chiffreIncrement = Integer.parseInt("" + nombre.partieDecimale.charAt(position - 1)) + 1;
		String chaineIncrement = "" + chiffreIncrement;
		if (chaineIncrement.length() > 1) {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, position);
		} else {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, position - 1) + chaineIncrement;
		}
		return nombre;
	}


	/**
	 * Extrait la partie decimale d'un double.
	 * Gere les notations avec virgule ou point.
	 * @param nombre Double dont extraire la decimale.
	 * @return Chaine representant la partie decimale.
	 */
	public String partieDecimale(double nombre) {
		String nombreChaine = "" + nombre;
		String separateur = ".";
		if (nombreChaine.indexOf(",") != -1) {
			separateur = ",";
		}
		if (nombreChaine.indexOf(separateur) == -1) {
			return "0";
		}
		return extraireApresPoint(nombreChaine, separateur.charAt(0));
	}

	/**
	 * Extrait la partie apres le separateur decimal.
	 * @param nombreChaine Chaine du nombre complet.
	 * @param separateur Caractere separateur (. ou ,).
	 * @return Partie apres le separateur.
	 */
	private String extraireApresPoint(String nombreChaine, char separateur) {
		boolean pointTrouve = false;
		String resultat = "";
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			if (pointTrouve) {
				resultat += nombreChaine.charAt(index);
			}
			if (nombreChaine.charAt(index) == separateur) {
				pointTrouve = true;
			}
		}
		return resultat;
	}


	/**
	 * Egalise les parties decimales de deux CalculsDouble.
	 * Ajoute des zeros pour que les deux aient la meme longueur.
	 * @param nombreA Premier nombre a egaliser.
	 * @param nombreB Second nombre a egaliser.
	 * @return Tableau des deux nombres egalises.
	 */
	public CalculsDouble[] egaliseNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.partieDecimale.length() != nombreB.partieDecimale.length()) {
			completerDecimales(nombreA, nombreB);
		}
		CalculsDouble[] resultat = new CalculsDouble[2];
		resultat[0] = nombreA;
		resultat[1] = nombreB;
		return resultat;
	}

	/**
	 * Complete les decimales du nombre le plus court.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 */
	private void completerDecimales(CalculsDouble nombreA, CalculsDouble nombreB) {
		int longueurMax = Math.max(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		if (longueurMax == nombreA.partieDecimale.length()) {
			nombreB.partieDecimale += genererZeros(nombreA.partieDecimale.length() - nombreB.partieDecimale.length());
		}
		if (longueurMax == nombreB.partieDecimale.length()) {
			nombreA.partieDecimale += genererZeros(nombreB.partieDecimale.length() - nombreA.partieDecimale.length());
		}
	}

	/**
	 * Genere une chaine de zeros.
	 * @param nombreZeros Nombre de zeros a generer.
	 * @return Chaine de zeros.
	 */
	private String genererZeros(int nombreZeros) {
		String zeros = "";
		for (int index = 0; index < nombreZeros; index++) {
			zeros += "0";
		}
		return zeros;
	}

	/**
	 * Cree une copie profonde d'un CalculsDouble.
	 * @param original CalculsDouble a copier.
	 * @return Nouvelle instance avec les memes valeurs.
	 */
	public CalculsDouble copie(CalculsDouble original) {
		CalculsDouble resultat = new CalculsDouble(0);
		resultat.partieEntiere = original.partieEntiere;
		resultat.partieDecimale = original.partieDecimale;
		resultat.estNegatif = original.estNegatif;
		resultat.representation_anglaise = original.representation_anglaise;
		return resultat;
	}

	/**
	 * Compare l'instance courante avec un autre CalculsDouble.
	 * @param autre CalculsDouble a comparer.
	 * @return True si les deux nombres sont egaux.
	 */
	public boolean equals(CalculsDouble autre) {
		return (compareNombres(this, autre) == 0);
	}

	/**
	 * Compare deux CalculsDouble en tenant compte du signe.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egaux, 1 si A > B, 2 si B > A, -1 si erreur.
	 */
	public int compareNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.estNegatif && !nombreB.estNegatif) {
			return 2;
		}
		if (!nombreA.estNegatif && nombreB.estNegatif) {
			return 1;
		}
		if (nombreA.estNegatif && nombreB.estNegatif) {
			return inverserResultatComparaison(compareNombres_abs(nombreA, nombreB));
		}
		if (!nombreA.estNegatif && !nombreB.estNegatif) {
			return compareNombres_abs(nombreA, nombreB);
		}
		return -1;
	}

	/**
	 * Inverse le resultat de comparaison pour nombres negatifs.
	 * @param resultat Resultat original.
	 * @return Resultat inverse (1 devient 2, 2 devient 1).
	 */
	private int inverserResultatComparaison(int resultat) {
		if (resultat == 0) {
			return 0;
		}
		if (resultat == 1) {
			return 2;
		}
		if (resultat == 2) {
			return 1;
		}
		return -1;
	}

// ------------------------------------------------------------------------------
// Comparaison en valeur absolue
// ------------------------------------------------------------------------------

	/**
	 * Compare deux CalculsDouble en valeur absolue.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egaux, 1 si |A| > |B|, 2 si |B| > |A|.
	 */
	public int compareNombres_abs(CalculsDouble nombreA, CalculsDouble nombreB) {
		int comparaisonEntiere = comparerPartiesEntieres(nombreA, nombreB);
		if (comparaisonEntiere != 0) {
			return comparaisonEntiere;
		}
		return comparerPartiesDecimales(nombreA, nombreB);
	}

	/**
	 * Compare les parties entieres de deux nombres.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerPartiesEntieres(CalculsDouble nombreA, CalculsDouble nombreB) {
		String entA = supprimerZerosGauche(nombreA.partieEntiere);
		String entB = supprimerZerosGauche(nombreB.partieEntiere);
		if (entA.length() > entB.length()) {
			return 1;
		}
		if (entA.length() < entB.length()) {
			return 2;
		}
		return comparerChiffresParChiffres(entA, entB);
	}

	/**
	 * Compare deux chaines chiffre par chiffre.
	 * @param chaineA Premiere chaine.
	 * @param chaineB Deuxieme chaine.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerChiffresParChiffres(String chaineA, String chaineB) {
		int longueur = chaineA.length();
		for (int index = 0; index < longueur; index++) {
			int chiffreA = chiffreVersInt(chaineA.charAt(index));
			int chiffreB = chiffreVersInt(chaineB.charAt(index));
			if (chiffreA > chiffreB) {
				return 1;
			}
			if (chiffreA < chiffreB) {
				return 2;
			}
		}
		return 0;
	}

	/**
	 * Compare les parties decimales de deux nombres.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerPartiesDecimales(CalculsDouble nombreA, CalculsDouble nombreB) {
		int longueurMin = Math.min(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		for (int index = 0; index < longueurMin; index++) {
			int chiffreA = Integer.parseInt("" + nombreA.partieDecimale.charAt(index));
			int chiffreB = Integer.parseInt("" + nombreB.partieDecimale.charAt(index));
			if (chiffreA > chiffreB) {
				return 1;
			}
			if (chiffreB > chiffreA) {
				return 2;
			}
		}
		return comparerDecimalesRestantes(nombreA, nombreB, longueurMin);
	}

	/**
	 * Compare les decimales restantes apres la longueur commune.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param longueurMin Longueur commune deja comparee.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerDecimalesRestantes(CalculsDouble nombreA, CalculsDouble nombreB, int longueurMin) {
		int longueurMax = Math.max(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		if (nombreA.partieDecimale.length() != nombreB.partieDecimale.length() && longueurMax > longueurMin) {
			if (nombreA.partieDecimale.length() > longueurMin) {
				if (Integer.parseInt("" + nombreA.partieDecimale.charAt(longueurMin)) > 0) {
					return 1;
				}
			}
			if (nombreB.partieDecimale.length() > longueurMin) {
				if (Integer.parseInt("" + nombreB.partieDecimale.charAt(longueurMin)) > 0) {
					return 2;
				}
			}
		}
		return 0;
	}


	/**
	 * Decompose un nombre en facteurs premiers.
	 * Ignore la partie decimale (troncature).
	 * @param nombre CalculsDouble a decomposer.
	 * @return Tableau des facteurs premiers ou null si invalide.
	 */
	public int[] decompose_en_facteurs_premiers(CalculsDouble nombre) {
		if (!nombre.partieDecimale.isEmpty()) {
			aff("n0.dec!=" + '"' + '"' + " : on prend la troncature entiere du nombre.");
		}
		if (nombre.partieEntiere.isEmpty()) {
			return null;
		}
		int valeurCourante = Integer.parseInt(nombre.partieEntiere);
		aff("n = " + valeurCourante);
		String facteursChaine = construireChaineFacteurs(valeurCourante);
		return convertirEnTableauFacteurs(facteursChaine);
	}

	/**
	 * Construit la chaine des facteurs premiers separes par :.
	 * @param valeur Valeur a factoriser.
	 * @return Chaine des facteurs (ex: "2:3:5:7").
	 */
	private String construireChaineFacteurs(int valeur) {
		String facteursChaine = "";
		while (!estPremier(valeur)) {
			int facteur = trouverPremierFacteur(valeur);
			facteursChaine += facteur + ":";
			valeur = valeur / facteur;
			if (valeur == 0) {
				valeur = 1;
			}
			aff("n = " + valeur);
		}
		facteursChaine += "" + valeur;
		return facteursChaine;
	}

	/**
	 * Trouve le premier facteur premier d'un nombre.
	 * @param valeur Nombre a factoriser.
	 * @return Premier facteur trouve.
	 */
	private int trouverPremierFacteur(int valeur) {
		int longueur = ("" + valeur).length();
		char dernierChiffre = ("" + valeur).charAt(longueur - 1);
		if (dernierChiffre == '0' || dernierChiffre == '5') {
			return 5;
		}
		if (dernierChiffre == '2' || dernierChiffre == '4' || dernierChiffre == '8') {
			return 2;
		}
		if (valeur % 3 == 0) {
			return 3;
		}
		for (int diviseur = 2; diviseur < valeur && valeur % diviseur != 0; diviseur++) {
		}
		return valeur;
	}

// ------------------------------------------------------------------------------
// Conversion et verification
// ------------------------------------------------------------------------------

	/**
	 * Convertit une chaine de facteurs en tableau d'entiers.
	 * @param facteursChaine Chaine de facteurs separes par :.
	 * @return Tableau des facteurs.
	 */
	private int[] convertirEnTableauFacteurs(String facteursChaine) {
		aff("restmp = " + facteursChaine);
		String[] facteursSplit = facteursChaine.split(":");
		int nombreFacteurs = compterFacteursValides(facteursSplit);
		int[] resultat = remplirTableauFacteurs(facteursSplit, nombreFacteurs);
		afficherVerification(resultat);
		return resultat;
	}

	/**
	 * Compte le nombre de facteurs valides dans le tableau.
	 * @param facteurs Tableau de chaines.
	 * @return Nombre de chaines non vides.
	 */
	private int compterFacteursValides(String[] facteurs) {
		int compteur = 0;
		for (int index = 0; index < facteurs.length; index++) {
			if (!facteurs[index].isEmpty()) {
				compteur++;
			}
		}
		return compteur;
	}

	/**
	 * Remplit un tableau d'entiers avec les facteurs valides.
	 * @param facteurs Tableau de chaines.
	 * @param nombreFacteurs Taille du tableau resultat.
	 * @return Tableau d'entiers.
	 */
	private int[] remplirTableauFacteurs(String[] facteurs, int nombreFacteurs) {
		int[] resultat = new int[nombreFacteurs];
		int indexResultat = 0;
		for (int index = 0; index < facteurs.length; index++) {
			if (!facteurs[index].isEmpty()) {
				resultat[indexResultat] = Integer.parseInt(facteurs[index]);
				indexResultat++;
			}
		}
		return resultat;
	}

	/**
	 * Affiche la verification du produit des facteurs.
	 * @param facteurs Tableau des facteurs.
	 */
	private void afficherVerification(int[] facteurs) {
		int verification = 1;
		for (int index = 0; index < facteurs.length; index++) {
			verification *= facteurs[index];
		}
		aff("Verification : n = " + verification);
	}


	/**
	 * Calcule la somme des chiffres d'une chaine numerique.
	 * @param nombreChaine Chaine de chiffres.
	 * @return Somme de tous les chiffres.
	 */
	public int somme_des_chiffres(String nombreChaine) {
		if (nombreChaine.isEmpty()) {
			return 0;
		}
		int resultat = 0;
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			resultat += Integer.parseInt("" + nombreChaine.charAt(index));
		}
		return resultat;
	}

	/**
	 * Calcule le produit des chiffres d'une chaine numerique.
	 * @param nombreChaine Chaine de chiffres.
	 * @return Produit de tous les chiffres.
	 */
	public int produit_des_chiffres(String nombreChaine) {
		if (nombreChaine.isEmpty()) {
			return 0;
		}
		int resultat = 1;
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			resultat *= Integer.parseInt("" + nombreChaine.charAt(index));
		}
		return resultat;
	}

	/**
	 * Teste si un entier est un nombre premier.
	 * @param nombre Entier a tester.
	 * @return True si le nombre est premier.
	 */
	public boolean estPremier(int nombre) {
		int limiteRecherche = 1 + (int)(Math.sqrt((double)(nombre)));
		for (int diviseur = 2; diviseur < limiteRecherche; diviseur++) {
			if (nombre % diviseur == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calcule et affiche les n premiers nombres premiers.
	 * @param nombrePremiers Quantite de nombres premiers a trouver.
	 */
	public void clacule_n_premiers(int nombrePremiers) {
		if (nombrePremiers < 1) {
			return;
		}
		int[] resultat = new int[nombrePremiers];
		int compteur = 0;
		int candidat = 2;
		while (compteur < nombrePremiers) {
			if (estPremier(candidat)) {
				resultat[compteur] = candidat;
				compteur++;
			}
			candidat++;
		}
		afficherTableauPremiers(resultat, nombrePremiers);
	}

	/**
	 * Affiche un tableau de nombres premiers.
	 * @param tableau Tableau des nombres premiers.
	 * @param nombrePremiers Quantite de nombres dans le tableau.
	 */
	private void afficherTableauPremiers(int[] tableau, int nombrePremiers) {
		affnn("p" + nombrePremiers + " = {");
		for (int index = 0; index < tableau.length - 1; index++) {
			affnn(tableau[index] + ", ");
		}
		aff(tableau[tableau.length - 1] + "};");
	}


// ==============================================================================
// Fonctions utilitaires
// ==============================================================================


	/**
	 * Verifie si une chaine represente un entier valide.
	 * Utilise Integer.parseInt pour la verification.
	 * @param chaine Chaine a tester.
	 * @return True si la chaine est un entier valide.
	 */
	public boolean isInteger(String chaine) {
		try {
			int nombre = Integer.parseInt(chaine);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}

	/**
	 * Convertit une chaine en entier avec gestion des erreurs.
	 * @param chaine Chaine a convertir.
	 * @return Valeur entiere ou -1 en cas d'erreur.
	 */
	public int convertStrInt(String chaine) {
		if (chaine == null) {
			aff("n0 est null");
			return -1;
		}
		if (chaine.isEmpty()) {
			aff("n0 est egal a " + '"' + '"' + ".");
			return -1;
		}
		try {
			return Integer.parseInt(chaine);
		} catch (NumberFormatException exception) {
			aff("Impossible de convertir ");
			return -1;
		}
	}

	/**
	 * Verifie si un caractere est un chiffre (0-9).
	 * @param caractere Caractere a tester.
	 * @return True si le caractere est un chiffre.
	 */
	public boolean isCharNumber(char caractere) {
		return (caractere >= '0' && caractere <= '9');
	}

	/**
	 * Verifie si une chaine contient uniquement des chiffres.
	 * @param chaine Chaine a tester.
	 * @return True si tous les caracteres sont des chiffres.
	 */
	public boolean isInteger_deep(String chaine) {
		if (chaine.isEmpty()) {
			return false;
		}
		int longueurChaine = chaine.length();
		for (int index = 0; index < longueurChaine; index++) {
			if (!isCharNumber(chaine.charAt(index))) {
				return false;
			}
		}
		return true;
	}

// ------------------------------------------------------------------------------
// Verification de nombres CalculsDouble
// ------------------------------------------------------------------------------

	/**
	 * Verifie si un CalculsDouble est un nombre valide.
	 * @param nombre CalculsDouble a tester.
	 * @return True si partieEntiere et partieDecimale sont des nombres valides.
	 */
	public boolean isInteger(CalculsDouble nombre) {
		boolean resultat = false;
		String partieEntiere = nombre.partieEntiere;
		String partieDecimale = nombre.partieDecimale;
		if (!partieEntiere.isEmpty()) {
			resultat = isInteger(partieEntiere);
		}
		if (!partieDecimale.isEmpty()) {
			resultat = isInteger(partieDecimale);
		}
		return resultat;
	}

	/**
	 * Verifie si un CalculsDouble est egal a zero.
	 * @param nombre CalculsDouble a tester.
	 * @return True si le nombre vaut 0 ou 0.0.
	 */
	public boolean estNul(CalculsDouble nombre) {
		if (!isInteger(nombre)) {
			return false;
		}
		return partieEstNulle(nombre.partieEntiere) && partieEstNulle(nombre.partieDecimale);
	}

	/**
	 * Verifie si une chaine ne contient que des zeros.
	 * @param partie Chaine a verifier.
	 * @return True si vide ou uniquement des zeros.
	 */
	private boolean partieEstNulle(String partie) {
		if (partie.isEmpty()) {
			return true;
		}
		for (int index = 0; index < partie.length(); index++) {
			if (partie.charAt(index) != '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Convertit le CalculsDouble en valeur double.
	 * @return Valeur numerique double.
	 */
	public double toDouble() {
		String valeur = partieEntiere;
		if (partieDecimale != null && !partieDecimale.isEmpty()) {
			valeur += "." + partieDecimale;
		}
		double resultat = Double.parseDouble(valeur);
		if (estNegatif) {
			resultat = -resultat;
		}
		return resultat;
	}

	/**
	 * Compte les occurrences d'un caractere dans une chaine.
	 * @param chaine Chaine a analyser.
	 * @param caractere Caractere a compter.
	 * @return Nombre d'occurrences.
	 */
	public int compteOcurrences(String chaine, char caractere) {
		if (chaine == null || chaine.isEmpty()) {
			return 0;
		}
		int resultat = 0;
		int longueurChaine = chaine.length();
		for (int index = 0; index < longueurChaine; index++) {
			if (chaine.charAt(index) == caractere) {
				resultat++;
			}
		}
		return resultat;
	}

// ------------------------------------------------------------------------------
// Arithmetique sur chaines (grands nombres)
// ------------------------------------------------------------------------------

	/**
	 * Convertit un caractere chiffre en valeur entiere.
	 * @param chiffre Caractere '0' a '9'.
	 * @return Valeur 0 a 9.
	 */
	public int chiffreVersInt(char chiffre) {
		return chiffre - '0';
	}

	/**
	 * Convertit une valeur 0-9 en caractere.
	 * @param valeur Valeur 0 a 9.
	 * @return Caractere '0' a '9'.
	 */
	public char intVersChiffre(int valeur) {
		return (char) ('0' + valeur);
	}

	/**
	 * Complete une chaine avec des zeros a gauche.
	 * @param chaine Chaine a completer.
	 * @param longueur Longueur souhaitee.
	 * @return Chaine completee.
	 */
	public String padGauche(String chaine, int longueur) {
		String resultat = chaine;
		while (resultat.length() < longueur) {
			resultat = "0" + resultat;
		}
		return resultat;
	}

	/**
	 * Additionne deux chaines numeriques positives.
	 * @param chaineA Premiere chaine.
	 * @param chaineB Deuxieme chaine.
	 * @return Somme sous forme de chaine.
	 */
	public String additionneChaines(String chaineA, String chaineB) {
		int longueurMax = Math.max(chaineA.length(), chaineB.length());
		String paddedA = padGauche(chaineA, longueurMax);
		String paddedB = padGauche(chaineB, longueurMax);
		return calculerSommeChaines(paddedA, paddedB, longueurMax);
	}

	/**
	 * Calcule la somme de deux chaines de meme longueur.
	 * @param chaineA Premiere chaine paddee.
	 * @param chaineB Deuxieme chaine paddee.
	 * @param longueur Longueur des chaines.
	 * @return Somme sous forme de chaine.
	 */
	private String calculerSommeChaines(String chaineA, String chaineB, int longueur) {
		String resultat = "";
		int retenue = 0;
		for (int index = longueur - 1; index >= 0; index--) {
			int somme = chiffreVersInt(chaineA.charAt(index)) + chiffreVersInt(chaineB.charAt(index)) + retenue;
			resultat = intVersChiffre(somme % 10) + resultat;
			retenue = somme / 10;
		}
		if (retenue > 0) {
			resultat = intVersChiffre(retenue) + resultat;
		}
		return resultat;
	}

	/**
	 * Compare deux chaines numeriques.
	 * @param chaineA Premiere chaine.
	 * @param chaineB Deuxieme chaine.
	 * @return 1 si {@code A > B}, -1 si {@code A < B}, 0 si egal.
	 */
	public int comparerChaines(String chaineA, String chaineB) {
		String paddedA = padGauche(chaineA, Math.max(chaineA.length(), chaineB.length()));
		String paddedB = padGauche(chaineB, Math.max(chaineA.length(), chaineB.length()));
		return paddedA.compareTo(paddedB);
	}

	/**
	 * Soustrait deux chaines numeriques (gere {@code A < B}).
	 * @param chaineA Premiere chaine.
	 * @param chaineB Deuxieme chaine.
	 * @return Difference sous forme de chaine (avec - si negatif).
	 */
	public String soustraitChaines(String chaineA, String chaineB) {
		int longueurMax = Math.max(chaineA.length(), chaineB.length());
		String paddedA = padGauche(chaineA, longueurMax);
		String paddedB = padGauche(chaineB, longueurMax);
		if (comparerChaines(chaineA, chaineB) < 0) {
			return "-" + calculerDifferenceChaines(paddedB, paddedA, longueurMax);
		}
		return calculerDifferenceChaines(paddedA, paddedB, longueurMax);
	}

	/**
	 * Calcule la difference de deux chaines de meme longueur.
	 * @param chaineA Premiere chaine paddee.
	 * @param chaineB Deuxieme chaine paddee.
	 * @param longueur Longueur des chaines.
	 * @return Difference sous forme de chaine.
	 */
	private String calculerDifferenceChaines(String chaineA, String chaineB, int longueur) {
		String resultat = "";
		int emprunt = 0;
		for (int index = longueur - 1; index >= 0; index--) {
			int diff = chiffreVersInt(chaineA.charAt(index)) - chiffreVersInt(chaineB.charAt(index)) - emprunt;
			if (diff < 0) {
				diff += 10;
				emprunt = 1;
			} else {
				emprunt = 0;
			}
			resultat = intVersChiffre(diff) + resultat;
		}
		return supprimerZerosGauche(resultat);
	}

	/**
	 * Supprime les zeros inutiles a gauche.
	 * @param chaine Chaine a nettoyer.
	 * @return Chaine sans zeros a gauche (au moins "0").
	 */
	public String supprimerZerosGauche(String chaine) {
		int index = 0;
		while (index < chaine.length() - 1 && chaine.charAt(index) == '0') {
			index++;
		}
		return chaine.substring(index);
	}

	/**
	 * Incremente une chaine numerique de 1.
	 * @param chaine Chaine a incrementer.
	 * @return Chaine incrementee.
	 */
	public String incrementeChaine(String chaine) {
		return additionneChaines(chaine, "1");
	}


	/**
	 * Affiche un objet sans retour a la ligne.
	 * @param objet Objet a afficher.
	 */
	public static void affnn(Object objet) {
		System.out.print("" + objet);
	}

	/**
	 * Affiche un objet avec retour a la ligne.
	 * @param objet Objet a afficher.
	 */
	public static void aff(Object objet) {
		System.out.println("" + objet);
	}

	/**
	 * Affiche un tableau d'entiers avec nom par defaut.
	 * @param tableau Tableau a afficher.
	 */
	public void affTab(int[] tableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff("t[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Affiche un tableau d'entiers avec nom personnalise.
	 * @param tableau Tableau a afficher.
	 * @param nomTableau Nom a utiliser pour l'affichage.
	 */
	public void affTab(int[] tableau, String nomTableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff(nomTableau + "[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Affiche un tableau de chaines avec nom personnalise.
	 * @param tableau Tableau de chaines a afficher.
	 * @param nomTableau Nom a utiliser pour l'affichage.
	 */
	public void afftab2dim(String[] tableau, String nomTableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff(nomTableau + "[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Agrandit un tableau d'entiers (double la taille + 1).
	 * @param tableauOriginal Tableau a agrandir.
	 * @return Nouveau tableau agrandi.
	 */
	public int[] agrandieTab(int[] tableauOriginal) {
		int[] nouveauTableau = new int[tableauOriginal.length * 2 + 1];
		for (int index = 0; index < tableauOriginal.length; index++) {
			nouveauTableau[index] = tableauOriginal[index];
		}
		return nouveauTableau;
	}


// ==============================================================================
// Affichage
// ==============================================================================

	/**
	 * Convertit le CalculsDouble en chaine de caracteres.
	 * Gere le signe, la virgule/point et les chiffres significatifs.
	 * @return Representation textuelle du nombre.
	 */
	public String toString() {
		String signeMoins = "";
		String affichageEntier = partieEntiere;
		String affichageDecimal = chiffresSignificatifs(this.partieDecimale, -1);
		char virgule = ',';
		if (representation_anglaise) {
			virgule = '.';
		}
		String chaineDecimale = virgule + affichageDecimal;
		if (affichageEntier.isEmpty()) {
			affichageEntier = "0";
		}
		if (partieDecimaleEstVide(affichageDecimal)) {
			chaineDecimale = "";
		}
		if (estNegatif) {
			signeMoins = "-";
		}
		return (signeMoins + affichageEntier + chaineDecimale);
	}

	/**
	 * Verifie si la partie decimale est vide ou nulle.
	 * @param partieDecimale Chaine de la partie decimale.
	 * @return True si vide ou egale a zero.
	 */
	private boolean partieDecimaleEstVide(String partieDecimale) {
		if (partieDecimale.isEmpty()) {
			return true;
		}
		return Integer.parseInt(partieDecimale) == 0;
	}


// ==============================================================================
// Reconnaissance d'expressions
// ==============================================================================


// ==============================================================================
// Donnees
// ==============================================================================

	Character[] listeOperateurs = {'+', '-', '*', '/'};
	Stack<String> pileCalcul = new Stack<String>();
	ArrayList<Character> operateursAutorises = new ArrayList<Character>();

// ==============================================================================
// Fonctions utilitaires
// ==============================================================================

	/**
	 * Initialise la liste des operateurs autorises.
	 * Copie les operateurs du tableau vers l'ArrayList.
	 */
	public void operateurs() {
		for (Character caractere : listeOperateurs) {
			operateursAutorises.add(caractere);
		}
	}

// ------------------------------------------------------------------------------
// Gestion de la pile
// ------------------------------------------------------------------------------

	/**
	 * Depile un element avec gestion d'erreur.
	 * @param pile Pile a depiler.
	 * @return Element depile ou chaine vide si pile vide.
	 */
	public String popAvecErreur(Stack<String> pile) {
		if (!pile.empty()) {
			return pile.pop();
		} else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	/**
	 * Effectue un calcul avec les 3 elements en haut de pile.
	 * Depile operande, operateur, operande puis empile le resultat.
	 * @param pile Pile de calcul.
	 */
	public void calculPile(Stack<String> pile) {
		String operandeA = popAvecErreur(pile);
		String symbole = popAvecErreur(pile);
		String operandeB = popAvecErreur(pile);
		int resultatTemporaire = traiteSymbolesInt(operandeB, operandeA, symbole);
		aff("resultatTemporaire = " + resultatTemporaire);
		pile.push("" + resultatTemporaire);
	}

// ------------------------------------------------------------------------------
// Verification des nombres doubles
// ------------------------------------------------------------------------------

	/**
	 * Verifie et nettoie un nombre double avec separateurs.
	 * @param nombre Nombre a verifier.
	 * @param separateur Separateur decimal utilise.
	 * @return Nombre nettoye ou chaine vide si invalide.
	 */
	public String verifieDoubleCorrecte(String nombre, char separateur) {
		if (nombre == null || nombre.isEmpty()) {
			aff("Le nombre est null ou vide.");
			return "";
		}
		char separateurAlternatif = (separateur == '.') ? ',' : '.';
		String[] partiesNombre = nombre.split("" + separateur);
		if (partiesNombre == null) {
			return "";
		}
		if (partiesNombre.length == 1) {
			return verifierPartieUnique(partiesNombre[0], separateur);
		}
		if (partiesNombre.length < 2) {
			return "";
		}
		return verifierPartieDecimale(partiesNombre, separateurAlternatif);
	}

	/**
	 * Verifie une partie unique du nombre.
	 * @param partie Partie a verifier.
	 * @param separateur Separateur a retirer.
	 * @return Partie nettoyee ou chaine vide si invalide.
	 */
	private String verifierPartieUnique(String partie, char separateur) {
		if (partie == null || partie.isEmpty() || partie.equals(",")) {
			return "";
		}
		return partie.replace("" + separateur, "");
	}

// ------------------------------------------------------------------------------
// Verification partie decimale
// ------------------------------------------------------------------------------

	/**
	 * Verifie la partie decimale avec separateurs de milliers.
	 * @param partiesNombre Tableau des parties du nombre.
	 * @param separateurMilliers Separateur de milliers attendu.
	 * @return Nombre nettoye ou chaine vide si invalide.
	 */
	private String verifierPartieDecimale(String[] partiesNombre, char separateurMilliers) {
		String partieDecimale = partiesNombre[1];
		int longueurDecimale = partieDecimale.length();
		for (int index = 0; index < longueurDecimale; index++) {
			if (index % 3 == 0) {
				if (partieDecimale.charAt(index) != separateurMilliers) {
					return "";
				}
			} else {
				if (!isCharNumber(partieDecimale.charAt(index))) {
					return "";
				}
			}
		}
		return partiesNombre[0] + partieDecimale.replace("" + separateurMilliers, "");
	}

// ------------------------------------------------------------------------------
// Analyse des nombres
// ------------------------------------------------------------------------------

	/**
	 * Analyse et convertit un nombre double depuis une chaine.
	 * Gere les formats avec virgule ou point decimal.
	 * @param nombre Chaine representant le nombre.
	 * @return Valeur double ou -1 en cas d'erreur.
	 */
	public double analyseNombreDouble(String nombre) {
		int nombreVirgules = compteOcurrences(nombre, ',');
		int nombrePoints = compteOcurrences(nombre, '.');
		aff("nombreVirgules = " + nombreVirgules);
		aff("nombrePoints = " + nombrePoints);
		if (nombreVirgules == 0 && nombrePoints == 0) {
			if (isInteger(nombre)) {
				return Integer.parseInt(nombre);
			}
		} else {
			nombre = normaliserSeparateurs(nombre, nombreVirgules, nombrePoints);
			nombreVirgules = compteOcurrences(nombre, ',');
			nombrePoints = compteOcurrences(nombre, '.');
			aff("nombreVirgules = " + nombreVirgules);
			aff("nombrePoints = " + nombrePoints);
			aff("nombre = " + nombre);
			return convertirEnDouble(nombre, nombreVirgules, nombrePoints);
		}
		return -1;
	}

	/**
	 * Normalise les separateurs du nombre.
	 * @param nombre Nombre a normaliser.
	 * @param nombreVirgules Compte des virgules.
	 * @param nombrePoints Compte des points.
	 * @return Nombre normalise.
	 */
	private String normaliserSeparateurs(String nombre, int nombreVirgules, int nombrePoints) {
		if (nombreVirgules == 1 && nombrePoints > 1) {
			return verifieDoubleCorrecte(nombre, ',');
		}
		if (nombreVirgules > 1 && nombrePoints == 1) {
			return verifieDoubleCorrecte(nombre, '.');
		}
		return nombre;
	}

// ------------------------------------------------------------------------------
// Conversion en double
// ------------------------------------------------------------------------------

	/**
	 * Convertit la chaine normalisee en double.
	 * @param nombre Nombre normalise.
	 * @param nombreVirgules Compte des virgules.
	 * @param nombrePoints Compte des points.
	 * @return Valeur double ou -1 si invalide.
	 */
	private double convertirEnDouble(String nombre, int nombreVirgules, int nombrePoints) {
		if (nombreVirgules > 1 && nombrePoints > 1) {
			return -1;
		}
		if (nombreVirgules == 1 && nombrePoints == 0) {
			return Double.parseDouble(nombre.replace(',', '.'));
		}
		if (nombreVirgules == 0 && nombrePoints == 1) {
			return Double.parseDouble(nombre);
		}
		return -1;
	}

// ==============================================================================
// Fonctions principales
// ==============================================================================

// ------------------------------------------------------------------------------
// Extraction des symboles
// ------------------------------------------------------------------------------

	/**
	 * Extrait un nombre depuis une expression a partir d'un indice.
	 * Gere les virgules et points decimaux.
	 * @param expression Expression source.
	 * @param indiceDebut Indice de debut d'extraction.
	 * @return Nombre extrait sous forme de chaine.
	 */
	public String mangeNombre(String expression, int indiceDebut) {
		int longueurExpression = expression.length();
		boolean contientVirgules = false;
		boolean contientPoints = false;
		boolean estChiffre = true;
		String resultat = "";
		for (int index = indiceDebut; index < longueurExpression && estChiffre; index++) {
			char caractereActuel = expression.charAt(index);
			if (isCharNumber(caractereActuel)) {
				resultat += caractereActuel;
			} else if (caractereActuel == ',') {
				contientVirgules = true;
				resultat += caractereActuel;
			} else if (caractereActuel == '.') {
				contientPoints = true;
				resultat += caractereActuel;
			} else {
				estChiffre = false;
			}
		}
		aff(resultat);
		if (contientVirgules || contientPoints) {
			return "" + analyseNombreDouble(resultat);
		}
		return resultat;
	}

	/**
	 * Extrait un operateur depuis une expression.
	 * @param expression Expression source.
	 * @param indice Position de l'operateur.
	 * @return Caractere operateur ou -1 si hors limites.
	 */
	public char mangeOperateur(String expression, int indice) {
		int longueurExpression = expression.length();
		if (indice >= 0 && indice < longueurExpression) {
			aff(expression.charAt(indice));
			return expression.charAt(indice);
		}
		return (char) (-1);
	}

// ------------------------------------------------------------------------------
// Traitement des symboles
// ------------------------------------------------------------------------------

	/**
	 * Effectue une operation arithmetique entre deux operandes.
	 * @param chaineA Premier operande (chaine).
	 * @param chaineB Second operande (chaine).
	 * @param chaineSymbole Operateur (chaine).
	 * @return Resultat de l'operation ou -1 en cas d'erreur.
	 */
	public int traiteSymbolesInt(String chaineA, String chaineB, String chaineSymbole) {
		if (chaineSymbole == null || chaineSymbole.isEmpty()) {
			aff("Symbole null ou egal a " + '"' + '"' + ".");
			return -1;
		}
		if (!isInteger(chaineA) || !isInteger(chaineB)) {
			aff("chaineA (= " + chaineA + ") ou chaineB (= " + chaineB + ") ne sont pas des nombres.");
			return -1;
		}
		int operandeA = Integer.parseInt(chaineA);
		int operandeB = Integer.parseInt(chaineB);
		char symbole = chaineSymbole.charAt(0);
		if (!operateursAutorises.contains(symbole)) {
			aff("symbole (= " + symbole + ") n'est pas un operateur.");
			return -1;
		}
		return executerOperation(operandeA, operandeB, symbole);
	}

	/**
	 * Execute l'operation arithmetique.
	 * @param operandeA Premier operande.
	 * @param operandeB Second operande.
	 * @param symbole Operateur.
	 * @return Resultat de l'operation.
	 */
	private int executerOperation(int operandeA, int operandeB, char symbole) {
		if (symbole == '+') {
			return operandeA + operandeB;
		}
		if (symbole == '-') {
			return operandeA - operandeB;
		}
		if (symbole == '*') {
			return operandeA * operandeB;
		}
		if (symbole == '/') {
			return operandeA / operandeB;
		}
		return 0;
	}

// ------------------------------------------------------------------------------
// Analyse d'expression
// ------------------------------------------------------------------------------

	/**
	 * Analyse et evalue une expression mathematique sans parentheses.
	 * Utilise une pile pour gerer les operations.
	 * @param expression Expression a analyser.
	 * @return Resultat de l'expression ou -1 en cas d'erreur.
	 */
	public int analyseExpressionSansParentheses(String expression) {
		if (expression == null) {
			aff("L'expression est null");
			return -1;
		}
		if (expression.isEmpty()) {
			aff("L'expression est vide.");
			return -1;
		}
		int longueurExpression = expression.length();
		int compteur = 0;
		String elementTemporaire = "";
		for (int index = 0; index < longueurExpression; index++) {
			if (isCharNumber(expression.charAt(index))) {
				elementTemporaire = mangeNombre(expression, index);
				pileCalcul.push(elementTemporaire);
				while (index < longueurExpression && isCharNumber(expression.charAt(index))) {
					index++;
				}
			}
			if (compteur > 0) {
				calculPile(pileCalcul);
			}
			if (index >= longueurExpression) {
				break;
			}
			if (operateursAutorises.contains(expression.charAt(index))) {
				elementTemporaire = "" + mangeOperateur(expression, index);
				pileCalcul.push(elementTemporaire);
			}
			compteur++;
		}
		String resultat = popAvecErreur(pileCalcul);
		return convertStrInt(resultat);
	}
	
// ==============================================================================
// Tests
// ==============================================================================


// ==============================================================================
// Variables de tests
// ==============================================================================

	static int testsPassed = 0;
	static int testsFailed = 0;

// ==============================================================================
// Fonction d'assertion
// ==============================================================================

	/**
	 * Verifie une condition et affiche le resultat.
	 * @param description Description du test.
	 * @param condition Condition a verifier.
	 */
	static void assertion(String description, boolean condition) {
		if (condition) {
			System.out.println("[PASS] " + description);
			testsPassed++;
		} else {
			System.out.println("[FAIL] " + description);
			testsFailed++;
		}
	}

// ==============================================================================
// Tests
// ==============================================================================

	/**
	 * Execute tous les tests.
	 */
	static void runTests() {
		CalculsDouble calc = new CalculsDouble();
		System.out.println("=== Tests CalculsDouble ===\n");
		testsConstructeur();
		testsAddition(calc);
		testsSoustraction(calc);
		testsMultiplication(calc);
		testsDivision(calc);
		testsValeurAbsolue(calc);
		testsEdgeCases(calc);
		afficherResume();
	}

// ------------------------------------------------------------------------------
// Tests unitaires par categorie
// ------------------------------------------------------------------------------

	/**
	 * Tests du constructeur CalculsDouble.
	 */
	static void testsConstructeur() {
		assertion("CalculsDouble positif", new CalculsDouble(42).toDouble() == 42.0);
		assertion("CalculsDouble negatif", new CalculsDouble(-42).toDouble() == -42.0);
		assertion("CalculsDouble decimal", Math.abs(new CalculsDouble(3.14).toDouble() - 3.14) < 0.001);
		assertion("CalculsDouble zero", new CalculsDouble(0).toDouble() == 0.0);
	}

	/**
	 * Tests de l'addition.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsAddition(CalculsDouble calc) {
		assertion("Addition simples",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(3)).toDouble() == 8.0);
		assertion("Addition negatifs",
			calc.additionneNombres(new CalculsDouble(-5), new CalculsDouble(-3)).toDouble() == -8.0);
		assertion("Addition mixte",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(-3)).toDouble() == 2.0);
	}

	/**
	 * Tests de la soustraction.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsSoustraction(CalculsDouble calc) {
		assertion("Soustraction simple",
			calc.soustraitNombres(new CalculsDouble(10), new CalculsDouble(3)).toDouble() == 7.0);
		assertion("Soustraction resultat negatif",
			calc.soustraitNombres(new CalculsDouble(3), new CalculsDouble(10)).toDouble() == -7.0);
	}

	/**
	 * Tests de la multiplication.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsMultiplication(CalculsDouble calc) {
		assertion("Multiplication simple",
			calc.mult(new CalculsDouble(6), new CalculsDouble(7)).toDouble() == 42.0);
		assertion("Multiplication par zero",
			calc.mult(new CalculsDouble(100), new CalculsDouble(0)).toDouble() == 0.0);
		assertion("Multiplication negatifs",
			calc.mult(new CalculsDouble(-3), new CalculsDouble(-4)).toDouble() == 12.0);
	}

	/**
	 * Tests de la division.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsDivision(CalculsDouble calc) {
		assertion("Division simple",
			calc.division_boucle(new CalculsDouble(20), new CalculsDouble(4))[0].toDouble() == 5.0);
		assertion("Division decimale",
			Math.abs(calc.division_boucle(new CalculsDouble(10), new CalculsDouble(4))[0].toDouble() - 2.5) < 0.001);
		CalculsDouble[] resultatDivZero = calc.division_boucle(new CalculsDouble(10), new CalculsDouble(0));
		assertion("Division par zero retourne null", resultatDivZero == null);
	}

	/**
	 * Tests de la valeur absolue.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsValeurAbsolue(CalculsDouble calc) {
		assertion("Valeur absolue positif",
			calc.abs(new CalculsDouble(5)).toDouble() == 5.0);
		assertion("Valeur absolue negatif",
			calc.abs(new CalculsDouble(-5)).toDouble() == 5.0);
	}

// ------------------------------------------------------------------------------
// Tests Edge Cases (bugs connus)
// ------------------------------------------------------------------------------

	/**
	 * Tests des cas limites (bugs connus).
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsEdgeCases(CalculsDouble calc) {
		System.out.println("\n=== Edge Cases (Bugs a corriger) ===\n");
		testsBug1Addition(calc);
		testsBug3GrandsNombres(calc);
	}

	/**
	 * Tests Bug1 : Addition decimal + entier.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsBug1Addition(CalculsDouble calc) {
		assertion("Bug1: Addition decimal + entier (12.5 + 3 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(3)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition entier + decimal (3 + 12.5 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(3), new CalculsDouble(12.5)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition decimal + entier negatif (12.5 + (-3) = 9.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(-3)).toDouble() - 9.5) < 0.001);
	}

	/**
	 * Tests Bug3 : Grands nombres (> 10 chiffres).
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsBug3GrandsNombres(CalculsDouble calc) {
		assertion("Bug3: Grand nombre entier (12345678901)", testerGrandNombre());
		assertion("Bug3: Addition grands nombres", testerAdditionGrandsNombres(calc));
		assertion("Bug3: Multiplication grands nombres", testerMultiplicationGrandsNombres(calc));
	}

// ------------------------------------------------------------------------------
// Fonctions auxiliaires pour tests Bug3
// ------------------------------------------------------------------------------

	/**
	 * Teste la creation d'un grand nombre.
	 * @return True si le test passe.
	 */
	static boolean testerGrandNombre() {
		try {
			CalculsDouble grand = new CalculsDouble(12345678901.0);
			return grand.partieEntiere.equals("12345678901");
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * Teste l'addition de grands nombres.
	 * @param calc Instance de CalculsDouble.
	 * @return True si le test passe.
	 */
	static boolean testerAdditionGrandsNombres(CalculsDouble calc) {
		try {
			CalculsDouble resultatAdd = calc.additionneNombres(new CalculsDouble(9999999999.0), new CalculsDouble(1));
			return resultatAdd.partieEntiere.equals("10000000000");
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * Teste la multiplication de grands nombres.
	 * @param calc Instance de CalculsDouble.
	 * @return True si le test passe.
	 */
	static boolean testerMultiplicationGrandsNombres(CalculsDouble calc) {
		try {
			double resultat = calc.mult(new CalculsDouble(1000000), new CalculsDouble(1000000)).toDouble();
			return (resultat == 1000000000000.0);
		} catch (Exception exception) {
			return false;
		}
	}

// ------------------------------------------------------------------------------
// Resume
// ------------------------------------------------------------------------------

	/**
	 * Affiche le resume des tests.
	 */
	static void afficherResume() {
		System.out.println("\n=== Resume ===");
		System.out.println("Tests passes: " + testsPassed);
		System.out.println("Tests echoues: " + testsFailed);
		System.out.println("Total: " + (testsPassed + testsFailed));
	}

// ==============================================================================
// Main
// ==============================================================================

	/**
	 * Point d'entree du programme.
	 * @param args Arguments de la ligne de commande.
	 */
	public static void main(String[] args) {
		runTests();
	}


}
