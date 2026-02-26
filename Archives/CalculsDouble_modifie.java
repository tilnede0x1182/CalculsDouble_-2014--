import java.math.BigInteger;
import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;

class CalculsDouble {
	boolean estNegatif=false;
	boolean representation_anglaise=false;

	String partieEntiereStr;
	String partieDecimaleStr;

// ==============================================================================
// Constructeurs
// ==============================================================================

	/**
	 * Constructeur par defaut.
	 * Initialise le nombre a 0.
	 */
	public CalculsDouble() {
		partieEntiereStr="0";
		partieDecimaleStr="";
	}

	/**
	 * Constructeur avec un entier.
	 * @param n0 Valeur entiere initiale.
	 */
	public CalculsDouble(int n0) {
		partieEntiereStr = ""+(int)(n0);
		partieDecimaleStr = "";

		if (partieEntiereStr.contains("-")) {
			estNegatif = true;
			partieEntiereStr = partieEntiereStr.replace("-", "");
		}
	}

	/**
	 * Constructeur avec un double.
	 * @param n0 Valeur decimale initiale.
	 */
	public CalculsDouble(double n0) {
		partieEntiereStr = ""+(int)(n0);
		partieDecimaleStr = partieDecimale(n0);

		if (partieEntiereStr.contains("-")) {
			estNegatif = true;
			partieEntiereStr = partieEntiereStr.replace("-", "");
		}
	}

	/**
	 * Constructeur avec entier et format de representation.
	 * @param n0 Valeur entiere initiale.
	 * @param repr_anglaise True pour format anglais (point decimal).
	 */
	public CalculsDouble(int n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		partieEntiereStr = ""+(int)(n0);
		partieDecimaleStr = "0";

		if (partieEntiereStr.contains("-")) {
			estNegatif = true;
			partieEntiereStr = partieEntiereStr.replace("-", "");
		}
	}

	/**
	 * Constructeur avec double et format de representation.
	 * @param n0 Valeur decimale initiale.
	 * @param repr_anglaise True pour format anglais (point decimal).
	 */
	public CalculsDouble(double n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		partieEntiereStr = ""+(int)(n0);
		partieDecimaleStr = partieDecimale(n0);

		if (partieEntiereStr.contains("-")) {
			estNegatif = true;
			partieEntiereStr = partieEntiereStr.replace("-", "");
		}
	}


// ==============================================================================
// Opérations
// ==============================================================================

	/**
	 * Complete une chaine avec des zeros a droite.
	 * @param value Chaine a completer.
	 * @param targetLength Longueur cible.
	 * @return Chaine completee.
	 */
	private static String padRight(String value, int targetLength) {
		StringBuilder sb = new StringBuilder(value);
		while (sb.length() < targetLength) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * Complete une chaine avec des zeros a gauche.
	 * @param value Chaine a completer.
	 * @param targetLength Longueur cible.
	 * @return Chaine completee.
	 */
	private static String padLeft(String value, int targetLength) {
		StringBuilder sb = new StringBuilder(value);
		while (sb.length() < targetLength) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}

	/**
	 * Convertit un CalculsDouble en BigInteger avec echelle.
	 * @param nombre Nombre a convertir.
	 * @param scale Nombre de decimales a integrer.
	 * @return BigInteger representant le nombre mis a echelle.
	 */
	private BigInteger toScaledBigInteger(CalculsDouble nombre, int scale) {
		String digits = nombre.partieEntiereStr + padRight(nombre.partieDecimaleStr, scale);
		if (digits.isEmpty()) {
			digits = "0";
		}
		BigInteger valeur = new BigInteger(digits);
		if (nombre.estNegatif && valeur.signum() != 0) {
			valeur = valeur.negate();
		}
		return valeur;
	}

	/**
	 * Extrait les parties entiere et decimale depuis les chiffres.
	 * @param resultat CalculsDouble a remplir.
	 * @param chiffres Chaine de chiffres.
	 * @param echelle Nombre de decimales.
	 */
	private void extrairePartiesDepuisChiffres(CalculsDouble resultat, String chiffres, int echelle) {
		String chiffresComplets = chiffres;
		if (chiffresComplets.length() <= echelle) {
			chiffresComplets = padLeft(chiffresComplets, echelle + 1);
		}
		int pointCoupure = chiffresComplets.length() - echelle;
		resultat.partieEntiereStr = chiffresComplets.substring(0, pointCoupure);
		resultat.partieDecimaleStr = chiffresComplets.substring(pointCoupure).replaceFirst("0+$", "");
	}

	/**
	 * Reconstitue un CalculsDouble depuis un BigInteger mis a echelle.
	 * @param valeur BigInteger a reconvertir.
	 * @param echelle Nombre de decimales encodees.
	 * @return CalculsDouble reconstitue.
	 */
	private CalculsDouble fromScaledValue(BigInteger valeur, int echelle) {
		CalculsDouble resultat = new CalculsDouble();
		resultat.estNegatif = valeur.signum() < 0;
		String chiffres = valeur.abs().toString();

		if (echelle == 0) {
			resultat.partieEntiereStr = chiffres;
			resultat.partieDecimaleStr = "";
		} else {
			extrairePartiesDepuisChiffres(resultat, chiffres, echelle);
		}

		if (resultat.partieEntiereStr.isEmpty()) resultat.partieEntiereStr = "0";
		if (resultat.partieDecimaleStr == null) resultat.partieDecimaleStr = "";
		if (resultat.partieEntiereStr.equals("0") && resultat.partieDecimaleStr.isEmpty()) {
			resultat.estNegatif = false;
		}
		return resultat;
	}

	/**
	 * Additionne deux CalculsDouble.
	 * @param a Premier nombre.
	 * @param b Second nombre.
	 * @return Somme des deux nombres.
	 */
	public CalculsDouble additionneNombres(CalculsDouble a, CalculsDouble b) {
		int scale = Math.max(a.partieDecimaleStr.length(), b.partieDecimaleStr.length());
		BigInteger somme = toScaledBigInteger(a, scale).add(toScaledBigInteger(b, scale));
		return fromScaledValue(somme, scale);
	}

	/**
	 * Calcule le signe du resultat d'une multiplication.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return true si le resultat est negatif.
	 */
	private boolean calculerSigneMultiplication(CalculsDouble nombreA, CalculsDouble nombreB) {
		boolean unSeulNegatif = nombreA.estNegatif || nombreB.estNegatif;
		boolean lesDeuxNegatifs = nombreA.estNegatif && nombreB.estNegatif;
		return unSeulNegatif && !lesDeuxNegatifs;
	}

	/**
	 * Compte le nombre total de decimales des deux nombres.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Nombre total de decimales.
	 */
	private int compterDecimalesTotal(CalculsDouble nombreA, CalculsDouble nombreB) {
		int totalDecimales = 0;
		if (!nombreA.partieDecimaleStr.isEmpty()) totalDecimales += nombreA.partieDecimaleStr.length();
		if (!nombreB.partieDecimaleStr.isEmpty()) totalDecimales += nombreB.partieDecimaleStr.length();
		return totalDecimales;
	}

	/**
	 * Calcule la longueur totale d'un nombre (entier + decimales).
	 * @param nombre Le nombre a mesurer.
	 * @return Longueur totale.
	 */
	private int longueurTotaleNombre(CalculsDouble nombre) {
		return nombre.partieEntiereStr.length() + nombre.partieDecimaleStr.length();
	}

	/**
	 * Effectue la multiplication chiffre par chiffre et remplit la matrice.
	 * @param chaineNombreA Chaine du premier nombre.
	 * @param chaineNombreB Chaine du second nombre.
	 * @return Matrice des resultats partiels.
	 */
	private String[] remplirMatriceMultiplication(String chaineNombreA, String chaineNombreB) {
		int longueurA = chaineNombreA.length();
		int longueurB = chaineNombreB.length();
		String[] matriceResultats = new String[longueurB];
		for (int index = 0; index < longueurB; index++) {
			matriceResultats[index] = "";
		}

		int retenue = 0;
		for (int indexB = longueurB - 1; indexB >= 0; indexB--) {
			retenue = 0;
			for (int indexA = longueurA - 1; indexA >= 0; indexA--) {
				int chiffreA = Character.getNumericValue(chaineNombreA.charAt(indexA));
				int chiffreB = Character.getNumericValue(chaineNombreB.charAt(indexB));
				int produitPartiel = retenue + (chiffreA * chiffreB);

				String produitChaine = "" + produitPartiel;
				if (produitChaine.length() > 1 && indexA > 0) {
					retenue = Integer.parseInt(produitChaine.substring(0, produitChaine.length() - 1));
					produitChaine = produitChaine.substring(produitChaine.length() - 1);
				} else {
					retenue = 0;
				}

				int positionMatrice = longueurB - indexB - 1;
				matriceResultats[positionMatrice] = produitChaine + matriceResultats[positionMatrice];
			}
			ajouterZerosFinaux(matriceResultats, longueurB, indexB);
		}
		return matriceResultats;
	}

	/**
	 * Ajoute les zeros de decalage en fin de ligne de la matrice.
	 * @param matrice Matrice a modifier.
	 * @param longueurB Longueur du nombre B.
	 * @param indexB Index courant dans B.
	 */
	private void ajouterZerosFinaux(String[] matrice, int longueurB, int indexB) {
		int positionMatrice = longueurB - indexB - 1;
		for (int indexZero = 0; indexZero < positionMatrice; indexZero++) {
			matrice[positionMatrice] += "0";
		}
	}

	/**
	 * Additionne tous les resultats partiels de la matrice.
	 * @param matriceResultats Matrice des resultats partiels.
	 * @return Somme sous forme de CalculsDouble.
	 */
	private CalculsDouble additionnerResultatsPartiels(String[] matriceResultats) {
		CalculsDouble somme = new CalculsDouble(0);
		CalculsDouble valeurPartielle = new CalculsDouble(0);
		for (int index = 0; index < matriceResultats.length; index++) {
			valeurPartielle.partieEntiereStr = matriceResultats[index];
			somme.partieEntiereStr = additionneNombres(valeurPartielle, somme).partieEntiereStr;
		}
		return somme;
	}

	/**
	 * Ajuste le resultat en separant partie entiere et decimale.
	 * @param resultat Resultat a ajuster.
	 * @param nombreDecimales Nombre de decimales attendues.
	 */
	private void ajusterPartieDecimale(CalculsDouble resultat, int nombreDecimales) {
		if (nombreDecimales > 0 && resultat.partieEntiereStr.length() > nombreDecimales) {
			int positionVirgule = resultat.partieEntiereStr.length() - nombreDecimales;
			resultat.partieDecimaleStr = resultat.partieEntiereStr.substring(positionVirgule);
			resultat.partieEntiereStr = resultat.partieEntiereStr.substring(0, positionVirgule);
		}
	}

	/**
	 * Multiplie deux CalculsDouble.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Resultat de la multiplication.
	 */
	public CalculsDouble mult(CalculsDouble nombreA, CalculsDouble nombreB) {
		boolean resultatNegatif = calculerSigneMultiplication(nombreA, nombreB);
		int totalDecimales = compterDecimalesTotal(nombreA, nombreB);

		if (longueurTotaleNombre(nombreB) > longueurTotaleNombre(nombreA)) {
			CalculsDouble temporaire = nombreA;
			nombreA = nombreB;
			nombreB = temporaire;
		}

		String chaineNombreA = nombreA.partieEntiereStr + nombreA.partieDecimaleStr;
		String chaineNombreB = nombreB.partieEntiereStr + nombreB.partieDecimaleStr;

		String[] matriceResultats = remplirMatriceMultiplication(chaineNombreA, chaineNombreB);
		CalculsDouble resultat = additionnerResultatsPartiels(matriceResultats);

		if (!nombreA.partieDecimaleStr.isEmpty() || !nombreB.partieDecimaleStr.isEmpty()) {
			ajusterPartieDecimale(resultat, totalDecimales);
		}

		resultat.estNegatif = resultatNegatif;
		return resultat;
	}

	/**
	 * Soustrait deux CalculsDouble (a - b).
	 * @param a Nombre dont on soustrait.
	 * @param b Nombre a soustraire.
	 * @return Difference a - b.
	 */
	public CalculsDouble soustraitNombres(CalculsDouble a, CalculsDouble b) {
		int recul = 0;
		int chiffres_avant_la_virgule = Math.min(compteOcurrences(a.partieDecimaleStr, 
			'0'), compteOcurrences(b.partieDecimaleStr, '0'));
		CalculsDouble c = new CalculsDouble ();
		CalculsDouble oppose = copie(b);
		boolean estZero = oppose.partieEntiereStr.equals("0") && (oppose.partieDecimaleStr.isEmpty() || oppose.partieDecimaleStr.replace("0", "").isEmpty());
		if (!estZero) {
			oppose.estNegatif = !oppose.estNegatif;
		} else {
			oppose.estNegatif = false;
		}
		return additionneNombres(a, oppose);
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
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur,
			int chiffresSignificatifs) {
		return division_boucle(dividende, diviseur, true, chiffresSignificatifs);
	}

	/**
	 * Verifie si le diviseur est zero.
	 * @param diviseur Le diviseur a verifier.
	 * @return true si division par zero.
	 */
	private boolean estDivisionParZero(CalculsDouble diviseur) {
		return diviseur.equals(new CalculsDouble(0));
	}

	/**
	 * Verifie si le dividende est zero.
	 * @param dividende Le dividende a verifier.
	 * @return true si dividende est zero.
	 */
	private boolean estDividendeZero(CalculsDouble dividende) {
		return dividende.equals(new CalculsDouble(0));
	}

	/**
	 * Verifie si le diviseur est un.
	 * @param diviseur Le diviseur a verifier.
	 * @return true si diviseur vaut 1.
	 */
	private boolean estDiviseurUn(CalculsDouble diviseur) {
		return diviseur.equals(new CalculsDouble(1));
	}

	/**
	 * Verifie si dividende et diviseur sont egaux.
	 * @param dividende Le dividende.
	 * @param diviseur Le diviseur.
	 * @return true si egaux.
	 */
	private boolean sontEgaux(CalculsDouble dividende, CalculsDouble diviseur) {
		return dividende.equals(diviseur);
	}

	/**
	 * Cree le resultat pour dividende zero.
	 * @return Tableau [0, 0].
	 */
	private CalculsDouble[] resultatDividendeZero() {
		return new CalculsDouble[]{new CalculsDouble(0), new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour diviseur = 1.
	 * @param dividende Le dividende.
	 * @return Tableau [dividende, 0].
	 */
	private CalculsDouble[] resultatDiviseurUn(CalculsDouble dividende) {
		CalculsDouble quotient = new CalculsDouble(0);
		quotient.partieEntiereStr = dividende.partieEntiereStr;
		quotient.partieDecimaleStr = dividende.partieDecimaleStr;
		return new CalculsDouble[]{quotient, new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour dividende = diviseur.
	 * @return Tableau [1, 0].
	 */
	private CalculsDouble[] resultatNombresEgaux() {
		return new CalculsDouble[]{new CalculsDouble(1), new CalculsDouble(0)};
	}

	/**
	 * Verifie si le reste est superieur ou egal au diviseur.
	 * @param reste Le reste courant.
	 * @param diviseur Le diviseur.
	 * @return true si reste >= diviseur.
	 */
	private boolean resteSuperieurOuEgalDiviseur(CalculsDouble reste, CalculsDouble diviseur) {
		int comparaison = compareNombres(reste, diviseur);
		return comparaison == 0 || comparaison == 1;
	}

	/**
	 * Calcule l'increment du quotient.
	 * @param estApresVirgule Indique si on est apres la virgule.
	 * @param chiffresRestants Chiffres significatifs restants.
	 * @param chiffresTotal Chiffres significatifs totaux.
	 * @return Increment a ajouter au quotient.
	 */
	private CalculsDouble calculerIncrementQuotient(boolean estApresVirgule,
			int chiffresRestants, int chiffresTotal) {
		CalculsDouble increment = new CalculsDouble(0);
		if (!estApresVirgule) {
			increment.partieEntiereStr = "1";
			increment.partieDecimaleStr = "";
		} else {
			increment.partieEntiereStr = "0";
			StringBuilder zeros = new StringBuilder();
			for (int index = chiffresRestants + 1; index < chiffresTotal; index++) {
				zeros.append("0");
			}
			increment.partieDecimaleStr = zeros.toString() + "1";
		}
		return increment;
	}

	/**
	 * Division complete avec tous les parametres.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat sinon arrondit.
	 * @param chiffresSignificatifs Precision souhaitee (max 8).
	 * @return Tableau [quotient, reste] ou null si division par zero.
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur,
			boolean tronquee, int chiffresSignificatifs) {
		if (!tronquee) chiffresSignificatifs++;
		if (chiffresSignificatifs > 9) {
			aff("Erreur : maximum 8 chiffres significatifs.");
			chiffresSignificatifs = 9;
		}

		if (estDivisionParZero(diviseur)) {
			aff("Impossible de diviser par 0.");
			return null;
		}
		if (estDividendeZero(dividende)) {
			return resultatDividendeZero();
		}
		if (estDiviseurUn(diviseur)) {
			return resultatDiviseurUn(dividende);
		}
		if (sontEgaux(dividende, diviseur)) {
			return resultatNombresEgaux();
		}

		return effectuerDivision(dividende, diviseur, tronquee, chiffresSignificatifs);
	}

	/**
	 * Effectue la division par soustractions successives.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat.
	 * @param chiffresSignificatifs Precision.
	 * @return Tableau [quotient, reste].
	 */
	private CalculsDouble[] effectuerDivision(CalculsDouble dividende, CalculsDouble diviseur,
			boolean tronquee, int chiffresSignificatifs) {
		int chiffresRestants = chiffresSignificatifs;
		CalculsDouble reste = new CalculsDouble(0);
		reste.partieEntiereStr = dividende.partieEntiereStr + dividende.partieDecimaleStr;
		CalculsDouble quotient = new CalculsDouble(0);

		CalculsDouble diviseurComplet = new CalculsDouble(0);
		diviseurComplet.partieEntiereStr = diviseur.partieEntiereStr + diviseur.partieDecimaleStr;

		boolean apresVirguleGlobal = false;

		do {
			boolean apresVirgule = !resteSuperieurOuEgalDiviseur(reste, diviseurComplet);
			if (apresVirgule) apresVirguleGlobal = true;

			if (chiffresRestants > 0 && apresVirgule) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
			reste = soustraitNombres(reste, diviseurComplet);

			CalculsDouble increment = calculerIncrementQuotient(apresVirguleGlobal,
					chiffresRestants, chiffresSignificatifs);
			quotient = additionneNombres(quotient, increment);

			apresVirgule = !resteSuperieurOuEgalDiviseur(reste, diviseurComplet);
			if (apresVirgule) apresVirguleGlobal = true;
			if (chiffresRestants > 0 && apresVirgule) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
		} while (resteSuperieurOuEgalDiviseur(reste, diviseurComplet));

		quotient.estNegatif = calculerSigneMultiplication(dividende, diviseur);

		if (!tronquee) {
			quotient = arrondi(quotient, chiffresSignificatifs - 1);
		}
		return new CalculsDouble[]{quotient, reste};
	}

	/**public static void main (String [] args) {
		int cs = 10;

		CalculsDouble a = new CalculsDouble(7);
		CalculsDouble b = new CalculsDouble(10);

		aff(""+a.division_boucle(a, b, false, cs)[0]);

		//CalculsDouble a = new CalculsDouble(0.89);
		//CalculsDouble b = new CalculsDouble(9);
		//
		//aff(""+a.arrondi(a, 2));
	}**/


// ==============================================================================
// Fonctions mathématiques
// ==============================================================================

	/**
	 * Enleve les zeros en debut de chaine dans la partie entiere.
	 * @param ent Partie entiere a nettoyer.
	 * @return Partie entiere sans zeros inutiles en debut.
	 */
	public String epureZerosEnt(String ent) {
		boolean plus_de_zeros = false;
		int  res = 0, lenent = ent.length();

		/**
		**	Parcours le str pour savoir s'il n'ya a plus de zeros (boolean) 
		**	et dès qu'on a fini, enlève les zeros en trop.
		**/
		res = lenent;
		int i=0;
		while (i<lenent && ent.charAt(i)=='0') i++;
		res = i;

		if (i==lenent) return "0";

		return (ent.substring(res, lenent));
	}

	/**
	 * Trouve la position du dernier chiffre non-zero.
	 * @param partieDecimale Chaine a analyser.
	 * @return Position apres le dernier non-zero.
	 */
	private int trouverDernierNonZero(String partieDecimale) {
		int positionCoupure = partieDecimale.length();
		for (int index = 0; index < partieDecimale.length(); index++) {
			if (partieDecimale.charAt(index) == '0') {
				positionCoupure = index;
			} else {
				positionCoupure = partieDecimale.length();
			}
		}
		return positionCoupure;
	}

	/**
	 * Enleve les zeros inutiles en fin de partie decimale.
	 * @param partieDecimale Partie decimale a nettoyer.
	 * @return Partie decimale sans zeros inutiles en fin.
	 */
	public String envleveZerosInutils(String partieDecimale) {
		int positionCoupure = trouverDernierNonZero(partieDecimale);
		return partieDecimale.substring(0, positionCoupure);
	}

	/**
	 * Ajuste la partie decimale au nombre de chiffres significatifs.
	 * @param dec Partie decimale.
	 * @param nn Nombre de chiffres souhaites (-1 = minimum).
	 * @return Partie decimale ajustee.
	 */
	public String chiffresSignificatifs(String partieDecimaleParam, int nombreChiffres) {
		int longueurDecimale = partieDecimaleParam.length();
		int nombreZerosManquants = nombreChiffres - longueurDecimale;

		partieDecimaleParam = envleveZerosInutils(partieDecimaleParam);

		if (nombreChiffres == -1) {
			return partieDecimaleParam;
		}

		for (int index = 0; index < nombreZerosManquants; index++) {
			partieDecimaleParam += '0';
		}

		return partieDecimaleParam;
	}

	/**
	 * Calcule la valeur absolue d un entier.
	 * @param n0 Entier.
	 * @return Valeur absolue.
	 */
	public int abs(int n0) {
		if (n0<0) return -n0;
		else return n0;
	}

	/**
	 * Rend ce nombre positif (valeur absolue in-place).
	 */
	public void abs() {
		this.estNegatif = false;
	}

	/**
	 * Calcule la valeur absolue d un CalculsDouble.
	 * @param n0 Nombre.
	 * @return Copie avec valeur absolue.
	 */
	public CalculsDouble abs(CalculsDouble n0) {
		CalculsDouble n1 = new CalculsDouble(0);
		n1.partieEntiereStr = n0.partieEntiereStr; n1.partieDecimaleStr = n0.partieDecimaleStr; n1.representation_anglaise 
			= n0.representation_anglaise;
		n1.estNegatif = false;

		return n1;
	}

	/**
	 * Extrait la partie entiere d un double.
	 * @param n0 Nombre decimal.
	 * @return Partie entiere.
	 */
	public int partieEntiere(double n0) {
		return ((int)(n0));
	}

	/**
	 * Retourne la partie entiere de ce nombre.
	 * @return Partie entiere ou 0 si vide.
	 */
	public int partieEntiere() {
		if (this.partieEntiereStr.isEmpty()) return 0;
		else return Integer.parseInt(this.partieEntiereStr);
	}


	/**
	 * Arrondit un CalculsDouble et retourne l entier.
	 * @param n0 Nombre a arrondir.
	 * @return Entier arrondi.
	 */
	public int arrondiEntier(CalculsDouble n0) {
		String res = arrondi(n0).partieEntiereStr;
		if (res.isEmpty()) return 0;
		else return Integer.parseInt(res);
	}

	/**
	 * Arrondit un CalculsDouble et retourne une chaine.
	 * @param n1 Nombre a arrondir.
	 * @return Representation textuelle arrondie.
	 */
	public String arrondiString(CalculsDouble n1) {
		return arrondiString(n1, 0);
	}

	/**
	 * Arrondit un CalculsDouble a r0 decimales et retourne une chaine.
	 * @param n1 Nombre a arrondir.
	 * @param r0 Nombre de decimales.
	 * @return Representation textuelle arrondie.
	 */
	public String arrondiString(CalculsDouble n1, int r0) {
		return (""+arrondi(n1, r0));
	}

	/**
	 * Arrondit un CalculsDouble a l entier.
	 * @param n1 Nombre a arrondir.
	 * @return Nombre arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble n1) {
		return arrondi(n1, 0);
	}

	/**
	 * Verifie si le chiffre a la position donnee necessite un arrondi superieur.
	 * @param partieDecimale La partie decimale.
	 * @param position Position du chiffre a verifier.
	 * @return true si le chiffre > 4.
	 */
	private boolean doitArrondiSuperieur(String partieDecimale, int position) {
		if (position >= partieDecimale.length()) return false;
		return Character.getNumericValue(partieDecimale.charAt(position)) > 4;
	}

	/**
	 * Arrondit un nombre sans decimales.
	 * @param nombre Le nombre a arrondir.
	 * @return Nombre arrondi a l'entier.
	 */
	private CalculsDouble arrondirSansDecimales(CalculsDouble nombre) {
		if (nombre.partieDecimaleStr.isEmpty()) return nombre;
		if (doitArrondiSuperieur(nombre.partieDecimaleStr, 0)) {
			nombre = this.additionneNombres(nombre, new CalculsDouble(1));
		}
		nombre.partieDecimaleStr = "";
		return nombre;
	}

	/**
	 * Traite un chiffre lors de la propagation d'arrondi.
	 * @param partieDecimale Partie decimale actuelle.
	 * @param position Position du chiffre.
	 * @return Nouvelle partie decimale ou null si propagation continue.
	 */
	private String traiterChiffrePropagation(String partieDecimale, int position) {
		int chiffreIncrement = Character.getNumericValue(partieDecimale.charAt(position - 1)) + 1;
		if (chiffreIncrement >= 10) {
			return partieDecimale.substring(0, position);
		}
		return partieDecimale.substring(0, position - 1) + chiffreIncrement;
	}

	/**
	 * Propage l'arrondi dans la partie decimale.
	 * @param nombre Le nombre a modifier.
	 * @param positionDepart Position de depart pour la propagation.
	 * @return Nombre avec arrondi propage.
	 */
	private CalculsDouble propagerArrondiDecimales(CalculsDouble nombre, int positionDepart) {
		int position = positionDepart;

		while (position > 0 && doitArrondiSuperieur(nombre.partieDecimaleStr, position)) {
			String nouvellePartie = traiterChiffrePropagation(nombre.partieDecimaleStr, position);
			nombre.partieDecimaleStr = nouvellePartie;
			if (nouvellePartie.length() < position) {
				position--;
			} else {
				break;
			}
		}

		if (position == 0 && doitArrondiSuperieur(nombre.partieDecimaleStr, 0)) {
			nombre.partieDecimaleStr = "";
			nombre.partieEntiereStr = "" + (Integer.parseInt(nombre.partieEntiereStr) + 1);
		}
		return nombre;
	}

	/**
	 * Arrondit un CalculsDouble a un nombre de decimales donne.
	 * @param nombreOriginal Le nombre a arrondir.
	 * @param nombreDecimales Nombre de decimales souhaitees (0 = entier).
	 * @return Nombre arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble nombreOriginal, int nombreDecimales) {
		CalculsDouble nombre = copie(nombreOriginal);

		if (nombreDecimales < 1) {
			return arrondirSansDecimales(nombre);
		}
		if (nombre.partieDecimaleStr.isEmpty()) {
			return nombre;
		}
		if (nombre.partieDecimaleStr.length() <= nombreDecimales) {
			return nombre;
		}
		if (!doitArrondiSuperieur(nombre.partieDecimaleStr, nombreDecimales)) {
			nombre.partieDecimaleStr = nombre.partieDecimaleStr.substring(0, nombreDecimales);
			return nombre;
		}

		return propagerArrondiDecimales(nombre, nombreDecimales);
	}

	/**
	 * Detecte le separateur decimal dans une chaine.
	 * @param nombreChaine Representation du nombre.
	 * @return Separateur trouve ('.' ou ',').
	 */
	private char detecterSeparateurDecimal(String nombreChaine) {
		if (nombreChaine.indexOf(",") != -1) return ',';
		return '.';
	}

	/**
	 * Extrait la partie decimale d un double sous forme de chaine.
	 * @param nombre Nombre decimal.
	 * @return Partie decimale sans le point.
	 */
	public String partieDecimale(double nombre) {
		String nombreChaine = "" + nombre;
		char separateur = detecterSeparateurDecimal(nombreChaine);
		int positionSeparateur = nombreChaine.indexOf(separateur);

		if (positionSeparateur == -1) return "0";
		return nombreChaine.substring(positionSeparateur + 1);
	}

	/**
	 * Egalise le nombre de decimales de deux CalculsDouble.
	 * @param a Premier nombre.
	 * @param b Second nombre.
	 * @return Tableau des deux nombres avec decimales egalisees.
	 */
	public CalculsDouble[] egaliseNombres(CalculsDouble a, CalculsDouble b) {
		if (a.partieDecimaleStr.length()!=b.partieDecimaleStr.length()) {
			if (Math.max(a.partieDecimaleStr.length(), b.partieDecimaleStr.length())==a.partieDecimaleStr.length()) {
				String tmpa="";
				int tmplena=a.partieDecimaleStr.length()-b.partieDecimaleStr.length();
				for (int i=0; i<tmplena; i++)
					tmpa+="0";
				b.partieDecimaleStr+=tmpa;
			}

			if (Math.max(a.partieDecimaleStr.length(), b.partieDecimaleStr.length())==b.partieDecimaleStr.length()) {
				String tmpb="";
				int tmplenb=b.partieDecimaleStr.length()-a.partieDecimaleStr.length();
				for (int i=0; i<tmplenb; i++)
					tmpb+="0";
				a.partieDecimaleStr+=tmpb;
			}
		}

		CalculsDouble [] res = new CalculsDouble [2]; res[0] = a; res[1] = b;

		return res;
	}
	/**
	 * Copie un CalculsDouble dans un nouveau.
	 * @param a Nombre a copier.
	 * @return Copie independante.
	 */
	public CalculsDouble copie(CalculsDouble a) {
		CalculsDouble res = new CalculsDouble(0);

		res.partieEntiereStr = a.partieEntiereStr;
		res.partieDecimaleStr = a.partieDecimaleStr;
		res.estNegatif = a.estNegatif;
		res.representation_anglaise = a.representation_anglaise;

		return res;
	}

	/**
	 * Verifie l egalite avec un autre CalculsDouble.
	 * @param a Nombre a comparer.
	 * @return true si egaux.
	 */
	public boolean equals(CalculsDouble a) {
		return (compareNombres(this, a)==0);
	}
	/**
	 * Compare deux CalculsDouble.
	 * @param a Premier nombre.
	 * @param b Second nombre.
	 * @return 0 si egaux, 1 si a > b, 2 si b > a, -1 si erreur.
	 */
	public int compareNombres(CalculsDouble a, CalculsDouble b) {
		int tmp = -1;
		if (a.estNegatif && !b.estNegatif) return 2;
		if (!a.estNegatif && b.estNegatif) return 1;

		if (a.estNegatif && b.estNegatif) {
			tmp = compareNombres_abs(a, b);

			if (tmp==0) return 0;
			if (tmp==1) return 2;
			if (tmp==2) return 1;
		}

		if (!a.estNegatif && !b.estNegatif)
			return compareNombres_abs (a, b);

		return -1;
	}

	/**
	 * Compare deux chaines chiffre par chiffre.
	 * @param chaineA Premiere chaine.
	 * @param chaineB Seconde chaine.
	 * @param longueur Nombre de caracteres a comparer.
	 * @return 0 si egaux, 1 si A > B, 2 si B > A.
	 */
	private int comparerChiffresParChiffres(String chaineA, String chaineB, int longueur) {
		for (int index = 0; index < longueur; index++) {
			int chiffreA = Integer.parseInt("" + chaineA.charAt(index));
			int chiffreB = Integer.parseInt("" + chaineB.charAt(index));
			if (chiffreA > chiffreB) return 1;
			if (chiffreA < chiffreB) return 2;
		}
		return 0;
	}

	/**
	 * Compare les longueurs des parties entieres.
	 * @param longueurA Longueur partie entiere A.
	 * @param longueurB Longueur partie entiere B.
	 * @return 0 si egaux, 1 si A > B, 2 si B > A.
	 */
	private int comparerLongueurs(int longueurA, int longueurB) {
		if (longueurA > longueurB) return 1;
		if (longueurA < longueurB) return 2;
		return 0;
	}

	/**
	 * Compare les valeurs absolues de deux CalculsDouble.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egaux, 1 si |a| > |b|, 2 si |b| > |a|.
	 */
	public int compareNombres_abs(CalculsDouble nombreA, CalculsDouble nombreB) {
		int resultatLongueur = comparerLongueurs(nombreA.partieEntiereStr.length(), nombreB.partieEntiereStr.length());
		if (resultatLongueur != 0) return resultatLongueur;

		int resultatEntier = comparerChiffresParChiffres(nombreA.partieEntiereStr, nombreB.partieEntiereStr, nombreA.partieEntiereStr.length());
		if (resultatEntier != 0) return resultatEntier;

		int longueurMin = Math.min(nombreA.partieDecimaleStr.length(), nombreB.partieDecimaleStr.length());
		int resultatDecimal = comparerChiffresParChiffres(nombreA.partieDecimaleStr, nombreB.partieDecimaleStr, longueurMin);
		if (resultatDecimal != 0) return resultatDecimal;

		if (nombreA.partieDecimaleStr.length() > longueurMin) {
			if (Integer.parseInt("" + nombreA.partieDecimaleStr.charAt(longueurMin)) > 0) return 1;
		}
		if (nombreB.partieDecimaleStr.length() > longueurMin) {
			if (Integer.parseInt("" + nombreB.partieDecimaleStr.charAt(longueurMin)) > 0) return 2;
		}
		return 0;
	}

	/**
	 * Detecte un facteur rapide (2, 3, 5) selon le dernier chiffre.
	 * @param nombre Nombre a tester.
	 * @return Facteur detecte ou -1 si aucun.
	 */
	private int detecterFacteurRapide(int nombre) {
		char dernierChiffre = ("" + nombre).charAt(("" + nombre).length() - 1);
		if (dernierChiffre == '0' || dernierChiffre == '5') return 5;
		if (dernierChiffre == '2' || dernierChiffre == '4' || dernierChiffre == '8') return 2;
		if (nombre % 3 == 0) return 3;
		return -1;
	}

	/**
	 * Trouve le prochain facteur premier d'un nombre.
	 * @param nombre Le nombre a factoriser.
	 * @return Le facteur premier trouve.
	 */
	private int trouverProchainFacteurPremier(int nombre) {
		int facteurRapide = detecterFacteurRapide(nombre);
		if (facteurRapide != -1) return facteurRapide;

		for (int diviseur = 2; diviseur < nombre; diviseur++) {
			if (nombre % diviseur == 0) return diviseur;
		}
		return nombre;
	}

	/**
	 * Convertit une chaine de facteurs en tableau d'entiers.
	 * @param chaineFacteurs Facteurs separes par ':'.
	 * @return Tableau des facteurs premiers.
	 */
	private int[] convertirChaineEnTableauFacteurs(String chaineFacteurs) {
		String[] facteursChaines = chaineFacteurs.split(":");
		int nombreFacteursValides = 0;

		for (int index = 0; index < facteursChaines.length; index++) {
			if (!facteursChaines[index].isEmpty()) {
				nombreFacteursValides++;
			}
		}

		int[] tableauFacteurs = new int[nombreFacteursValides];
		int indexResultat = 0;
		for (int index = 0; index < facteursChaines.length; index++) {
			if (!facteursChaines[index].isEmpty()) {
				tableauFacteurs[indexResultat] = Integer.parseInt(facteursChaines[index]);
				indexResultat++;
			}
		}
		return tableauFacteurs;
	}

	/**
	 * Decompose un nombre en facteurs premiers.
	 * @param nombre Le nombre a decomposer.
	 * @return Tableau des facteurs premiers ou null si invalide.
	 */
	public int[] decompose_en_facteurs_premiers(CalculsDouble nombre) {
		if (!nombre.partieDecimaleStr.isEmpty()) {
			aff("Partie decimale ignoree, troncature entiere utilisee.");
		}
		if (nombre.partieEntiereStr.isEmpty()) {
			return null;
		}

		int valeurCourante = Integer.parseInt(nombre.partieEntiereStr);
		StringBuilder facteursChaine = new StringBuilder();

		aff("n = " + valeurCourante);

		while (!estPremier(valeurCourante)) {
			int facteur = trouverProchainFacteurPremier(valeurCourante);
			facteursChaine.append(facteur).append(":");
			valeurCourante = valeurCourante / facteur;
			if (valeurCourante == 0) valeurCourante = 1;
			aff("n = " + valeurCourante);
		}

		facteursChaine.append(valeurCourante);
		int[] resultat = convertirChaineEnTableauFacteurs(facteursChaine.toString());

		aff("Facteurs = " + facteursChaine);
		int verification = 1;
		for (int index = 0; index < resultat.length; index++) {
			verification *= resultat[index];
		}
		aff("Verification : n = " + verification);

		return resultat;
	}





	/**
	 * Calcule la somme des chiffres d un nombre.
	 * @param n0 Nombre sous forme de chaine.
	 * @return Somme des chiffres.
	 */
	public int somme_des_chiffres(String n0) {
		int i, n0len = n0.length(), res=0;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res+=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}

	/**
	 * Calcule le produit des chiffres d un nombre.
	 * @param n0 Nombre sous forme de chaine.
	 * @return Produit des chiffres ou 0 si vide.
	 */
	public int produit_des_chiffres(String n0) {
		int i, n0len = n0.length(), res=1;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res*=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}
	/**
	 * Verifie si un entier est premier.
	 * @param n0 Entier a tester.
	 * @return true si n0 est premier.
	 */
	public boolean estPremier(int n0) {
		for (int i=2; i<1+(int)(Math.sqrt((double)(n0))); i++) {
			if (n0%i==0) return false;
		}

		return true;
	}

	/**
	 * Genere un tableau des n premiers nombres premiers.
	 * @param quantite Nombre de premiers a generer.
	 * @return Tableau des nombres premiers.
	 */
	private int[] genererTableauPremiers(int quantite) {
		int[] premiers = new int[quantite];
		int compteur = 0;
		int candidat = 2;

		while (compteur < quantite) {
			if (estPremier(candidat)) {
				premiers[compteur] = candidat;
				compteur++;
			}
			candidat++;
		}
		return premiers;
	}

	/**
	 * Calcule et affiche les n premiers nombres premiers.
	 * @param quantite Nombre de premiers a calculer.
	 */
	public void clacule_n_premiers(int quantite) {
		if (quantite < 1) return;

		int[] premiers = genererTableauPremiers(quantite);

		affnn("p" + quantite + " = {");
		for (int index = 0; index < premiers.length - 1; index++) {
			affnn(premiers[index] + ", ");
		}
		aff(premiers[premiers.length - 1] + "};");
	}

// ==============================================================================
// Fonctions utilitaires
// ==============================================================================


	/**
	 * Verifie si une chaine represente un entier valide.
	 * @param n0 Chaine a tester.
	 * @return true si parsable en entier.
	 */
	public boolean isInteger(String n0) {
		try {
			int n1 = Integer.parseInt(n0);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Convertit une chaine en entier avec gestion des erreurs.
	 * @param n0 Chaine a convertir.
	 * @return Entier ou -1 si erreur.
	 */
	public int convertStrInt(String n0) {
		if (n0==null) {
			aff("n0 est null");
			return -1;
		}
		if (n0.isEmpty()) {
			aff("n0 est egal a "+'"'+'"'+".");
			return -1;
		}
		try {
			return Integer.parseInt(n0);
		}
		catch(NumberFormatException e) {
			aff("Impossible de convertir ");
			return -1;
		}
	}

	/**
	 * Verifie si un caractere est un chiffre.
	 * @param n0 Caractere a tester.
	 * @return true si chiffre (0-9).
	 */
	public boolean isCharNumber(char n0) {
		return (n0>='0' && n0<='9');
	}

	/**
	 * Verifie caractere par caractere si une chaine est un entier.
	 * @param n0 Chaine a tester.
	 * @return true si tous les caracteres sont des chiffres.
	 */
	public boolean isInteger_deep(String n0) {
		if (n0.isEmpty()) return false;
		int n0len = n0.length();

		for (int i=0; i<n0len; i++) {
			if (!isCharNumber(n0.charAt(i))) return false;
		}

		return true;
	}

	/**
	 * Verifie si un CalculsDouble est valide numeriquement.
	 * @param n0 Nombre a tester.
	 * @return true si parties entiere et decimale sont valides.
	 */
	public boolean isInteger(CalculsDouble n0) {
		boolean res = false;
		int i=0;
		String n0ent = n0.partieEntiereStr;
		String n0dec = n0.partieDecimaleStr;

		if (!n0ent.isEmpty()) {
			res = isInteger(n0ent);
		}
		if (!n0dec.isEmpty()) {
			res = isInteger(n0dec);
		}

		return res;
	}

	/**
	 * Verifie si une chaine ne contient que des zeros.
	 * @param chaine Chaine a tester.
	 * @return true si uniquement des zeros.
	 */
	private boolean contientUniquementZeros(String chaine) {
		for (int index = 0; index < chaine.length(); index++) {
			if (chaine.charAt(index) != '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifie si un CalculsDouble vaut zero.
	 * @param nombre Nombre a tester.
	 * @return true si nombre = 0.
	 */
	public boolean estNul(CalculsDouble nombre) {
		if (!isInteger(nombre)) return false;
		if (!contientUniquementZeros(nombre.partieEntiereStr)) return false;
		if (!nombre.partieDecimaleStr.isEmpty() && !contientUniquementZeros(nombre.partieDecimaleStr)) return false;
		return true;
	}

	/**
	 * Compte les occurrences d un caractere dans une chaine.
	 * @param test Chaine a analyser.
	 * @param cc Caractere a compter.
	 * @return Nombre d occurrences.
	 */
	public int compteOcurrences(String test, char cc) {
		if (test==null) return 0;
		if (test.isEmpty()) return 0;
		// Gestion des erreurs

		int res = 0, lentest = test.length();
		for (int i=0; i<lentest; i++) {
			if (test.charAt(i)==cc) res++;
		}
		return res;
	}


	/**
	 * Affiche un objet sans retour a la ligne.
	 * @param s0 Objet a afficher.
	 */
	public static void affnn(Object s0) {
		System.out.print(""+s0);
	}

	/**
	 * Affiche un objet avec retour a la ligne.
	 * @param s0 Objet a afficher.
	 */
	public static void aff(Object s0) {
		System.out.println(""+s0);
	}

	/**
	 * Affiche un tableau d entiers.
	 * @param n0 Tableau a afficher.
	 */
	public void affTab(int[] n0) {
		for (int i=0; i<n0.length; i++) {
			aff("t["+i+"] = "+n0[i]);
		}
	}

	/**
	 * Affiche un tableau d entiers avec un nom.
	 * @param n0 Tableau a afficher.
	 * @param nom Nom du tableau.
	 */
	public void affTab(int[] n0, String nom) {
		for (int i=0; i<n0.length; i++) {
			aff(nom+"["+i+"] = "+n0[i]);
		}
	}

	/**
	 * Affiche un tableau de chaines avec un nom.
	 * @param t1 Tableau a afficher.
	 * @param nom Nom du tableau.
	 */
	public void afftab2dim(String[] t1, String nom) {
		int i;

		//aff(nom+" : ");

		for (i=0; i<t1.length; i++) {
			aff(nom+"["+i+"] = "+t1[i]);
		}
	}

	/**
	 * Double la taille d un tableau d entiers.
	 * @param t0 Tableau original.
	 * @return Nouveau tableau de taille doublee + 1.
	 */
	public int[] agrandieTab(int[] t0) {
		int i;
		int [] t1 = new int[t0.length*2+1];

		for (i = 0; i<t0.length; i++) {
			t1[i] = t0[i];
		}

		return t1;
	}


// ==============================================================================
// toString
// ==============================================================================

	/**
	 * Construit le suffixe decimal pour l'affichage.
	 * @param partieDecimale Partie decimale formatee.
	 * @param virgule Caractere separateur.
	 * @return Suffixe decimal ou chaine vide.
	 */
	private String construireSuffixeDecimal(String partieDecimale, char virgule) {
		if (partieDecimale.isEmpty()) return "";
		if (Integer.parseInt(partieDecimale) == 0) return "";
		return virgule + partieDecimale;
	}

	/**
	 * Convertit le CalculsDouble en chaine lisible.
	 * @return Representation textuelle du nombre.
	 */
	public String toString() {
		String partieEntiere = partieEntiereStr.isEmpty() ? "0" : partieEntiereStr;
		String partieDecimale = chiffresSignificatifs(this.partieDecimaleStr, -1);
		char virgule = representation_anglaise ? '.' : ',';
		String suffixe = construireSuffixeDecimal(partieDecimale, virgule);
		String signe = estNegatif ? "-" : "";
		return signe + partieEntiere + suffixe;
	}

// ==============================================================================
// Reconnaissance opérations
// ==============================================================================


	Character [] operateurs_tmp = 
		{'+', '-', '*', '/'};
	Stack<String> st = new Stack<String>();
	ArrayList<Character> operateurs = new ArrayList<Character>();

	/**
	 * Initialise la liste des operateurs reconnus.
	 */
	public void operateurs() {
		for (Character c1 : operateurs_tmp) {
			operateurs.add(c1);
		}
	}

	/**
	 * Valide une expression (non null, non vide).
	 * @param expression Expression a valider.
	 * @return true si valide, false sinon.
	 */
	private boolean validerExpression(String expression) {
		if (expression == null) {
			aff("L'expression est null");
			return false;
		}
		if (expression.isEmpty()) {
			aff("L'expression est vide.");
			return false;
		}
		return true;
	}

	/**
	 * Traite un token (nombre ou operateur) dans l'expression.
	 * @param expression Expression source.
	 * @param position Position actuelle.
	 * @param compteur Compteur d'iterations.
	 * @return Nouvelle position apres traitement.
	 */
	private int traiterTokenExpression(String expression, int position, int compteur) {
		if (isCharNumber(expression.charAt(position))) {
			String nombre = mange_nombre(expression, position);
			st.push(nombre);
			while (position < expression.length() && isCharNumber(expression.charAt(position))) {
				position++;
			}
		}
		if (compteur > 0) {
			calcul_pile(st);
		}
		if (position < expression.length() && operateurs.contains(expression.charAt(position))) {
			String operateur = "" + mange_operateur(expression, position);
			st.push(operateur);
		}
		return position;
	}

	/**
	 * Analyse une expression arithmetique sans parentheses.
	 * @param expression Expression a analyser.
	 * @return Resultat du calcul ou -1 si erreur.
	 */
	public int analyse_expression_sans_parentheses(String expression) {
		if (!validerExpression(expression)) {
			return -1;
		}

		int compteur = 0;
		for (int position = 0; position < expression.length(); position++) {
			position = traiterTokenExpression(expression, position, compteur);
			if (position >= expression.length()) break;
			compteur++;
		}
		String resultat = pop_erreur(st);
		return convertStrInt(resultat);
	}

// ==============================================================================
// Fonctions de traitement des symboles
// ==============================================================================

	/**
	 * Depile un element avec gestion d erreur.
	 * @param st Pile source.
	 * @return Element depile ou chaine vide si pile vide.
	 */
	public String pop_erreur(Stack<String> st) {
		if (!st.empty()) {
			return st.pop();
		}
		else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	/**
	 * Effectue un calcul avec les trois premiers elements de la pile.
	 * @param st Pile contenant operandes et operateur.
	 */
	public void calcul_pile(Stack<String> st) {
		String aS = pop_erreur(st);
		String symS = pop_erreur(st);
		String bS = pop_erreur(st);

		int res_tmp = traite_symbolesInt(bS, aS, symS);
		aff("res_tmp = "+res_tmp);
		st.push(""+res_tmp);
	}

	/**
	 * Valide les entrees pour traite_symbolesInt.
	 * @param operandeA Premier operande.
	 * @param operandeB Second operande.
	 * @param operateur Operateur.
	 * @return true si valide, false sinon.
	 */
	private boolean validerEntreesSymbolesInt(String operandeA, String operandeB, String operateur) {
		if (operateur == null || operateur.isEmpty()) {
			aff("Symbole null ou egal a " + '"' + '"' + ".");
			return false;
		}
		if (!isInteger(operandeA) || !isInteger(operandeB)) {
			aff("operandeA (= " + operandeA + ") ou operandeB (= " + operandeB + ") ne sont pas des nombres.");
			return false;
		}
		return true;
	}

	/**
	 * Calcule le resultat d'une operation entiere.
	 * @param valeurA Premiere valeur.
	 * @param valeurB Seconde valeur.
	 * @param symbole Operateur.
	 * @return Resultat du calcul.
	 */
	private int calculerOperationEntiere(int valeurA, int valeurB, char symbole) {
		if (symbole == '+') return valeurA + valeurB;
		if (symbole == '-') return valeurA - valeurB;
		if (symbole == '*') return valeurA * valeurB;
		if (symbole == '/') return valeurA / valeurB;
		return 0;
	}

	/**
	 * Applique un operateur sur deux operandes.
	 * @param operandeA Premier operande.
	 * @param operandeB Second operande.
	 * @param operateurStr Operateur.
	 * @return Resultat ou -1 si erreur.
	 */
	public int traite_symbolesInt(String operandeA, String operandeB, String operateurStr) {
		if (!validerEntreesSymbolesInt(operandeA, operandeB, operateurStr)) {
			return -1;
		}

		char symbole = operateurStr.charAt(0);
		if (!operateurs.contains(symbole)) {
			aff("symbole (= " + symbole + ") n'est pas un operateur.");
			return -1;
		}

		int valeurA = Integer.parseInt(operandeA);
		int valeurB = Integer.parseInt(operandeB);
		return calculerOperationEntiere(valeurA, valeurB, symbole);
	}

// ==============================================================================
// Fonction mange_symbole
// ==============================================================================

	/**
	 * Verifie si un nombre est null ou vide.
	 * @param nombre Nombre a verifier.
	 * @return true si invalide, false sinon.
	 */
	private boolean estNombreInvalide(String nombre) {
		if (nombre == null) {
			aff("Le nombre est null.");
			return true;
		}
		if (nombre.isEmpty()) {
			aff("Le nombre est vide.");
			return true;
		}
		return false;
	}

	/**
	 * Verifie le format des milliers dans la partie decimale.
	 * @param partieDecimale Partie apres le separateur.
	 * @param separateurMilliers Separateur de milliers.
	 * @return true si format valide, false sinon.
	 */
	private boolean verifierFormatMilliers(String partieDecimale, char separateurMilliers) {
		int longueur = partieDecimale.length();
		for (int index = 0; index < longueur; index++) {
			if (index % 3 == 0) {
				if (partieDecimale.charAt(index) != separateurMilliers) {
					return false;
				}
			} else {
				if (!isCharNumber(partieDecimale.charAt(index))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Verifie et nettoie un nombre decimal avec separateurs.
	 * @param nombre Nombre a verifier.
	 * @param separateurDecimal Separateur decimal utilise.
	 * @return Nombre nettoye ou chaine vide si invalide.
	 */
	public String verifie_double_correcte(String nombre, char separateurDecimal) {
		if (estNombreInvalide(nombre)) {
			return "";
		}

		char separateurMilliers = (separateurDecimal == '.') ? ',' : '.';
		String[] parties = nombre.split("" + separateurDecimal);

		if (parties == null || parties.length < 1) {
			return "";
		}
		if (parties.length == 1) {
			if (parties[0] == null || parties[0].isEmpty() || parties[0].equals(",")) {
				return "";
			}
			return nombre.replace("" + separateurDecimal, "");
		}

		String partieDecimale = parties[1];
		if (!verifierFormatMilliers(partieDecimale, separateurMilliers)) {
			return "";
		}
		return parties[0] + partieDecimale.replace("" + separateurMilliers, "");
	}

	/**
	 * Analyse un nombre sans separateur decimal.
	 * @param nombre Chaine a analyser.
	 * @return Entier parse ou -1 si invalide.
	 */
	private double analyserNombreSansSeparateur(String nombre) {
		if (isInteger(nombre)) {
			return Integer.parseInt(nombre);
		}
		return -1;
	}

	/**
	 * Nettoie un nombre avec separateurs mixtes.
	 * @param nombre Chaine a nettoyer.
	 * @param nombreVirgules Nombre de virgules.
	 * @param nombrePoints Nombre de points.
	 * @return Nombre nettoye.
	 */
	private String nettoyerSeparateurs(String nombre, int nombreVirgules, int nombrePoints) {
		if (nombreVirgules == 1 && nombrePoints > 1) {
			return verifie_double_correcte(nombre, ',');
		}
		if (nombreVirgules > 1 && nombrePoints == 1) {
			return verifie_double_correcte(nombre, '.');
		}
		return nombre;
	}

	/**
	 * Parse un nombre avec separateur decimal.
	 * @param nombre Chaine a parser.
	 * @param nombreVirgules Nombre de virgules.
	 * @param nombrePoints Nombre de points.
	 * @return Double parse ou -1 si invalide.
	 */
	private double parserNombreAvecSeparateur(String nombre, int nombreVirgules, int nombrePoints) {
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

	/**
	 * Analyse une chaine et la convertit en double.
	 * @param nombre Chaine a analyser.
	 * @return Valeur double ou -1 si erreur.
	 */
	public double analyse_nombreDouble(String nombre) {
		int nombreVirgules = compteOcurrences(nombre, ',');
		int nombrePoints = compteOcurrences(nombre, '.');

		if (nombreVirgules == 0 && nombrePoints == 0) {
			return analyserNombreSansSeparateur(nombre);
		}

		String nombreNettoye = nettoyerSeparateurs(nombre, nombreVirgules, nombrePoints);
		int virgApresNettoyage = compteOcurrences(nombreNettoye, ',');
		int ptsApresNettoyage = compteOcurrences(nombreNettoye, '.');

		return parserNombreAvecSeparateur(nombreNettoye, virgApresNettoyage, ptsApresNettoyage);
	}

	/**
	 * Extrait les caracteres d'un nombre depuis une expression.
	 * @param expression Expression source.
	 * @param debut Position de depart.
	 * @return Tableau [nombreExtrait, aVirgule, aPoint].
	 */
	private Object[] extraireCaracteresNombre(String expression, int debut) {
		String resultat = "";
		boolean aVirgule = false;
		boolean aPoint = false;

		for (int index = debut; index < expression.length(); index++) {
			char caractere = expression.charAt(index);
			if (isCharNumber(caractere)) {
				resultat += caractere;
			} else if (caractere == ',') {
				aVirgule = true;
				resultat += caractere;
			} else if (caractere == '.') {
				aPoint = true;
				resultat += caractere;
			} else {
				break;
			}
		}
		return new Object[]{resultat, aVirgule, aPoint};
	}

	/**
	 * Extrait un nombre depuis une position dans une expression.
	 * @param expression Expression source.
	 * @param indiceDebut Position de depart.
	 * @return Nombre extrait sous forme de chaine.
	 */
	public String mange_nombre(String expression, int indiceDebut) {
		Object[] extraction = extraireCaracteresNombre(expression, indiceDebut);
		String nombreExtrait = (String) extraction[0];
		boolean aVirgule = (Boolean) extraction[1];
		boolean aPoint = (Boolean) extraction[2];

		aff(nombreExtrait);
		if (aVirgule || aPoint) {
			return "" + analyse_nombreDouble(nombreExtrait);
		}
		return nombreExtrait;
	}

	/**
	 * Extrait un operateur a une position donnee.
	 * @param expr Expression source.
	 * @param indice Position de l operateur.
	 * @return Caractere operateur ou -1 si invalide.
	 */
	public char mange_operateur(String expr, int indice) {
		int lenexpr = expr.length();

		if (indice>=0 && indice<lenexpr) {
			aff(expr.charAt(indice));
			return expr.charAt(indice);
		}
		else return (char)(-1);
	}
	
// ==============================================================================
// Main
// ==============================================================================

	/**
	 * Point d entree principal pour les tests.
	 * @param args Arguments de ligne de commande.
	 */
	public static void main(String[] args) {
		CalculsDouble cb = new CalculsDouble();
		/**String expr1
		//"103*8/09+3-8"
		 = "";
		int res_verififcation = 0;
		aff("expr1 = "+expr1);
		cb.operateurs();
		int res = cb.analyse_expression_sans_parentheses(expr1);
		aff("res = "+res);
		aff("Verification : "+res_verififcation);
		**/
		String nombre = "12.100,0";
		aff("nombre = "+nombre);
		Double tmpD = cb.analyse_nombreDouble(nombre);
		aff("res = "+tmpD);
	}

}
