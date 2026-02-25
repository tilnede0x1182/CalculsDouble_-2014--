
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
		int longueurEntiere = nombre.partieEntiere.length();
		int longueurDecimale = 0;
		if (!nombre.partieDecimale.isEmpty()) {
			longueurDecimale = nombre.partieDecimale.length();
		}
		for (int index = 0; index < longueurEntiere; index++) {
			if (nombre.partieEntiere.charAt(index) != '0') {
				return false;
			}
		}
		for (int index = 0; index < longueurDecimale; index++) {
			if (nombre.partieDecimale.charAt(index) != '0') {
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
	 * @return 1 si A > B, -1 si A < B, 0 si egal.
	 */
	public int comparerChaines(String chaineA, String chaineB) {
		String paddedA = padGauche(chaineA, Math.max(chaineA.length(), chaineB.length()));
		String paddedB = padGauche(chaineB, Math.max(chaineA.length(), chaineB.length()));
		return paddedA.compareTo(paddedB);
	}

	/**
	 * Soustrait deux chaines numeriques (gere A < B).
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
