import java.math.BigInteger;
import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;

class CalculsDouble {
	boolean estNegatif=false;
	boolean representation_anglaise=false;

	String ent;
	String dec;

// ############################### Constructeurs ####################################### //

	public CalculsDouble () {
		ent="0";
		dec="";
	}

	public CalculsDouble (int n0) {
		ent = ""+(int)(n0);
		dec = "";

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (double n0) {
		ent = ""+(int)(n0);
		dec = partieDecimale(n0);

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (int n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		ent = ""+(int)(n0);
		dec = "0";

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (double n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		ent = ""+(int)(n0);
		dec = partieDecimale(n0);

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}


// ################################# Opérations ######################################## //

	private static String padRight(String value, int targetLength) {
		StringBuilder sb = new StringBuilder(value);
		while (sb.length() < targetLength) {
			sb.append('0');
		}
		return sb.toString();
	}

	private static String padLeft(String value, int targetLength) {
		StringBuilder sb = new StringBuilder(value);
		while (sb.length() < targetLength) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}

	private BigInteger toScaledBigInteger(CalculsDouble nombre, int scale) {
		String digits = nombre.ent + padRight(nombre.dec, scale);
		if (digits.isEmpty()) {
			digits = "0";
		}
		BigInteger valeur = new BigInteger(digits);
		if (nombre.estNegatif && valeur.signum() != 0) {
			valeur = valeur.negate();
		}
		return valeur;
	}

	private CalculsDouble fromScaledValue(BigInteger valeur, int scale) {
		CalculsDouble res = new CalculsDouble();
		res.estNegatif = valeur.signum() < 0;
		BigInteger absolue = valeur.abs();
		String digits = absolue.toString();
		if (scale == 0) {
			res.ent = digits;
			res.dec = "";
		} else {
			if (digits.length() <= scale) {
				digits = padLeft(digits, scale + 1);
			}
			int split = digits.length() - scale;
			res.ent = digits.substring(0, split);
			res.dec = digits.substring(split);
			res.dec = res.dec.replaceFirst("0+$", "");
		}
		if (res.ent.isEmpty()) res.ent = "0";
		if (res.dec == null) res.dec = "";
		if (res.ent.equals("0") && res.dec.isEmpty()) {
			res.estNegatif = false;
		}
		return res;
	}

	public CalculsDouble additionneNombres (CalculsDouble a, CalculsDouble b){
		int scale = Math.max(a.dec.length(), b.dec.length());
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
		if (!nombreA.dec.isEmpty()) totalDecimales += nombreA.dec.length();
		if (!nombreB.dec.isEmpty()) totalDecimales += nombreB.dec.length();
		return totalDecimales;
	}

	/**
	 * Calcule la longueur totale d'un nombre (entier + decimales).
	 * @param nombre Le nombre a mesurer.
	 * @return Longueur totale.
	 */
	private int longueurTotaleNombre(CalculsDouble nombre) {
		return nombre.ent.length() + nombre.dec.length();
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
			valeurPartielle.ent = matriceResultats[index];
			somme.ent = additionneNombres(valeurPartielle, somme).ent;
		}
		return somme;
	}

	/**
	 * Ajuste le resultat en separant partie entiere et decimale.
	 * @param resultat Resultat a ajuster.
	 * @param nombreDecimales Nombre de decimales attendues.
	 */
	private void ajusterPartieDecimale(CalculsDouble resultat, int nombreDecimales) {
		if (nombreDecimales > 0 && resultat.ent.length() > nombreDecimales) {
			int positionVirgule = resultat.ent.length() - nombreDecimales;
			resultat.dec = resultat.ent.substring(positionVirgule);
			resultat.ent = resultat.ent.substring(0, positionVirgule);
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

		String chaineNombreA = nombreA.ent + nombreA.dec;
		String chaineNombreB = nombreB.ent + nombreB.dec;

		String[] matriceResultats = remplirMatriceMultiplication(chaineNombreA, chaineNombreB);
		CalculsDouble resultat = additionnerResultatsPartiels(matriceResultats);

		if (!nombreA.dec.isEmpty() || !nombreB.dec.isEmpty()) {
			ajusterPartieDecimale(resultat, totalDecimales);
		}

		resultat.estNegatif = resultatNegatif;
		return resultat;
	}

	public CalculsDouble soustraitNombres (CalculsDouble a, CalculsDouble b){
		int recul = 0;
		int chiffres_avant_la_virgule = Math.min(compteOcurrences(a.dec, 
			'0'), compteOcurrences(b.dec, '0'));
		CalculsDouble c = new CalculsDouble ();
		CalculsDouble oppose = copie(b);
		boolean estZero = oppose.ent.equals("0") && (oppose.dec.isEmpty() || oppose.dec.replace("0", "").isEmpty());
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
		quotient.ent = dividende.ent;
		quotient.dec = dividende.dec;
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
			increment.ent = "1";
			increment.dec = "";
		} else {
			increment.ent = "0";
			StringBuilder zeros = new StringBuilder();
			for (int index = chiffresRestants + 1; index < chiffresTotal; index++) {
				zeros.append("0");
			}
			increment.dec = zeros.toString() + "1";
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
		reste.ent = dividende.ent + dividende.dec;
		CalculsDouble quotient = new CalculsDouble(0);

		CalculsDouble diviseurComplet = new CalculsDouble(0);
		diviseurComplet.ent = diviseur.ent + diviseur.dec;

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


// ########################## Fonctions mathématiques ################################## //

	/**
		Enlève les zéros en début de string dans la partie entiere (String ent).
	**/
	public String epureZerosEnt (String ent) {
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
		-1 : pas de chiffres significatifs (on fait comme en maths : 
		le moins de chiffres possible). n : n chiffres aux minimum.
	**/
	public String envleveZerosInutils (String dec) {
		boolean plus_de_zeros = false;
		int  res = 0, lendec = dec.length();

		/**
		**	Parcours le str pour savoir s'il n'ya a plus de zeros (boolean) 
		**	et dès qu'on a fini, enlève les zeros en trop.
		**/
		res = lendec;
		for (int i=0; i<lendec; i++) {
			if (dec.charAt(i)=='0') {
				plus_de_zeros = true;
				res = i;
			}
			else {
				plus_de_zeros = false;
				res = lendec;
			}
		}

		return (dec.substring(0, res));
	}

	public String chiffresSignificatifs (String dec, int nn) {
		int lendec = dec.length();
		int nbrZeros = nn-lendec;

		/**
			enlèves les zéros inutils (sans les compter au préalable 
			(plus simple à coder))
		**/
		dec = envleveZerosInutils (dec);

		if (nn == -1) return dec; 
		// cas de base (on enlève 
		// le plus de zéros possibles, comme en maths).

		/* Ajoute des zeros si besoin */
		for (int i=0; i<nbrZeros; i++) {
			dec+='0';
		}

		return dec;
	}

	public int abs (int n0) {
		if (n0<0) return -n0;
		else return n0;
	}

	public void abs () {
		this.estNegatif = false;
	}

	public CalculsDouble abs (CalculsDouble n0) {
		CalculsDouble n1 = new CalculsDouble(0);
		n1.ent = n0.ent; n1.dec = n0.dec; n1.representation_anglaise 
			= n0.representation_anglaise;
		n1.estNegatif = false;

		return n1;
	}

	public int partieEntiere (double n0) {
		return ((int)(n0));
	}

	public int partieEntiere () {
		if (this.ent.isEmpty()) return 0;
		else return Integer.parseInt(this.ent);
	}


	public int arrondiEntier (CalculsDouble n0) {
		String res = arrondi(n0).ent;
		if (res.isEmpty()) return 0;
		else return Integer.parseInt(res);
	}

	public String arrondiString (CalculsDouble n1) {
		return arrondiString(n1, 0);
	}

	public String arrondiString (CalculsDouble n1, int r0) {
		return (""+arrondi(n1, r0));
	}

	public CalculsDouble arrondi (CalculsDouble n1) {
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
		if (nombre.dec.isEmpty()) return nombre;
		if (doitArrondiSuperieur(nombre.dec, 0)) {
			nombre = this.additionneNombres(nombre, new CalculsDouble(1));
		}
		nombre.dec = "";
		return nombre;
	}

	/**
	 * Propage l'arrondi dans la partie decimale.
	 * @param nombre Le nombre a modifier.
	 * @param positionDepart Position de depart pour la propagation.
	 * @return Nombre avec arrondi propage.
	 */
	private CalculsDouble propagerArrondiDecimales(CalculsDouble nombre, int positionDepart) {
		int positionCourante = positionDepart;
		boolean continuerPropagation = true;

		while (positionCourante > 0 && doitArrondiSuperieur(nombre.dec, positionCourante) && continuerPropagation) {
			int chiffrePrecedent = Character.getNumericValue(nombre.dec.charAt(positionCourante - 1)) + 1;
			String chiffreIncrement = "" + chiffrePrecedent;

			if (chiffreIncrement.length() > 1) {
				nombre.dec = nombre.dec.substring(0, positionCourante);
			} else {
				nombre.dec = nombre.dec.substring(0, positionCourante - 1) + chiffreIncrement;
				continuerPropagation = false;
			}
			positionCourante--;
		}

		if (positionCourante == 0 && doitArrondiSuperieur(nombre.dec, 0)) {
			nombre.dec = "";
			nombre.ent = "" + (Integer.parseInt(nombre.ent) + 1);
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
		if (nombre.dec.isEmpty()) {
			return nombre;
		}
		if (nombre.dec.length() <= nombreDecimales) {
			return nombre;
		}
		if (!doitArrondiSuperieur(nombre.dec, nombreDecimales)) {
			nombre.dec = nombre.dec.substring(0, nombreDecimales);
			return nombre;
		}

		return propagerArrondiDecimales(nombre, nombreDecimales);
	}

	public String partieDecimale (double n0) {
		String n0tmp = ""+n0;
		String point=".";

		//n0tmp = "16575.76812"; //test pour ',' ou '.'

		if (n0tmp.indexOf(",")!= -1) point=",";
		if(n0tmp.indexOf(point)== -1) return "0";

		boolean aTrouveLePoint=false;
		String res="";
		int lenn0tmp=n0tmp.length();
		for (int i=0; i<lenn0tmp; i++) {
			if (aTrouveLePoint) res+=n0tmp.charAt(i);
			if (n0tmp.charAt(i)==point.charAt(0)) aTrouveLePoint=true;
		}

		//aff("res = "+res);

		return res;
	}

	public CalculsDouble [] egaliseNombres  (CalculsDouble a, CalculsDouble b) {
		if (a.dec.length()!=b.dec.length()) {
			if (Math.max(a.dec.length(), b.dec.length())==a.dec.length()) {
				String tmpa="";
				int tmplena=a.dec.length()-b.dec.length();
				for (int i=0; i<tmplena; i++)
					tmpa+="0";
				b.dec+=tmpa;
			}

			if (Math.max(a.dec.length(), b.dec.length())==b.dec.length()) {
				String tmpb="";
				int tmplenb=b.dec.length()-a.dec.length();
				for (int i=0; i<tmplenb; i++)
					tmpb+="0";
				a.dec+=tmpb;
			}
		}

		CalculsDouble [] res = new CalculsDouble [2]; res[0] = a; res[1] = b;

		return res;
	}
	/**
		Copie un CalculsDouble dans un autre.
	**/
	public CalculsDouble copie (CalculsDouble a) {
		CalculsDouble res = new CalculsDouble(0);

		res.ent = a.ent;
		res.dec = a.dec;
		res.estNegatif = a.estNegatif;
		res.representation_anglaise = a.representation_anglaise;

		return res;
	}

	public boolean equals (CalculsDouble a) {
		return (compareNombres(this, a)==0);
	}
	/**
		0 : identiques
		1 : a>b
		2 : b>a

		-1 : erreur
	**/
	public int compareNombres (CalculsDouble a, CalculsDouble b) {
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
		Compare nombre avec valeur la absolue de a et de b : 

		0 : identiques
		1 : a>b
		2 : b>a
	**/
	public int compareNombres_abs (CalculsDouble a, CalculsDouble b) {
		int res=0;

		if (a.ent.length()>b.ent.length()) return 1;
		if (a.ent.length()<b.ent.length()) return 2;

		// donc a.ent.length() == b.ent.length()
		int alen = a.ent.length();

		for (int i=0;  i<alen;  i++) {
			if (Integer.parseInt(""+a.ent.charAt(i))>Integer.parseInt(""+b.ent.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.ent.charAt(i))<Integer.parseInt(""+b.ent.charAt(i)))
				return 2;
		}

		// donc les parties entières sont identiques
		int xlen = Math.min(a.dec.length(), b.dec.length());
		for (int i=0;  i<xlen;  i++) {
			if (Integer.parseInt(""+a.dec.charAt(i))>Integer.parseInt(""+b.dec.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.dec.charAt(i))<Integer.parseInt(""+b.dec.charAt(i)))
				return 2;
		}

		if (a.dec.length()!=b.dec.length() && Math.max(a.dec.length(), b.dec.length())>xlen) {
			if (a.dec.length()>xlen)
				if (Integer.parseInt(""+a.dec.charAt(xlen))>0) return 1;
			if (b.dec.length()>xlen)
				if (Integer.parseInt(""+b.dec.charAt(xlen))>0) return 2;
		}

		return 0;
	}

	/**
	 * Trouve le prochain facteur premier d'un nombre.
	 * @param nombre Le nombre a factoriser.
	 * @return Le facteur premier trouve.
	 */
	private int trouverProchainFacteurPremier(int nombre) {
		String nombreChaine = "" + nombre;
		char dernierChiffre = nombreChaine.charAt(nombreChaine.length() - 1);

		if (dernierChiffre == '0' || dernierChiffre == '5') {
			return 5;
		}
		if (dernierChiffre == '2' || dernierChiffre == '4' || dernierChiffre == '8') {
			return 2;
		}
		if (nombre % 3 == 0) {
			return 3;
		}

		for (int diviseur = 2; diviseur < nombre; diviseur++) {
			if (nombre % diviseur == 0) {
				return diviseur;
			}
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
		if (!nombre.dec.isEmpty()) {
			aff("Partie decimale ignoree, troncature entiere utilisee.");
		}
		if (nombre.ent.isEmpty()) {
			return null;
		}

		int valeurCourante = Integer.parseInt(nombre.ent);
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





	public int somme_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=0;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res+=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}

	public int produit_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=1;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res*=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}
	public boolean estPremier (int n0) {
		for (int i=2; i<1+(int)(Math.sqrt((double)(n0))); i++) {
			if (n0%i==0) return false;
		}

		return true;
	}

	public void clacule_n_premiers (int n0) {
		int i, cmp=0;

		if (n0<1) return; // rien à afficher

		int [] res = new int[n0];

		i=2;
		while (cmp<n0) {
			if (estPremier(i)) {
				res[cmp] = i;
				cmp++;
			}

			i++;
		}

		affnn("p"+n0+" = {");
		for (i=0; i<res.length-1; i++) {
			affnn(res[i]+", ");
		}
		aff(res[i]+"};");
	}

// ######################### Fonctions utilitaires ##################################### //


	/**
		Shows if a String is an Integer 
		using function Integer.parseInt(String) of Java.
	**/
	public boolean isInteger (String n0) {
		try {
			int n1 = Integer.parseInt(n0);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
		Convertit un String en entier (gestion des exceptions).
	**/
	public int convertStrInt (String n0) {
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
		Vérifie si un char est un chiffre 
		ou non.
	**/
	public boolean isCharNumber (char n0) {
		return (n0>='0' && n0<='9');
	}

	public boolean isInteger_deep (String n0) {
		if (n0.isEmpty()) return false;
		int n0len = n0.length();

		for (int i=0; i<n0len; i++) {
			if (!isCharNumber(n0.charAt(i))) return false;
		}

		return true;
	}

	/**
		Regarde si CalculsDouble n0 est un nombre.
		Renvoie true si n0.ent est un nombre
		et que n0.dec est vide ou un nombre aussi.
	**/
	public boolean isInteger (CalculsDouble n0) {
		boolean res = false;
		int i=0;
		String n0ent = n0.ent;
		String n0dec = n0.dec;

		if (!n0ent.isEmpty()) {
			res = isInteger(n0ent);
		}
		if (!n0dec.isEmpty()) {
			res = isInteger(n0dec);
		}

		return res;
	}

	/**
		Renvoie true si n0=0.0
		ou 0.
	**/
	public boolean estNul (CalculsDouble n0) {
		int i=0;
		int n0entlen = n0.ent.length();
		int n0declen = 0;

		if (!isInteger(n0)) return false;

		if (!n0.dec.isEmpty())
			n0declen = n0.dec.length();

		for (i=0; i<n0entlen; i++) {
			if (n0.ent.charAt(i)!='0')
				return false;
		}

		for (i=0; i<n0declen; i++) {
			if (n0.dec.charAt(i)!='0')
				return false;
		}

		return true;
	}

	public int compteOcurrences (String test, char cc) {
		if (test==null) return 0;
		if (test.isEmpty()) return 0;
		// Gestion des erreurs

		int res = 0, lentest = test.length();
		for (int i=0; i<lentest; i++) {
			if (test.charAt(i)==cc) res++;
		}
		return res;
	}


	public static void affnn (Object s0) {
		System.out.print(""+s0);
	}

	public static void aff (Object s0) {
		System.out.println(""+s0);
	}

	public void affTab(int [] n0) {
		for (int i=0; i<n0.length; i++) {
			aff("t["+i+"] = "+n0[i]);
		}
	}

	public void affTab(int [] n0, String nom) {
		for (int i=0; i<n0.length; i++) {
			aff(nom+"["+i+"] = "+n0[i]);
		}
	}

	public void afftab2dim(String [] t1, String nom) {
		int i;

		//aff(nom+" : ");

		for (i=0; i<t1.length; i++) {
			aff(nom+"["+i+"] = "+t1[i]);
		}
	}

	public int [] agrandieTab (int [] t0) {
		int i;
		int [] t1 = new int[t0.length*2+1];

		for (i = 0; i<t0.length; i++) {
			t1[i] = t0[i];
		}

		return t1;
	}


// ################################ toString ########################################### //

	public String toString ()  {
		String signeMoins="";
		String affent = ent;
		String dec = chiffresSignificatifs(this.dec, -1);
		char virgule = ',';
		if (representation_anglaise) virgule='.';
		String affDec=virgule+dec;

		if (affent.isEmpty()) affent = "0";

		if (!dec.isEmpty() && Integer.parseInt(dec)==0 || dec.isEmpty()) affDec="";

		if (estNegatif) signeMoins = "-";

		return (signeMoins+affent+affDec);
	}

// ######################## Reconnaissance opérations ################################## //


	Character [] operateurs_tmp = 
		{'+', '-', '*', '/'};
	Stack<String> st = new Stack<String>();
	ArrayList<Character> operateurs = new ArrayList<Character>();

	public void operateurs () {
		for (Character c1 : operateurs_tmp) {
			operateurs.add(c1);
		}
	}

	/**
		Renvoie -1 en cas d'erreur.
	**/
	public int analyse_expression_sans_parentheses (String expr) {
		if (expr==null) {
			aff("L'expression est null");
			return -1;
		}
		if (expr.isEmpty()) {
			aff("L'expression est vide.");
			return -1;
		}
		int lenexpr = expr.length();
		int cmp = 0;
		String tmp = "";

		for (int i=0; i<lenexpr; i++) {
			if (isCharNumber(expr.charAt(i))) {
				//affnn(expr.charAt(i));
				tmp = mange_nombre(expr, i);
				st.push(tmp);
				while (i<lenexpr 
				&& isCharNumber(expr.charAt(i))) i++;
			}
			if (cmp>0) calcul_pile(st);
			if (i>=lenexpr) break;
			if (operateurs.contains(expr.charAt(i))) {
				tmp = ""+mange_operateur(expr, i);
				st.push(tmp);
			}
			cmp++;
		}
		String res = pop_erreur(st);
		return convertStrInt(res);
	}

// ##################### Fonctions de traitement des symboles ######################### //

	public String pop_erreur (Stack<String> st) {
		if (!st.empty()) {
			return st.pop();
		}
		else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	public void calcul_pile (Stack<String> st) {
		String aS = pop_erreur(st);
		String symS = pop_erreur(st);
		String bS = pop_erreur(st);

		int res_tmp = traite_symbolesInt(bS, aS, symS);
		aff("res_tmp = "+res_tmp);
		st.push(""+res_tmp);
	}

	/**
		Retourne -1 en cas d'erreur.
	**/
	public int traite_symbolesInt (String aS, String bS, String symS) {
		if (symS==null || symS.isEmpty()) {
			aff("Symbole null ou egal a "+'"'+'"'+".");
			return -1;
		}
		if (!isInteger(aS) || !isInteger(bS)) {
			aff("aS (= "+aS+") ou bS (= "+bS+") ne sont pas des nombres.");
			return -1;
		}
		int a = Integer.parseInt(aS);
		int b = Integer.parseInt(bS);
		char sym = symS.charAt(0);
		if (!operateurs.contains(sym)) {
			aff("sym (= "+sym+") n'est pas un operateur.");
			return -1;
		}
		int res = 0;

		if (sym=='+') {
			res = a+b;
		}

		if (sym=='-') {
			res = a-b;
		}

		if (sym=='*') {
			res = a*b;
		}

		if (sym=='/') {
			res = a/b;
		}

		return res;
	}

// ########################### Fonction mange_symbole ################################# //

	public String verifie_double_correcte (String nombre, char sym) {
		if (nombre==null) {
			aff("Le nombre est null.");
			return "";
		}
		if (nombre.isEmpty()) {
			aff("Le nombre est vide.");
			return "";
		}
		// Gestion des erreurs
		char sym2 = '.';
		if (sym=='.') sym2 = ',';
		String [] double_tmp = nombre.split(""+sym);
		if (double_tmp==null) return "";
		if (double_tmp.length==1) {
			if (double_tmp[0]==null) return "";
			if (double_tmp[0].isEmpty()) return "";
			if (double_tmp[0].equals(",")) return "";
			return nombre.replace(""+sym, "");
		}
		if (double_tmp.length<2) return "";
		// Gestion des erreurs avec le nombre.split(sym).

		String tmp = double_tmp[1];
		int lentmp = tmp.length();
		
		for (int i=0; i<lentmp; i++) {
			if (i%3==0) {
				if (tmp.charAt(i)!=sym2) return "";
			}
			else {
				if (!isCharNumber(tmp.charAt(i)))
					return "";
			}
		}
		return double_tmp[0]+tmp.replace(""+sym2, "");
	}

	/**
		Retourne -1 en cas d'erreur.
	**/
	public double analyse_nombreDouble (String nombre) {
		int i = 0;
		int nbr_virgules = 0, nbr_points = 0;
		boolean virgule = false;
		boolean point = false;

		nbr_virgules = compteOcurrences(nombre, ',');
		nbr_points = compteOcurrences(nombre, '.');

		aff("nbr_virgules = "+nbr_virgules);
		aff("nbr_points = "+nbr_points);

		if (nbr_virgules==0 && nbr_points==0) {
			if (isInteger(nombre)) {
				return Integer.parseInt(nombre);
			}
		}
		else {
			if (nbr_virgules==1 && nbr_points>1) {
				nombre = verifie_double_correcte (nombre, ',');
			}

			if (nbr_virgules>1 && nbr_points==1) {
				nombre = verifie_double_correcte (nombre, '.');
			}

			nbr_virgules = compteOcurrences(nombre, ',');
			nbr_points = compteOcurrences(nombre, '.');

			aff("nbr_virgules = "+nbr_virgules);
			aff("nbr_points = "+nbr_points);
			aff("nombre = "+nombre);

			if (nbr_virgules>1 && nbr_points>1)
				return -1;
			if (nbr_virgules==1 && nbr_points==0) {
				return Double.parseDouble(
				nombre.replace(',', '.'));
			}
			if (nbr_virgules==0 && nbr_points==1) {
				return Double.parseDouble(nombre);	
			}
		}
		return -1;		
	}

	public String mange_nombre (String expr, int indice_debut) {
		int lenexpr = expr.length();
		boolean virgules = false;
		boolean points = false;
		boolean isDigit = true;
		String res = "";

		for (int i=indice_debut; i<lenexpr && isDigit; i++) {
			if (isCharNumber(expr.charAt(i))) {
				res+=expr.charAt(i);
			}
			if (expr.charAt(i)==',') {
				virgules = true;
				res+=expr.charAt(i);
			}
			if (expr.charAt(i)=='.') {
				points = true;
				res+=expr.charAt(i);
			}
			else isDigit = false;
		}
		aff(res);
		if (virgules || points) {
			res = ""+analyse_nombreDouble(res);
			return res;
		}
		else return res;
	}

	public char mange_operateur (String expr, int indice) {
		int lenexpr = expr.length();

		if (indice>=0 && indice<lenexpr) {
			aff(expr.charAt(indice));
			return expr.charAt(indice);
		}
		else return (char)(-1);
	}
	
// ##################### Main ####################### //

	public static void main (String [] args) {
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
