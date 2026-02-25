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
			resultat.partieEntiere = additionneNombres(valeurLigne, resultat).ent;
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
